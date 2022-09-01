package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.Serializable

@Serializable
data class ReactionPayload(
    val messageId: String,
    val emoji: String
)