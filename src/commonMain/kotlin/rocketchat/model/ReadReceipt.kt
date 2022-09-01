package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReadReceipt(
    @SerialName("_id")
    val id: String?,
    val roomId: String,
    val userId: String,
    val messageId: String,
    @SerialName("ts")
    val timestamp: String,
    val user: SimpleUser
)