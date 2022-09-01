package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestResponseGroups(
    @SerialName("groups")
    val groupRooms: List<Room>? = null,
    val count: Long,
    val offset: Long,
    val total: Long,
    val success: Boolean
)