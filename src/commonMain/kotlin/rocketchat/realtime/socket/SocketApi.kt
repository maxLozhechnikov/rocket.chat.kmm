package com.omegar.libs.rocketchat.realtime.socket

import com.omegar.libs.rocketchat.ChatLogger
import com.omegar.libs.rocketchat.DefaultSerializer
import com.omegar.libs.rocketchat.model.*
import com.omegar.libs.rocketchat.realtime.model.*
import com.omegar.libs.rocketchat.realtime.model.socket.*
import com.omegar.libs.rocketchat.realtime.model.socket.channel.SubscriptionsChannels
import com.omegar.libs.rocketchat.realtime.model.socket.channel.UserStatusMessage
import com.omegar.libs.rocketchat.realtime.model.socket.channel.UserTypingMessage
import com.omegar.libs.rocketchat.rest.RestClient
import io.ktor.websocket.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import kotlin.jvm.Synchronized
import kotlin.jvm.Volatile


class SocketApi(
    private val restClient: RestClient,
    private val logger: ChatLogger,
    private val subscriptionsChannels: SubscriptionsChannels
)
{
    companion object {
        private const val SELF_DISCONNECT_MESSAGE = "Self disconnect"
    }
    private var currentSession: WebSocketSession? = null
    @Volatile
    private var currentId: Int = 0
    private var selfDisconnect = false
    private val reconnectionStrategy = ReconnectionStrategy()
    internal var currentState: ConnectionState = ConnectionState.DISCONNECTED
    private var activeSubscriptions: MutableSet<String> = mutableSetOf()
    private val userStatuses = mapOf(0 to UserStatus.OFFLINE, 1 to UserStatus.ONLINE, 2 to UserStatus.AWAY, 3 to UserStatus.BUSY)

    private suspend fun connect() {
        logger.log(ChatLogger.Level.INFO, "socket", "connecting")
        sendWebSocketMessage(SocketMessageFactory.connectMessageDefault)
    }

    internal suspend fun startWebSocketSession() {
        try {
            currentState = ConnectionState.CONNECTING
            restClient.createWebSocket {
                currentSession = this
                currentId = 0
                selfDisconnect = false
                reconnectionStrategy.reset()
                activeSubscriptions.clear()
                connect()

                incoming.consumeEach { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            val serverMessage = DefaultSerializer.fromJson<SocketMessage>(frame.readText())
                            logger.log(ChatLogger.Level.DEBUG, "WebSocketSession", "RAW Message ${frame.readText()}")
                            processIncomingMessage(serverMessage)
                        }
                        else -> {
                            // nothing?
                        }
                    }
                }
            }
        } catch (cause: Throwable) {
            logger.log(ChatLogger.Level.ERROR, "SocketSession Error", cause.message?: "")
        } finally {
            onDisconnect()
        }
    }

    internal suspend fun disconnect(after: Long = 0) {
        delay(after)
        selfDisconnect = true
        currentSession?.close(CloseReason(CloseReason.Codes.NORMAL, SELF_DISCONNECT_MESSAGE))
    }

    private suspend fun onDisconnect() {
        logger.log(ChatLogger.Level.INFO, "SocketSession", "Disconnected")
        currentState = ConnectionState.DISCONNECTED
        if (!selfDisconnect) {
            delay(reconnectionStrategy.reconnectInterval.toLong())
            reconnectionStrategy.processAttempts()
            startWebSocketSession()
        }
    }

    private suspend fun processIncomingMessage(message: SocketMessage) {

        logger.log(ChatLogger.Level.DEBUG, "processIncomingMessage", "Message: $message")

        when (currentState) {
            ConnectionState.CONNECTING -> {
                if (message.type == SocketMessageCollectionType.CONNECTED){
                    currentState = ConnectionState.AUTHENTICATING
                    logger.log(ChatLogger.Level.INFO, "socket", "authenticating")
                    restClient.currentAuthToken?.let {
                        sendWebSocketMessage(SocketMessageFactory.createLoginMessage(generateId(), it))
                    }
                }
                if (message.type == SocketMessageCollectionType.PING){
                    logger.log(ChatLogger.Level.DEBUG, "processMessage", "Message type: ${message.type}")
                    sendWebSocketMessage(SocketMessageFactory.createPongMessage)
                }
            }
            ConnectionState.AUTHENTICATING -> {
                if (message.type == SocketMessageCollectionType.ADDED){
                    currentState = ConnectionState.CONNECTED
                    logger.log(ChatLogger.Level.INFO, "socket", "connected")
                }
                if (message.type == SocketMessageCollectionType.PING){
                    logger.log(ChatLogger.Level.DEBUG, "processMessage", "Message type: ${message.type}")
                    sendWebSocketMessage(SocketMessageFactory.createPongMessage)
                }
            }
            else -> {
                processMessage(message)
            }
        }
    }

    private suspend fun processMessage(message: SocketMessage) {
        when (message.type) {
            SocketMessageCollectionType.PING -> {
                logger.log(ChatLogger.Level.DEBUG, "processMessage", "Message type: ${message.type}")
                sendWebSocketMessage(SocketMessageFactory.createPongMessage)
            }
            SocketMessageCollectionType.CHANGED -> {
                processSubscriptionsChanged(message)
            }
            SocketMessageCollectionType.READY -> {
                processSubscriptionResult(message)
            }
            SocketMessageCollectionType.UNSUBSCRIBED -> {
                processUnsubscriptionResult(message)
            }
            else -> {
                logger.log(ChatLogger.Level.DEBUG, "processMessage", "Ignoring message type: ${message.type}")
            }
        }
    }

    private suspend fun processSubscriptionsChanged(socketMessage: SocketMessage) {
        when (socketMessage.collectionType) {
            SocketMessageCollection.STREAM_NOTIFY_USER -> {
                processNotifyUserStream(socketMessage)
            }
            SocketMessageCollection.STREAM_ROOM_MESSAGES -> {
                processRoomMessage(socketMessage)
            }
            SocketMessageCollection.STREAM_NOTIFY_ROOM -> {
                processNotifyRoomStream(socketMessage)
            }
            SocketMessageCollection.STREAM_NOTIFY_LOGGED -> {
                processSubscribeUserStatus(socketMessage)
            }
            SocketMessageCollection.USERS -> {
                //TODO: process users
                logger.log(
                    ChatLogger.Level.DEBUG,
                    "processSubscriptionsChanged",
                    "Ignoring SocketMessageCollection.USERS")
            }
            else -> {
                logger.log(
                    ChatLogger.Level.DEBUG,
                    "processSubscriptionsChanged",
                    "Ignoring collection type: ${socketMessage.collectionType}")
            }
        }
    }

    private suspend fun processRoomMessage(socketMessage: SocketMessage) {
        try {
            val messageString = socketMessage.getResponseArray()?.get(0)?.toString()
            val message = messageString?.let { DefaultSerializer.fromJson<Message>(it) }
            message?.let { subscriptionsChannels.messagesChannel.send(it) }
            logger.log(ChatLogger.Level.DEBUG, "processRoomMessage", "message: $message")
        } catch (ex: Exception) {
            ex.message?.let { logger.log(ChatLogger.Level.ERROR, "processRoomMessage", it) }
        }
    }

    private suspend fun processNotifyUserStream(socketMessage: SocketMessage) {
        try {
            val array = socketMessage.getResponseArray()
            val state = array?.get(0)?.toString()
            val data = array?.get(1)?.toString()
            val streamType = socketMessage.getEventName()?.getStreamType()
            when (streamType) {
                SocketMessageFactory.PARAMETER_METHOD_ROOMS_CHANGED -> {
                    processRoomStream(state, data)
                }
                SocketMessageFactory.PARAMETER_METHOD_SUBSCRIPTIONS_CHANGED -> {
                    processSubscriptionStream(state, data)
                }
            }

        } catch (ex: Exception) {
            ex.message?.let { logger.log(ChatLogger.Level.ERROR, "processNotifyUserStream", it) }
        }
    }

    internal suspend fun processNotifyRoomStream(socketMessage: SocketMessage) {
        try {
            when (socketMessage.getEventName()?.getStreamType()) {
                SocketMessageFactory.PARAMETER_METHOD_TYPING -> {
                    val array = socketMessage.getResponseArray()
                    val username = array?.get(0)?.toString()
                    val isTyping = array?.get(1)?.jsonPrimitive?.boolean
                    subscriptionsChannels.usersTypingChannel.send(UserTypingMessage(username, isTyping))
                    logger.log(
                        ChatLogger.Level.DEBUG,
                        "processNotifyRoomStream",
                        "user: $username, isTyping: $isTyping"
                    )
                }
            }
        } catch (ex: Exception) {
            ex.message?.let { logger.log(ChatLogger.Level.ERROR, "processNotifyRoomStream", it) }
        }
    }

    internal suspend fun processSubscribeUserStatus(socketMessage: SocketMessage) {
        try {
            when (socketMessage.getEventName()) {
                SocketMessageFactory.PARAMETER_METHOD_USER_STATUS -> {
                    val array = socketMessage.getResponseArray()
                    val userId = array?.get(0)?.toString()
                    val username = array?.get(1)?.toString()
                    val userStatusId = array?.get(2)?.jsonPrimitive?.int
                    val userStatus: UserStatus? = userStatuses[userStatusId]
                    subscriptionsChannels.usersStatusChannel.send(UserStatusMessage(userId, username, userStatus))
                    logger.log(
                        ChatLogger.Level.DEBUG,
                        "processSubscribeUserStatus",
                        "user: $username, userStatus: $userStatus"
                    )
                }
            }
        } catch (ex: Exception) {
            ex.message?.let { logger.log(ChatLogger.Level.ERROR, "processNotifyRoomStream", it) }
        }
    }

    internal suspend fun processRoomStream(state: String?, data: String?) {
        val room = data?.let { DefaultSerializer.fromJson<Room>(it) }
        room?.let { subscriptionsChannels.roomChannel.send(it) }
        logger.log(ChatLogger.Level.DEBUG, "processRoomStream", "state: $state, room: $room")
    }

    internal suspend fun processSubscriptionStream(state: String?, data: String?) {
        val subscription = data?.let { DefaultSerializer.fromJson<Subscription>(it) }
        subscription?.let { subscriptionsChannels.subscriptionChannel.send(it) }
        logger.log(ChatLogger.Level.DEBUG, "processSubscriptionStream", "state: $state, subscription: $subscription")
    }

    internal fun processSubscriptionResult(socketMessage: SocketMessage) {
        socketMessage.subscriptions?.let { activeSubscriptions.addAll(it)}
        logger.log(ChatLogger.Level.DEBUG, "activeSubscriptions", ": $activeSubscriptions")
    }

    internal fun processUnsubscriptionResult(socketMessage: SocketMessage) {
        socketMessage.id?.let { activeSubscriptions.remove(it)}
        logger.log(ChatLogger.Level.DEBUG, "activeSubscriptions", ": $activeSubscriptions")
    }

    internal suspend fun unsubscribeAll() {
        activeSubscriptions.forEach { unsubscribe(it) }
    }

    private fun String.getStreamType(): String =
        split(SocketMessageFactory.PARAMETER_METHOD_SEPARATOR)[1]

    @Synchronized
    internal fun generateId(): String {
        currentId++
        return currentId.toString()
    }

    internal suspend fun sendWebSocketMessage(socketMessage: SocketMessage) {
        val message = DefaultSerializer.toJson(socketMessage)
        logger.log(ChatLogger.Level.DEBUG, "Send SocketMessage", "message: $message")
        currentSession?.send(message)
    }

    internal suspend fun sendSubscriptionMessages() {
        subscribeSubscriptions()
        subscribeRooms()
    }

    suspend fun setTypingStatus(roomId: String, username: String, isTyping: Boolean) {
        logger.log(ChatLogger.Level.DEBUG, "setTypingStatus", "state: $isTyping")
        sendWebSocketMessage(SocketMessageFactory.createTypingMessage(generateId(), roomId, username, isTyping))
    }

    internal suspend fun subscribeTypingStatus(roomId: String): String {
        val id = generateId()
        sendWebSocketMessage(SocketMessageFactory.createStreamTypingMessage(id, roomId))
        return id
    }

    internal suspend fun subscribeRooms(): String {
        val id = generateId()
        sendWebSocketMessage(SocketMessageFactory.createRoomsStreamMessage(id, restClient.currentUserId))
        return id
    }

    internal suspend fun subscribeRoomMessages(roomId: String): String {
        val id = generateId()
        sendWebSocketMessage(SocketMessageFactory.createStreamRoomMessagesMessage(id, roomId))
        return id
    }

    internal suspend fun createDirectMessage(username: String): String {
        val id = generateId()
        sendWebSocketMessage(SocketMessageFactory.createDirectMessage(id, username))
        return id
    }

    internal suspend fun subscribeSubscriptions(): String {
        val id = generateId()
        sendWebSocketMessage(SocketMessageFactory.createSubscriptionsStreamMessage(id, restClient.currentUserId))
        return id
    }

    internal suspend fun unsubscribe(subId: String) {
        sendWebSocketMessage(SocketMessageFactory.createUnsubscribeMessage(subId))
    }

    internal suspend fun setDefaultStatus(status: UserStatus) {
        val id = generateId()
        sendWebSocketMessage(SocketMessageFactory.createDefaultStatusMessage(id, status))
    }

    internal suspend fun setTemporaryStatus(status: UserStatus) {
        when (status) {
            UserStatus.ONLINE, UserStatus.AWAY -> {
                val id = generateId()
                sendWebSocketMessage(SocketMessageFactory.createTemporaryStatusMessage(id, status))
            }
            else -> {
                logger.log(
                    ChatLogger.Level.ERROR,
                    "setTemporaryStatus",
                    "Only \"UserStatus.Online\" and \"UserStatus.Away\" are accepted as temporary status"
                )
            }
        }
    }

    internal suspend fun subscribeUserData(): String {
        val id = generateId()
        sendWebSocketMessage(SocketMessageFactory.createUserDataChangesMessage(id))
        return id
    }

    internal suspend fun subscribeActiveUsers(): String {
        val id = generateId()
        sendWebSocketMessage(SocketMessageFactory.createActiveUsersMessage(id))
        return id
    }

    internal suspend fun subscribeUserStatus(): String {
        val id = generateId()
        sendWebSocketMessage(SocketMessageFactory.createSubscriptionUserStatus(id))
        return id
    }
}