package com.omegar.libs.rocketchat.model.internal

import com.omegar.libs.rocketchat.model.Attachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostMessagePayload(
    val roomId: String?,
    val text: String?,
    val alias: String?,
    val emoji: String?,
    val avatar: String?,
    val attachments: List<Attachment>?,
    @SerialName("msgId")
    val messageId: String? = null
)