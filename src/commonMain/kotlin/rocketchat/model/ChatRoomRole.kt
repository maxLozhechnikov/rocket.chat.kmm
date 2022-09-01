package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomRole(
    @SerialName("_id")
    val id: String,
    @SerialName("rid")
    val roomId: String,
    @SerialName("u")
    val user: SimpleUser,
    val roles: List<String>
)

@Serializable
data class RestResponseChatRoomRoles(
    val roles: List<ChatRoomRole>? = null,
    val success: Boolean
)