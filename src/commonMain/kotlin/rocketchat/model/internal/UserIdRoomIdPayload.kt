package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.*

@Serializable
data class UserIdRoomIdPayload(
    val roomId: String,
    val userId: String
)