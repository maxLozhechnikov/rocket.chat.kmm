package com.omegar.libs.rocketchat.model.internal

import com.omegar.libs.rocketchat.model.Attachment
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class SendMessageBody(
    @SerialName("_id")
    val id: String? = null,
    @SerialName("rid")
    val roomId: String?,
    @SerialName("msg")
    val text: String?,
    val alias: String?,
    val emoji: String?,
    val avatar: String?,
    val attachments: List<Attachment>?,
    @SerialName("msgId")
    val messageId: String? = null
)

@Serializable
internal data class SendMessagePayload(
    val message: SendMessageBody?
)