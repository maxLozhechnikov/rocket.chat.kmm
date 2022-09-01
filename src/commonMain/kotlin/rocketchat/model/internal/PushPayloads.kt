package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PushRegistrationPayload(
    val type: PushType,
    val value: String,
    val appName: String
)

@Serializable
data class PushUnregistrationPayload(val token: String)

@Serializable
enum class PushType {
    @SerialName("apn")
    APN,
    @SerialName("gcm")
    GCM
}