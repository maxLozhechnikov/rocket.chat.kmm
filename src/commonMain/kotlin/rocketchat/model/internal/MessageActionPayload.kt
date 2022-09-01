package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.*

@Serializable
data class MessageActionPayload(
    val messageId: String
)