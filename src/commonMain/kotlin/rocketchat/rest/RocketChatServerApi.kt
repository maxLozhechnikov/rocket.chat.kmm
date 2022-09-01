package com.omegar.libs.rocketchat.rest

import com.omegar.libs.rocketchat.ChatLogger
import com.omegar.libs.rocketchat.model.RestResponseConfigurations
import com.omegar.libs.rocketchat.model.RestResponseServerInfo
import com.omegar.libs.rocketchat.model.RestResponseSettings
import com.omegar.libs.rocketchat.model.RestResponseSettingsOauth
import io.ktor.http.HttpMethod

class RocketChatServerApi(private val restClient: RestClient, private val logger: ChatLogger) {
    companion object {
        private const val FUNCTION_SERVER_INFO = "api/info"
        private const val FUNCTION_SERVICE_CONFIGURATIONS = "service.configurations"
        private const val FUNCTION_SETTINGS = "settings.public"
        private const val FUNCTION_SETTINGS_OAUTH = "settings.oauth"
    }

    suspend fun getServerInfo(): RestResponseServerInfo {
        val result = restClient.createRequest<RestResponseServerInfo>(
            functionName =  FUNCTION_SERVER_INFO,
            requestType = HttpMethod.Get,
            withAuth = false,
            shortPath = true
        )
        logger.log(ChatLogger.Level.DEBUG, "getServerInfo", "Message from the server: $result")
        return result
    }

    suspend fun getConfigurations(): RestResponseConfigurations {
        val result = restClient.createGetRequest<RestResponseConfigurations>(
            functionName =  FUNCTION_SERVICE_CONFIGURATIONS,
            withAuth = false
        )
        logger.log(ChatLogger.Level.DEBUG, "getConfigurations", "Message from the server: $result")
        return result
    }

    suspend fun getSettings(): RestResponseSettings {
        val result = restClient.createGetRequest<RestResponseSettings>(
            functionName =  FUNCTION_SETTINGS,
            withAuth = false
        )
        logger.log(ChatLogger.Level.DEBUG, "getSettings", "Message from the server: $result")
        return result
    }

    suspend fun getSettingsOauth(): RestResponseSettingsOauth {
        val result = restClient.createGetRequest<RestResponseSettingsOauth>(
            functionName =  FUNCTION_SETTINGS_OAUTH,
            withAuth = false
        )
        logger.log(ChatLogger.Level.DEBUG, "getSettingsOauth", "Message from the server: $result")
        return result
    }
}