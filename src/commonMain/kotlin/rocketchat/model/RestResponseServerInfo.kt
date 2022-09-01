package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class RestResponseServerInfo(
    val version: String? = null,
    val success: Boolean
)