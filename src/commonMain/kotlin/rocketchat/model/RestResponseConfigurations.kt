package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class RestResponseConfigurations(
    val configurations: List<JsonObject>? = null,
    val success: Boolean
)

