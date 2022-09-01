package com.omegar.libs.rocketchat.rest

import com.omegar.libs.rocketchat.ChatLogger
import com.omegar.libs.rocketchat.model.*
import com.omegar.libs.rocketchat.model.internal.*
import com.omegar.libs.rocketchat.model.internal.SendMessageBody
import com.omegar.libs.rocketchat.model.internal.SendMessagePayload
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class RocketChatMessageApi(private val restClient: RestClient, private val logger: ChatLogger) {
    companion object {
        private const val FUNCTION_SEND_MESSAGE = "chat.sendMessage"
        private const val FUNCTION_POST_MESSAGE = "chat.postMessage"
        private const val FUNCTION_UPDATE_MESSAGE = "chat.update"
        private const val FUNCTION_DELETE_MESSAGE = "chat.delete"
        private const val FUNCTION_STAR_MESSAGE = "chat.starMessage"
        private const val FUNCTION_UNSTAR_MESSAGE = "chat.unStarMessage"
        private const val FUNCTION_PIN_MESSAGE = "chat.pinMessage"
        private const val FUNCTION_UNPIN_MESSAGE = "chat.unPinMessage"
        private const val FUNCTION_TOGGLE_REACTION = "chat.react"
        private const val FUNCTION_UPLOAD = "rooms.upload"
        private const val FUNCTION_GET_MESSAGES = "messages"
        private const val FUNCTION_GET_MESSAGE = "chat.getMessage"
        private const val FUNCTION_GET_HISTORY = "history"
        private const val FUNCTION_GET_RECEIPTS = "chat.getMessageReadReceipts"

        private const val ROOM_NAME_SEPARATOR = "/"
        private const val PARAMETER_FILENAME = "filename="
        private const val PARAMETER_UPLOAD_FILE = "file"
        private const val PARAMETER_MESSAGE = "msg"
        private const val PARAMETER_DESCRIPTION = "description"
        private const val PARAMETER_ROOM_ID = "roomId"
        private const val PARAMETER_OFFSET = "offset"
        private const val PARAMETER_COUNT = "count"
        private const val PARAMETER_MESSAGE_ID = "msgId"
        private const val PARAMETER_MESSAGE_ID2 = "messageId"
        private const val PARAMETER_OLDEST = "oldest"
        private const val PARAMETER_LATEST = "latest"
    }

    suspend fun sendMessage(message: Message): RestResponseMessage {
        val payload = SendMessagePayload(
            SendMessageBody(
                message.id,
                message.roomId,
                message.text,
                message.senderAlias,
                message.emoji,
                message.avatar,
                message.attachments
            )
        )
        val result = restClient.createPostRequest<RestResponseMessage>(FUNCTION_SEND_MESSAGE, payload)
        logger.log(ChatLogger.Level.DEBUG, "sendMessage", "Message from the server: $result")
        return result
    }

    suspend fun postMessage(message: Message): RestResponseMessage {
        val payload = PostMessagePayload(
            message.roomId,
            message.text,
            message.senderAlias,
            message.emoji,
            message.avatar,
            message.attachments
        )
        val result = restClient.createPostRequest<RestResponseMessage>(FUNCTION_POST_MESSAGE, payload)
        logger.log(ChatLogger.Level.DEBUG, "postMessage", "Message from the server: $result")
        return result
    }

    suspend fun updateMessage(message: Message): RestResponseMessage {
        val payload = PostMessagePayload(message.roomId, message.text, null, null, null, null, message.id)
        val result = restClient.createPostRequest<RestResponseMessage>(FUNCTION_UPDATE_MESSAGE, payload)
        logger.log(ChatLogger.Level.DEBUG, "updateMessage", "Message from the server: $result")
        return result
    }

    suspend fun deleteMessage(roomId: String, messageId: String, asUser: Boolean = false): DeleteResult {
        val payload = DeletePayload(roomId, messageId, asUser)
        val result = restClient.createPostRequest<DeleteResult>(FUNCTION_DELETE_MESSAGE, payload)
        logger.log(ChatLogger.Level.DEBUG, "deleteMessage", "Message from the server: $result")
        return result
    }

    suspend fun starMessage(messageId: String): DefaultResult {
        val payload = MessageActionPayload(messageId)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_STAR_MESSAGE, payload)
        logger.log(ChatLogger.Level.DEBUG, "starMessage", "Message from the server: $result")
        return result
    }

    suspend fun unStarMessage(messageId: String): DefaultResult {
        val payload = MessageActionPayload(messageId)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_UNSTAR_MESSAGE, payload)
        logger.log(ChatLogger.Level.DEBUG, "unStarMessage", "Message from the server: $result")
        return result
    }

    suspend fun pinMessage(messageId: String): DefaultResult {
        val payload = MessageActionPayload(messageId)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_PIN_MESSAGE, payload)
        logger.log(ChatLogger.Level.DEBUG, "pinMessage", "Message from the server: $result")
        return result
    }

    suspend fun unPinMessage(messageId: String): DefaultResult {
        val payload = MessageActionPayload(messageId)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_UNPIN_MESSAGE, payload)
        logger.log(ChatLogger.Level.DEBUG, "unPinMessage", "Message from the server: $result")
        return result
    }

    suspend fun toggleReaction(messageId: String, emoji: String): DefaultResult {
        val payload = ReactionPayload(messageId, emoji)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_TOGGLE_REACTION, payload)
        logger.log(ChatLogger.Level.DEBUG, "toggleReaction", "Message from the server: $result")
        return result
    }

    suspend fun uploadFile(file: UploadFile): Boolean {
        val body = MultiPartFormDataContent(
            formData {
                append(
                    PARAMETER_UPLOAD_FILE,
                    file.fileData,
                    Headers.build {
                        append(HttpHeaders.ContentType, file.mimeType)
                        append(HttpHeaders.ContentDisposition, PARAMETER_FILENAME + file.fileName)
                    }
                )
                append(PARAMETER_MESSAGE, file.message)
                append(PARAMETER_DESCRIPTION, file.description)
            }
        )

        val result = restClient.createPostRequest<DefaultResult>(
            functionName = FUNCTION_UPLOAD + ROOM_NAME_SEPARATOR + file.roomId,
            data = body,
            isJson = false
        )
        logger.log(ChatLogger.Level.DEBUG, "uploadFile", "Message from the server: $result")
        return result.success
    }

    suspend fun getMessages(
        roomId: String,
        roomType: RoomType,
        offset: Long,
        count: Long
    ): RestResponseMessages {
        val queryParameters = restClient.generateQueryString(
            PARAMETER_ROOM_ID to roomId,
            PARAMETER_OFFSET to offset,
            PARAMETER_COUNT to count
        )
        val result = restClient.createGetRequest<RestResponseMessages>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_GET_MESSAGES),
            queryParameters
        )
        logger.log(ChatLogger.Level.DEBUG, "getMessages", "Message from the server: $result")
        return result
    }

    suspend fun getMessage(messageId: String): RestResponseMessage {
        val queryParameters = restClient.generateQueryString(PARAMETER_MESSAGE_ID to messageId)
        val result = restClient.createGetRequest<RestResponseMessage>(FUNCTION_GET_MESSAGE, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "getMessage", "Message from the server: $result")
        return result
    }

    suspend fun getHistory(
        roomId: String,
        roomType: RoomType,
        count: Long = 50,
        oldest: String? = null,
        latest: String? = null
    ): RestResponseMessages {
        val queryList: MutableList<Pair<String, Any>> = mutableListOf(
            PARAMETER_ROOM_ID to roomId,
            PARAMETER_COUNT to count
        )
        oldest?.let {
            queryList.add(PARAMETER_OLDEST to it)
        }
        latest?.let {
            queryList.add(PARAMETER_LATEST to it)
        }
        val queryParameters = restClient.generateQueryString(*queryList.toTypedArray())
        val result = restClient.createGetRequest<RestResponseMessages>(
            restClient.getRestApiMethodNameByRoomType(roomType, FUNCTION_GET_HISTORY),
            queryParameters
        )
        logger.log(ChatLogger.Level.DEBUG, "getHistory", "Message from the server: $result")
        return result
    }

    suspend fun getMessageReadReceipts(
        messageId: String,
        count: Long = 50,
        oldest: String? = null,
        latest: String? = null
    ): RestResponseReadReceipts {
        val queryList: MutableList<Pair<String, Any>> = mutableListOf(
            PARAMETER_MESSAGE_ID2 to messageId,
            PARAMETER_COUNT to count
        )
        oldest?.let {
            queryList.add(PARAMETER_OLDEST to it)
        }
        latest?.let {
            queryList.add(PARAMETER_LATEST to it)
        }
        val queryParameters = restClient.generateQueryString(*queryList.toTypedArray())
        val result = restClient.createGetRequest<RestResponseReadReceipts>(FUNCTION_GET_RECEIPTS, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "getMessageReadReceipts", "Message from the server: $result")
        return result
    }
}