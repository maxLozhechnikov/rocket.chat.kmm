package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeletePayload(
    val roomId: String,
    @SerialName("msgId")
    val messageId: String,
    val asUser: Boolean = false
)