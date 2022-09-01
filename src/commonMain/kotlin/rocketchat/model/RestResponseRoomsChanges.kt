package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestResponseRoomsChanges(
    @SerialName("update")
    val updateList: List<Room>? = null,
    @SerialName("remove")
    val removeList: List<Removed>? = null,
    val success: Boolean
)