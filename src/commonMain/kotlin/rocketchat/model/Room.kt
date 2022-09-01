package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Room(
    @SerialName("_id")
    val id: String,
    @SerialName("t")
    val type: RoomType,
    @SerialName("u")
    val user: SimpleUser? = null,
    val name: String? = null,
    @SerialName("fname")
    val fullName: String? = null,
    @SerialName("ro")
    val readonly: Boolean = false,
    @SerialName("_updatedAt")
    @Serializable(with= TimeLongSerializer::class)
    val updatedAt: Long? = null,
    val topic: String? = null,
    val description: String? = null,
    val announcement: String? = null,
    val lastMessage: Message? = null,
    @SerialName("muted")
    val mutedUsers: List<String>? = null,
    val broadcast: Boolean = false
)

@Serializable
enum class RoomType {
    @SerialName("c")
    CHANNEL,
    @SerialName("p")
    PRIVATE_GROUP,
    @SerialName("d")
    DIRECT_MESSAGE,
    @SerialName("l")
    LIVECHAT
}