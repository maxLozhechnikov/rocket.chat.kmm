package com.omegar.libs.rocketchat.rest
import com.omegar.libs.rocketchat.ChatLogger
import com.omegar.libs.rocketchat.model.*
import com.omegar.libs.rocketchat.model.internal.*
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.util.InternalAPI

class RocketChatUserApi(private val restClient: RestClient, private val logger: ChatLogger) {
    companion object {
        private const val FUNCTION_ME = "me"
        private const val FUNCTION_UPDATE_PROFILE = "users.update"
        private const val FUNCTION_USERS = "users.list"
        private const val FUNCTION_GET_USER = "users.info"
        private const val FUNCTION_UPDATE_OWN_INFO = "users.updateOwnBasicInfo"
        private const val FUNCTION_RESET_AVATAR = "users.resetAvatar"
        private const val FUNCTION_SET_AVATAR = "users.setAvatar"
        private const val FUNCTION_GET_AVATAR = "users.getAvatar"
        private const val FUNCTION_GET_SUBSCRIPTION = "subscriptions.getOne"
        private const val FUNCTION_GET_SUBSCRIPTIONS = "subscriptions.get"
        private const val FUNCTION_GET_ROOMS = "rooms.get"
        private const val FUNCTION_GET_GROUPS = "groups.listAll"
        private const val FUNCTION_GET_IMS = "im.list"
        private const val PARAMETER_SET_AVATAR = "image"
        private const val PARAMETER_OFFSET = "offset"
        private const val PARAMETER_COUNT = "count"
        private const val PARAMETER_USER_ID = "userId"
        private const val PARAMETER_USERNAME = "username"
        private const val PARAMETER_FILENAME = "filename="
        private val AVATAR_MIME_TYPES = listOf("image/gif", "image/png", "image/jpeg", "image/jpeg", "image/bmp", "image/webp")
        private const val AVATAR_URL = "avatar/"
        private const val PARAMETER_ROOM_ID = "roomId"
        private const val PARAMETER_UPDATED_SINCE = "updatedSince"
    }

    suspend fun me(): Boolean {
        val result = restClient.createGetRequest<Myself>(FUNCTION_ME)
        logger.log(ChatLogger.Level.DEBUG, "ME", "Message from the server: $result")
        return result.success
    }

    suspend fun updateProfile(
        userId: String,
        email: String? = null,
        name: String? = null,
        password: String? = null,
        username: String? = null
    ): RestResponseUser {
        val payload = UserPayload(
            userId,
            UserPayloadData(
                name,
                password,
                username,
                email
            )
        )
        val result = restClient.createPostRequest<RestResponseUser>(FUNCTION_UPDATE_PROFILE, payload)
        logger.log(ChatLogger.Level.DEBUG, "updateProfile", "Message from the server: $result")
        return result
    }

    suspend fun users(offset: Long, count: Long): RestResponseUsers {
        val queryParameters = restClient.generateQueryString(PARAMETER_OFFSET to offset, PARAMETER_COUNT to count)
        val result = restClient.createGetRequest<RestResponseUsers>(FUNCTION_USERS, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "users", "Message from the server: $result")
        return result
    }

    suspend fun getProfileByUserId(userId: String): RestResponseUser {
        val queryParameters = restClient.generateQueryString(PARAMETER_USER_ID to userId)
        val result = restClient.createGetRequest<RestResponseUser>(FUNCTION_GET_USER, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "getProfileByUserId", "Message from the server: $result")
        return result
    }

    suspend fun getProfileByUsername(username: String): RestResponseUser {
        val queryParameters = restClient.generateQueryString(PARAMETER_USERNAME to username)
        val result = restClient.createGetRequest<RestResponseUser>(FUNCTION_GET_USER, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "getProfileByUsername", "Message from the server: $result")
        return result
    }

    suspend fun updateOwnBasicInfo(
        email: String? = null,
        currentPassword: String? = null,
        newPassword: String? = null,
        username: String? = null,
        name: String? = null
    ): RestResponseUser {
        val payload = OwnBasicInformationPayload(
            OwnBasicInformationPayloadData(
                email,
                currentPassword,
                newPassword,
                username,
                name
            )
        )
        val result = restClient.createPostRequest<RestResponseUser>(FUNCTION_UPDATE_OWN_INFO, payload)
        logger.log(ChatLogger.Level.DEBUG, "updateOwnBasicInfo", "Message from the server: $result")
        return result
    }

    suspend fun resetAvatar(userId: String): DefaultResult {
        var payload = UserPayload(userId, null)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_RESET_AVATAR, payload)
        logger.log(ChatLogger.Level.DEBUG, "resetAvatar", "Message from the server: $result")
        return result
    }

    suspend fun setAvatar(fileName: String, mimeType: String, fileData: ByteArray): Boolean {
        if (mimeType !in AVATAR_MIME_TYPES) {
            throw RocketChatException("Invalid image type $mimeType")
        }

        val body = MultiPartFormDataContent(
            formData {
                append(
                    PARAMETER_SET_AVATAR,
                    fileData,
                    Headers.build {
                        append(HttpHeaders.ContentType, mimeType)
                        append(HttpHeaders.ContentDisposition, PARAMETER_FILENAME + fileName)
                    }
                )
            }
        )

        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_SET_AVATAR, body, isJson = false)
        logger.log(ChatLogger.Level.DEBUG, "setAvatar", "Message from the server: $result")
        return result.success
    }

    suspend fun setAvatar(avatarUrl: String): Boolean {
        val payload = UserPayload(null, null, avatarUrl)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_SET_AVATAR, payload)
        logger.log(ChatLogger.Level.DEBUG, "setAvatar", "Message from the server: $result")
        return result.success
    }

    suspend fun getAvatar(username: String): String {
        return restClient.apiShort + AVATAR_URL + username
    }

    suspend fun getSubscription(roomId: String): RestResponseSubscription {
        val queryParameters = restClient.generateQueryString(PARAMETER_ROOM_ID to roomId)
        val result = restClient.createGetRequest<RestResponseSubscription>(FUNCTION_GET_SUBSCRIPTION, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "getSubscription", "Message from the server: $result")
        return result
    }

    suspend fun getSubscriptions(updatedSince: String? = null): RestResponseSubscriptionsChanges {
        val queryParameters = updatedSince?.let {restClient.generateQueryString(PARAMETER_UPDATED_SINCE to it)} ?: ""
        val result = restClient.createGetRequest<RestResponseSubscriptionsChanges>(FUNCTION_GET_SUBSCRIPTIONS, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "getSubscriptions", "Message from the server: $result")
        return result
    }

    suspend fun listRooms(updatedSince: String? = null): RestResponseRoomsChanges {
        val queryParameters = updatedSince?.let {restClient.generateQueryString(PARAMETER_UPDATED_SINCE to it)} ?: ""
        val result = restClient.createGetRequest<RestResponseRoomsChanges>(FUNCTION_GET_ROOMS, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "listRooms", "Message from the server: $result")
        return result
    }

    suspend fun listGroups(offset: Long = 0, count: Long = 0): RestResponseGroups {
        val queryParameters = restClient.generateQueryString(PARAMETER_OFFSET to offset, PARAMETER_COUNT to count)
        val result = restClient.createGetRequest<RestResponseGroups>(FUNCTION_GET_GROUPS, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "listGroups", "Message from the server: $result")
        return result
    }

    suspend fun getDirectMessages(offset: Long = 0, count: Long = 0): RestResponseDirectMessages {
        val queryParameters = restClient.generateQueryString(PARAMETER_OFFSET to offset, PARAMETER_COUNT to count)
        val result = restClient.createGetRequest<RestResponseDirectMessages>(FUNCTION_GET_IMS, queryParameters)
        logger.log(ChatLogger.Level.DEBUG, "listIms", "Message from the server: $result")
        return result
    }
}