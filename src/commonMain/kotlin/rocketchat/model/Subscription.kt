package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Subscription(
    @SerialName("rid")
    val roomId: String,
    @SerialName("_id")
    val id: String,
    @SerialName("t")
    val type: RoomType,
    @SerialName("u")
    val user: SimpleUser?,
    val name: String?,
    @SerialName("fname")
    val fullName: String? = null,
    @SerialName("ro")
    val readonly: Boolean = false,
    @SerialName("ts")
    @Serializable(with= TimeLongSerializer::class)
    val timestamp: Long?,
    @SerialName("ls")
    @Serializable(with= TimeLongSerializer::class)
    val lastSeen: Long? = null,
    @SerialName("_updatedAt")
    @Serializable(with= TimeLongSerializer::class)
    val updatedAt: Long?,
    val roles: List<String>? = null,
    @SerialName("default")
    val isDefault: Boolean = false,
    @SerialName("f")
    val isFavorite: Boolean = false,
    val open: Boolean = false,
    val alert: Boolean = false,
    val archived: Boolean = false,
    val unread: Long = 0,
    val userMentions: Long = 0,
    val groupMentions: Long = 0
)