package com.omegar.libs.rocketchat.model

import kotlinx.serialization.*

@Serializable
data class Command(
    val command: String,
    val params: String? = null,
    val description: String? = null,
    val clientOnly: Boolean = false
)