package com.omegar.libs.rocketchat

import kotlinx.serialization.*
import kotlinx.serialization.json.Json

object DefaultSerializer {
    val json = Json.Default

    inline fun <reified T : Any> toJson(data: T): String {
        return json.encodeToString(data)
    }

    inline fun <reified T : Any> fromJson(jsonString: String): T {
        return json.decodeFromString(jsonString)
    }
}