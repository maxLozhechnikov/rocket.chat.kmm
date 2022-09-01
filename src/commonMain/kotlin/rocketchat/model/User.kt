package com.omegar.libs.rocketchat.model

import kotlinx.serialization.*

@Serializable
data class User(
    @SerialName("_id")
    val id: String,
    val username: String? = null,
    val name: String? = null,
    val status: UserStatus? = null,
    val utcOffset: Float? = null,
    val emails: List<Email>? = null,
    val roles: List<String>? = null
)

@Serializable
enum class UserStatus {
    @SerialName("online")
    ONLINE,
    @SerialName("busy")
    BUSY,
    @SerialName("away")
    AWAY,
    @SerialName("offline")
    OFFLINE
}