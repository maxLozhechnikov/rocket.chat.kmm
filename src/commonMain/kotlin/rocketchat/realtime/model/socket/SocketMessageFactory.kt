package com.omegar.libs.rocketchat.realtime.model.socket

import com.omegar.libs.rocketchat.model.UserStatus

object SocketMessageFactory {
    private const val PARAMETER_VERSION_1 = "1"
    private const val PARAMETER_VERSION_PRE1 = "pre1"
    private const val PARAMETER_VERSION_PRE2 = "pre2"
    private const val PARAMETER_METHOD_LOGIN = "login"
    private const val PARAMETER_USER_PRESENCE = "UserPresence:"
    private const val PARAMETER_USER_PRESENCE_DEFAULT = "setDefaultStatus"
    private const val PARAMETER_METHOD_STREAM_NOTIFY_ROOM = "stream-notify-room"
    internal const val PARAMETER_METHOD_TYPING = "typing"
    private const val PARAMETER_METHOD_CREATE_DIRECT_MESSAGE = "createDirectMessage"
    private const val PARAMETER_METHOD_STREAM_NOTIFY_USER = "stream-notify-user"
    private const val PARAMETER_METHOD_STREAM_ROOM_MESSAGES = "stream-room-messages"
    private const val PARAMETER_METHOD_USER_DATA = "userData"
    private const val PARAMETER_METHOD_ACTIVE_USERS = "activeUsers"
    internal const val PARAMETER_METHOD_SUBSCRIPTIONS_CHANGED = "subscriptions-changed"
    internal const val PARAMETER_METHOD_ROOMS_CHANGED = "rooms-changed"
    internal const val PARAMETER_METHOD_SEPARATOR = "/"
    private const val PARAMETER_METHOD_STREAM_NOTIFY_LOGGED = "stream-notify-logged"
    internal const val PARAMETER_METHOD_USER_STATUS = "user-status"

    internal val connectMessageDefault: SocketMessage =
        SocketMessage(
            type = SocketMessageCollectionType.CONNECT,
            version = PARAMETER_VERSION_1,
            support = listOf(
                PARAMETER_VERSION_1,
                PARAMETER_VERSION_PRE1,
                PARAMETER_VERSION_PRE2
            )
        )

    internal val createPingMessage = SocketMessage(type = SocketMessageCollectionType.PING)

    internal val createPongMessage = SocketMessage(type = SocketMessageCollectionType.PONG)

    internal fun createMethodMessage(method: String, id: String, params: List<ParamValue>? = null): SocketMessage =
        SocketMessage(
            type = SocketMessageCollectionType.METHOD,
            id = id,
            method = method,
            params = params
        )

    internal fun createLoginMessage(id: String, token: String): SocketMessage =
        createMethodMessage(
            PARAMETER_METHOD_LOGIN,
            id,
            ParamValue.paramList(
                SocketParameterLogin(token)
            )
        )

    internal fun createDefaultStatusMessage(id: String, status: UserStatus): SocketMessage =
        createMethodMessage(
            PARAMETER_USER_PRESENCE + PARAMETER_USER_PRESENCE_DEFAULT,
            id,
            ParamValue.paramList(status)
        )

    internal fun createTemporaryStatusMessage(id: String, status: UserStatus): SocketMessage =
        createMethodMessage(
            PARAMETER_USER_PRESENCE + status.toString().toLowerCase(),
            id
        )

    internal fun createTypingMessage(id: String, roomId: String, username: String, isTyping: Boolean): SocketMessage =
        createMethodMessage(
            PARAMETER_METHOD_STREAM_NOTIFY_ROOM,
            id,
            ParamValue.paramList(
                createMethodName(roomId, PARAMETER_METHOD_TYPING),
                username,
                isTyping
            )
        )

    internal fun createDirectMessage(id: String, username: String): SocketMessage {
        return createMethodMessage(
            PARAMETER_METHOD_CREATE_DIRECT_MESSAGE,
            id,
            ParamValue.paramList(username)
        )
    }

    private fun createSubscriptionMessage(name: String, id: String, params: List<ParamValue>? = null): SocketMessage =
        SocketMessage(
            type = SocketMessageCollectionType.SUBSCRIBE,
            id = id,
            name = name,
            params = params
        )

    internal fun createUnsubscribeMessage(id: String): SocketMessage =
        SocketMessage(
            type = SocketMessageCollectionType.UNSUBSCRIBE,
            id = id
        )

    internal fun createSubscriptionsStreamMessage(id: String, userId: String): SocketMessage =
        createSubscriptionMessage(
            PARAMETER_METHOD_STREAM_NOTIFY_USER,
            id,
            ParamValue.paramList(
                createMethodName(userId, PARAMETER_METHOD_SUBSCRIPTIONS_CHANGED),
                false
            )
        )

    internal fun createRoomsStreamMessage(id: String, userId: String): SocketMessage =
        createSubscriptionMessage(
            PARAMETER_METHOD_STREAM_NOTIFY_USER,
            id,
            ParamValue.paramList(
                createMethodName(userId, PARAMETER_METHOD_ROOMS_CHANGED),
                false
            )
        )

    internal fun createStreamRoomMessagesMessage(id: String, roomId: String): SocketMessage =
        createSubscriptionMessage(
            PARAMETER_METHOD_STREAM_ROOM_MESSAGES,
            id,
            ParamValue.paramList(
                roomId,
                false
            )
        )

    internal fun createUserDataChangesMessage(id: String): SocketMessage =
        createSubscriptionMessage(
            PARAMETER_METHOD_USER_DATA,
            id
        )

    internal fun createActiveUsersMessage(id: String): SocketMessage =
        createSubscriptionMessage(
            PARAMETER_METHOD_ACTIVE_USERS,
            id
        )

    internal fun createStreamTypingMessage(id: String, roomId: String): SocketMessage =
        createSubscriptionMessage(
            PARAMETER_METHOD_STREAM_NOTIFY_ROOM,
            id,
            ParamValue.paramList(
                createMethodName(roomId, PARAMETER_METHOD_TYPING),
                false
            )
        )

    internal fun createSubscriptionUserStatus(id: String): SocketMessage =
        createSubscriptionMessage(
            PARAMETER_METHOD_STREAM_NOTIFY_LOGGED,
            id,
            ParamValue.paramList(
                PARAMETER_METHOD_USER_STATUS,
                false
            )
        )

    private fun createMethodName(vararg path: String): String {
        return path.joinToString(separator = PARAMETER_METHOD_SEPARATOR)
    }
}