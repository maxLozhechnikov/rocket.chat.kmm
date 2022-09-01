package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class RestResponseCommands(
    val commands: List<Command>? = null,
    val count: Long? = null,
    val offset: Long? = null,
    val total: Long? = null,
    val success: Boolean
)