package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class RestResponseSpotlight(
    val users: List<User>? = null,
    val rooms: List<Room>? = null,
    val success: Boolean = false
)