package com.omegar.libs.rocketchat.rest

import com.omegar.libs.rocketchat.ChatLogger
import com.omegar.libs.rocketchat.model.LoginResponse
import com.omegar.libs.rocketchat.model.LogoutResponse
import com.omegar.libs.rocketchat.model.SignupResponse
import com.omegar.libs.rocketchat.model.internal.*

class RocketChatLoginApi(private val restClient: RestClient, private val logger: ChatLogger) {
    companion object {
        private const val FUNCTION_LOGIN = "login"
        private const val FUNCTION_LOGOUT = "logout"
        private const val FUNCTION_REGISTER = "users.register"
        private const val FUNCTION_FORGOT_PASSWORD = "users.forgotPassword"
        private const val STATUS_SUCCESS = "success"
    }

    suspend fun login(username: String, password: String, pin: String? = null): Boolean {
        val payload = UsernameLoginPayload(
            username = username,
            password = password,
            code = pin
        )
        val result = restClient.createPostRequest<LoginResponse>(FUNCTION_LOGIN, payload,false)
        logger.log(ChatLogger.Level.DEBUG, "login", "Message from the server: $result")
        if (result.status == STATUS_SUCCESS){
            result.data?.let { restClient.setAuthData(it); return true}
        }
        return false
    }

    suspend fun loginEmail(email: String, password: String, pin: String? = null): Boolean {
        val payload = EmailLoginPayload(
            user = email,
            password = password,
            code = pin
        )
        val result = restClient.createPostRequest<LoginResponse>(FUNCTION_LOGIN, payload, false)
        logger.log(ChatLogger.Level.DEBUG, "loginEmail", "Message from the server: $result")
        if (result.status == STATUS_SUCCESS){
            result.data?.let { restClient.setAuthData(it); return true}
        }
        return false
    }

    suspend fun loginLdap(username: String, password: String): Boolean {
        val payload = LdapLoginPayload(
            ldap = true,
            username = username,
            ldapPass = password
        )
        val result = restClient.createPostRequest<LoginResponse>(FUNCTION_LOGIN, payload, false)
        logger.log(ChatLogger.Level.DEBUG, "loginLdap", "Message from the server: $result")
        if (result.status == STATUS_SUCCESS){
            result.data?.let { restClient.setAuthData(it); return true}
        }
        return false
    }

    suspend fun loginCas(casCredential: String): Boolean {
        val payload = CasLoginPayload(
            CasData(casCredential)
        )
        val result = restClient.createPostRequest<LoginResponse>(FUNCTION_LOGIN, payload, false)
        logger.log(ChatLogger.Level.DEBUG, "loginCas", "Message from the server: $result")
        if (result.status == STATUS_SUCCESS){
            result.data?.let { restClient.setAuthData(it); return true}
        }
        return false
    }

    suspend fun loginSaml(samlCredential: String): Boolean {
        val payload = SamlLoginPayload(true, samlCredential)
        val result = restClient.createPostRequest<LoginResponse>(FUNCTION_LOGIN, payload, false)
        logger.log(ChatLogger.Level.DEBUG, "loginSaml", "Message from the server: $result")
        if (result.status == STATUS_SUCCESS){
            result.data?.let { restClient.setAuthData(it); return true}
        }
        return false
    }

    suspend fun loginOauth(credentialToken: String, credentialSecret: String): Boolean {
        val payload = OauthLoginPayload(
            OauthData(
                credentialToken,
                credentialSecret
            )
        )
        val result = restClient.createPostRequest<LoginResponse>(FUNCTION_LOGIN, payload, false)
        logger.log(ChatLogger.Level.DEBUG, "loginOauth", "Message from the server: $result")
        if (result.status == STATUS_SUCCESS){
            result.data?.let { restClient.setAuthData(it); return true}
        }
        return false
    }

    suspend fun signup(email: String, name: String, username: String, password: String): SignupResponse {
        val payload = SignUpPayload(username, email, password, name)
        val result = restClient.createPostRequest<SignupResponse>(FUNCTION_REGISTER, payload, false)
        logger.log(ChatLogger.Level.DEBUG, "signup", "Message from the server: $result")
        return result
    }

    suspend fun forgotPassword(email: String): Boolean {
        val payload = ForgotPasswordPayload(email)
        val result = restClient.createPostRequest<DefaultResult>(FUNCTION_FORGOT_PASSWORD, payload, false)
        logger.log(ChatLogger.Level.DEBUG, "forgotPassword", "Message from the server: $result")
        return result.success
    }

    suspend fun logout(): Boolean {
        val result = restClient.createPostRequest<LogoutResponse>(FUNCTION_LOGOUT)
        logger.log(ChatLogger.Level.DEBUG, "logout", "Message from the server: $result")
        return result.status == STATUS_SUCCESS
    }
}