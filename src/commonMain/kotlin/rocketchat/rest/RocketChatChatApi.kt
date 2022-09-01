package com.omegar.libs.rocketchat.rest

import com.omegar.libs.rocketchat.DefaultSerializer
import com.omegar.libs.rocketchat.ChatLogger
import com.omegar.libs.rocketchat.model.*
import com.omegar.libs.rocketchat.model.internal.*
import com.omegar.libs.rocketchat.model.Command
import io.ktor.http.HttpMethod


class RocketChatChatApi(private val restClient: RestClient, private val logger: ChatLogger) {
    companion object {
        private const val FUNCTION_GET_MEMBERS = "members"
        private const val FUNCTION_GET_MENTIONS = "channels.getAllUserMentionsByChannel"
        private const val FUNCTION_GET_MESSAGES = "messages"
        private const val FUNCTION_GET_FILES = "files"
        private const val FUNCTION_GET_INFO = "info"
        private const val FUNCTION_MARK_AS_READ = "subscriptions.read"
        private const val FUNCTION_JOIN = "channels.join"
        private const val FUNCTION_LEAVE = "leave"
        private const val FUNCTION_RENAME = "rename"
        private const val FUNCTION_SET_READ_ONLY = "setReadOnly"
        private const val FUNCTION_SET_ROOM_TYPE = "setType"
        private const val FUNCTION_SET_JOIN_CODE = "setJoinCode"
        private const val FUNCTION_SET_TOPIC = "setTopic"
        private const val FUNCTION_SET_DESCRIPTION = "setDescription"
        private const val FUNCTION_SET_ANNOUNCEMENT = "setAnnouncement"
        private const val FUNCTION_HIDE = "close"
        private const val FUNCTION_SHOW = "open"
        private const val FUNCTION_FAVORITE = "rooms.favorite"
        private const val FUNCTION_SEARCH = "chat.search"
        private const val FUNCTION_CHAT_ROOM_ROLES = "roles"
        private const val FUNCTION_SAVE_NOTIFICATION = "rooms.saveNotification"
        private const val FUNCTION_KICK = "kick"
        private const val FUNCTION_INVITE = "invite"
        private const val FUNCTION_CLOSE_DIRECT_MESSAGE = "im.close"
        private const val FUNCTION_GET_COMMANDS = "commands.list"
        private const val FUNCTION_RUN_COMMAND = "commands.run"
        private const val FUNCTION_PUSH_TOKEN = "push.token"
        private const val FUNCTION_GET_CUSTOM_EMOJIS = "emoji-custom.list"
        private const val FUNCTION_GET_PERMISSIONS = "permissions.listAll"
        private const val FUNCTION_GET_SPOTLIGHT = "spotlight"

        private const val PARAMETER_ROOM_ID = "roomId"
        private const val PARAMETER_OFFSET = "offset"
        private const val PARAMETER_COUNT = "count"
        private const val PARAMETER_SORT = "sort"
        private const val PARAMETER_QUERY = "query"
        private const val PARAMETER_ROOM_NAME = "roomName"
        private const val PARAMETER_SEARCH_TEXT = "searchText"
        private const val PARAMETER_DEFAULT_APP_NAME = "MyApp"
    }

    suspend fun getMembers(
        roomId: String,
        roomType: RoomType,
        offset: Long,
        count: Long
    ): RestResponseMembers {
        val queryParameters = restClient.generateQueryString(
            PARAMETER_ROOM_ID to roomId,
            PARAMETER_OFFSET to offset,
            PARAMETER_COUNT to count
        )
        val result = restClient.createGetRequest<RestResponseMembers>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_GET_MEMBERS),
            queryParameters
        )
        logger.log(ChatLogger.Level.DEBUG, "getMembers", "Message from the server: $result")
        return result
    }

    suspend fun getMentions(roomId: String, offset: Long, count: Long): RestResponseMentions {
        val queryParameters = restClient.generateQueryString(
            PARAMETER_ROOM_ID to roomId,
            PARAMETER_OFFSET to offset,
            PARAMETER_COUNT to count,
            PARAMETER_SORT to DefaultSerializer.toJson(SortPayload(displayTimeSortOrder = -1))
        )
        val result = restClient.createGetRequest<RestResponseMentions>(FUNCTION_GET_MENTIONS, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "getMentions", "Message from the server: $result")
        return result
    }

    suspend fun getMessagesQuery(
        roomId: String,
        roomType: RoomType,
        offset: Long,
        query: QueryPayload
    ): RestResponseMessages {
        val queryParameters = restClient.generateQueryString(
            PARAMETER_ROOM_ID to roomId,
            PARAMETER_OFFSET to offset,
            PARAMETER_QUERY to DefaultSerializer.toJson(query)
        )
        val result = restClient.createGetRequest<RestResponseMessages>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_GET_MESSAGES),
            queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "getMessagesQuery", "Message from the server: $result")
        return result
    }

    suspend fun getFiles(
        roomId: String,
        roomType: RoomType,
        offset: Long = 0
    ): RestResponseFiles {
        val queryParameters = restClient.generateQueryString(
            PARAMETER_ROOM_ID to roomId,
            PARAMETER_OFFSET to offset,
            PARAMETER_SORT to DefaultSerializer.toJson(SortPayload(uploadTimeSortOrder = -1))
        )
        val result = restClient.createGetRequest<RestResponseFiles>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_GET_FILES),
            queryParameters
        )
        logger.log(ChatLogger.Level.DEBUG, "getFiles", "Message from the server: $result")
        return result
    }

    suspend fun getInfo(
        roomId: String? = null,
        roomName: String? = null,
        roomType: RoomType
    ): Room? {
        if (roomType != RoomType.CHANNEL && roomType != RoomType.PRIVATE_GROUP) return null
        val queryList: MutableList<Pair<String, Any>> = mutableListOf()
        roomId?.let {
            queryList.add(PARAMETER_ROOM_ID to it)
        }
        roomName?.let {
            queryList.add(PARAMETER_ROOM_NAME to it)
        }

        val queryParameters = restClient.generateQueryString(*queryList.toTypedArray())
        val result = restClient.createGetRequest<RestResponseInfo>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_GET_INFO),
            queryParameters
        )
        logger.log(ChatLogger.Level.DEBUG, "getInfo", "Message from the server: $result")

        return when(roomType) {
            RoomType.CHANNEL -> result.channelRoom
            RoomType.PRIVATE_GROUP -> result.groupRoom
            else -> null
        }
    }

    suspend fun markAsRead(roomId: String): Boolean {
        val payload = RidPayload(roomId = roomId)
        val result = restClient.createPostRequest<DefaultResult>(
            FUNCTION_MARK_AS_READ,
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "markAsRead", "Message from the server: $result")
        return result.success
    }

    suspend fun joinChat(roomId: String): Boolean {
        val payload = RoomIdPayload(roomId = roomId)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_JOIN, payload)
        logger.log(ChatLogger.Level.DEBUG, "joinChat", "Message from the server: $result")
        return result.success
    }

    suspend fun leaveChat(roomId: String, roomType: RoomType): Boolean {
        val payload = RoomIdPayload(roomId = roomId)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_LEAVE),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "leaveChat", "Message from the server: $result")
        return result.success
    }

    suspend fun rename(roomId: String, roomType: RoomType, newName: String): Boolean {
        val payload = RoomRenamePayload(roomId = roomId, name = newName)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_RENAME),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "rename", "Message from the server: $result")
        return result.success
    }

    suspend fun setReadOnly(roomId: String, roomType: RoomType, readOnly: Boolean):Boolean {
        val payload = ChatRoomReadOnlyPayload(roomId, readOnly)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_SET_READ_ONLY),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "setReadOnly", "Message from the server: $result")
        return result.success
    }

    suspend fun setType(roomId: String, roomType: RoomType, newType: RoomType): Boolean {
        val payload = RoomTypePayload(roomId, newType)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_SET_ROOM_TYPE),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "setType", "Message from the server: $result")
        return result.success
    }

    suspend fun setJoinCode(roomId: String, roomType: RoomType, joinCode: String): Boolean {
        val payload = ChatRoomJoinCodePayload(roomId, joinCode)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_SET_JOIN_CODE),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "setJoinCode", "Message from the server: $result")
        return result.success
    }

    suspend fun setTopic(roomId: String, roomType: RoomType, topic: String?): Boolean {
        val payload = ChatRoomTopicPayload(roomId, topic)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_SET_TOPIC),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "setTopic", "Message from the server: $result")
        return result.success
    }

    suspend fun setDescription(roomId: String, roomType: RoomType, description: String?): Boolean {
        val payload = ChatRoomDescriptionPayload(roomId, description)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_SET_DESCRIPTION),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "setDescription", "Message from the server: $result")
        return result.success
    }

    suspend fun setAnnouncement(roomId: String, roomType: RoomType, announcement: String?): Boolean {
        val payload = ChatRoomAnnouncementPayload(roomId, announcement)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_SET_ANNOUNCEMENT),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "setAnnouncement", "Message from the server: $result")
        return result.success
    }

    suspend fun hide(roomId: String, roomType: RoomType): Boolean {
        val payload = RoomIdPayload(roomId)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_HIDE),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "hide", "Message from the server: $result")
        return result.success
    }

    suspend fun show(roomId: String, roomType: RoomType): Boolean {
        val payload = RoomIdPayload(roomId)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_SHOW),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "show", "Message from the server: $result")
        return result.success
    }

    suspend fun favorite(roomId: String, favorite: Boolean): Boolean {
        val payload = ChatRoomFavoritePayload(roomId, favorite)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_FAVORITE, payload)
        logger.log(ChatLogger.Level.DEBUG, "favorite", "Message from the server: $result")
        return result.success
    }

    suspend fun searchMessages(roomId: String, searchText: String): RestResponseMessages {
        val queryParameters = restClient.generateQueryString(
            PARAMETER_ROOM_ID to roomId,
            PARAMETER_SEARCH_TEXT to searchText
        )
        val result = restClient.createGetRequest<RestResponseMessages>(FUNCTION_SEARCH, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "searchMessages", "Message from the server: $result")
        return result
    }

    suspend fun chatRoomRoles(roomType: RoomType, roomName: String): RestResponseChatRoomRoles {
        val queryParameters = restClient.generateQueryString(PARAMETER_ROOM_NAME to roomName)
        val result = restClient.createGetRequest<RestResponseChatRoomRoles>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_CHAT_ROOM_ROLES),
            queryParameters
        )
        logger.log(ChatLogger.Level.DEBUG, "searchMessages", "Message from the server: $result")
        return result
    }

    suspend fun saveNotification(roomId: String, disable: Boolean): Boolean {
        val notificationsPayload = NotificationsPayload(disableNotifications = disable)
        val payload = SaveNotificationPayload(roomId = roomId, notifications = notificationsPayload)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_SAVE_NOTIFICATION, data =  payload)
        logger.log(ChatLogger.Level.DEBUG, "saveNotification", "Message from the server: $result")
        return result.success
    }

    suspend fun kickUser(roomId: String, roomType: RoomType, userId: String): Boolean {
        val payload = UserIdRoomIdPayload(roomId, userId)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_KICK),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "kickUser", "Message from the server: $result")
        return result.success
    }

    suspend fun inviteUser(roomId: String, roomType: RoomType, userId: String): Boolean {
        val payload = UserIdRoomIdPayload(roomId, userId)
        val result = restClient.createPostRequest<DefaultResult>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_INVITE),
            payload
        )
        logger.log(ChatLogger.Level.DEBUG, "inviteUser", "Message from the server: $result")
        return result.success
    }

    suspend fun closeDirectMessages(roomId: String): Boolean {
        val payload = RoomIdPayload(roomId)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_CLOSE_DIRECT_MESSAGE, payload)
        logger.log(ChatLogger.Level.DEBUG, "closeDirectMessages", "Message from the server: $result")
        return result.success
    }

    suspend fun getCommands(offset: Long = 0, count: Long = 0): RestResponseCommands {
        val queryParameters = restClient.generateQueryString(PARAMETER_OFFSET to offset,PARAMETER_COUNT to count)
        val result = restClient.createGetRequest<RestResponseCommands>(FUNCTION_GET_COMMANDS, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "getCommands", "Message from the server: $result")
        return result
    }

    suspend fun runCommand(command: Command, roomId: String): Boolean {
        val payload = CommandPayload(command = command.command, roomId = roomId, params = command.params)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_RUN_COMMAND, payload)
        logger.log(ChatLogger.Level.DEBUG, "runCommand", "Message from the server: $result")
        return result.success
    }

    suspend fun registerPushToken(
        token: String,
        pushType: PushType = PushType.GCM,
        appName: String = PARAMETER_DEFAULT_APP_NAME
    ): Boolean {
        val payload = PushRegistrationPayload(pushType, token, appName)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_PUSH_TOKEN, payload)
        logger.log(ChatLogger.Level.DEBUG, "registerPushToken", "Message from the server: $result")
        return result.success
    }

    suspend fun unregisterPushToken(token: String): Boolean {
        val payload = PushUnregistrationPayload(token)
        val result = restClient.createRequest<DefaultResult>(FUNCTION_PUSH_TOKEN, payload, requestType =  HttpMethod.Delete)
        logger.log(ChatLogger.Level.DEBUG, "unregisterPushToken", "Message from the server: $result")
        return result.success
    }

    suspend fun getCustomEmojis(): RestResponseCustomEmojis {
        val result = restClient.createGetRequest<RestResponseCustomEmojis>(FUNCTION_GET_CUSTOM_EMOJIS)
        logger.log(ChatLogger.Level.DEBUG, "getCustomEmojis", "Message from the server: $result")
        return result
    }

    suspend fun getPermissions(): RestResponsePermissions {
        val result = restClient.createGetRequest<RestResponsePermissions>(FUNCTION_GET_PERMISSIONS)
        logger.log(ChatLogger.Level.DEBUG, "getCustomEmojis", "Message from the server: $result")
        return result
    }

    suspend fun getSpotlight(query: String): RestResponseSpotlight {
        val queryParameters = restClient.generateQueryString(PARAMETER_QUERY to query)
        val result = restClient.createGetRequest<RestResponseSpotlight>(FUNCTION_GET_SPOTLIGHT, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "getSpotlight", "Message from the server: $result")
        return result
    }
}
