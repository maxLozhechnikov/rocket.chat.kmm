package com.omegar.libs.rocketchat.model

import com.omegar.libs.rocketchat.model.Room
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestResponseDirectMessages(
    @SerialName("directMessages")
    val directMessageRooms: List<Room>? = null,
    val count: Long,
    val offset: Long,
    val total: Long,
    val success: Boolean
)