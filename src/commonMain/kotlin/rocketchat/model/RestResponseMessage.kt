package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class RestResponseMessage(
    val message: Message? = null,
    val success: Boolean
)