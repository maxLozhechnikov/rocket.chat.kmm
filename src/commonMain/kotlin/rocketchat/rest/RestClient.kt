package com.omegar.libs.rocketchat.rest

import com.omegar.libs.rocketchat.ChatLogger
import com.omegar.libs.rocketchat.model.LoginResponseData
import com.omegar.libs.rocketchat.model.RocketChatException
import com.omegar.libs.rocketchat.model.RoomType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.json.*
import io.ktor.client.plugins.kotlinx.serializer.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.utils.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

class RestClient(val apiUrl: String, val logger: ChatLogger, val useHttpClientLogger: Boolean = false) {
    companion object {
        private const val HEADER_AUTH_TOKEN = "X-Auth-Token"
        private const val HEADER_USER_ID = "X-User-Id"
        private const val SEPARATOR_KEY_VALUE = "="
        private const val SEPARATOR_QUERY = "&"
        private const val PREFIX_QUERY = "?"
        private const val API_PATH_REST = "api/v1/"
        private const val API_PATH_SOCKET = "/websocket"
        private const val API_PATH_PREFIX = "https://"
        private const val API_PATH_SEPARATOR = "/"
        private const val API_SOCKET_PORT = 443
    }

    private var authData: LoginResponseData? = null

    private val apiFullUrl: String
        get() = apiShort + API_PATH_REST

    internal val apiShort: String
        get() = API_PATH_PREFIX + apiUrl + API_PATH_SEPARATOR

    internal val currentUserId: String
        get() = authData?.userId ?: ""

    internal val currentAuthToken: String
        get() = authData?.authToken ?: ""

    private val client = HttpClient {
        install(JsonPlugin) {
            serializer = KotlinxSerializer(
                Json as Json
            )
        }

        install(WebSockets)

        if (useHttpClientLogger) {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }
        }
    }

    internal suspend fun createWebSocket(block: suspend DefaultClientWebSocketSession.() -> Unit) {
        return client.wss(
            method = HttpMethod.Get,
            host = apiUrl,
            port = API_SOCKET_PORT,
            path = API_PATH_SOCKET
        ) {block()}
    }

    internal suspend inline fun <reified T> createRequest(
        functionName: String,
        data: Any = EmptyContent,
        queryParameters: String = "",
        requestType: HttpMethod,
        withAuth: Boolean = true,
        isJson: Boolean = true,
        shortPath: Boolean = false
    ): T {
        return client.request {
            url((if (shortPath) apiShort else apiFullUrl) + functionName + queryParameters)
            method = requestType
            if (isJson) {
                contentType(ContentType.Application.Json)
            }
            if (withAuth && authData != null) {
                header(HEADER_AUTH_TOKEN, authData?.authToken)
                header(HEADER_USER_ID, authData?.userId)
            }
        }.body()
    }

    internal suspend inline fun <reified T> createPostRequest(
        functionName: String,
        data: Any = EmptyContent,
        withAuth: Boolean = true,
        isJson: Boolean = true
    ): T {
        return createRequest(
            functionName = functionName,
            data =  data,
            requestType =  HttpMethod.Post,
            withAuth =  withAuth,
            isJson =  isJson
        )
    }

    internal suspend inline fun <reified T> createGetRequest(
        functionName: String,
        queryParameters: String = "",
        withAuth: Boolean = true,
        isJson: Boolean = true
    ): T {
        return createRequest(
            functionName = functionName,
            queryParameters =  queryParameters,
            requestType =  HttpMethod.Get,
            withAuth =  withAuth,
            isJson =  isJson
        )
    }

    fun generateQueryString(vararg params: Pair<String, Any>): String {
        val args = arrayOf(*params)
        val queries = args.joinToString(separator = SEPARATOR_QUERY) { it.first + SEPARATOR_KEY_VALUE + it.second }
        return PREFIX_QUERY + queries
    }

    internal fun setAuthData(aData: LoginResponseData) {
        authData = aData
        logger.log(ChatLogger.Level.DEBUG, "RestClient", "New authData: $authData")
    }

    internal fun getRestApiMethodNameByRoomType(roomType: RoomType, method: String): String {
        return when (roomType) {
            RoomType.CHANNEL -> "channels.$method"
            RoomType.PRIVATE_GROUP -> "groups.$method"
            RoomType.DIRECT_MESSAGE -> "im.$method"
            else -> throw RocketChatException("Invalid room type $roomType")
        }
    }
}