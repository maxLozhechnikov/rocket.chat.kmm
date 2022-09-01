package com.omegar.libs.rocketchat.model.internal
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordPayload(val email: String?)