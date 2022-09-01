package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class RestResponseSettings(
    val settings: List<JsonObject>? = null,
    val count: Int,
    val offset: Int,
    val total: Int,
    val success: Boolean
)