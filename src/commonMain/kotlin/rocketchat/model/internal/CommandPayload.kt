package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.*

@Serializable
data class CommandPayload(
    val command: String,
    val roomId: String? = null,
    val params: String? = null
)