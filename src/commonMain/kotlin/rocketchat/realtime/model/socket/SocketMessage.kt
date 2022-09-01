package com.omegar.libs.rocketchat.realtime.model.socket

import kotlinx.serialization.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class SocketMessage(
    @SerialName("msg")
    val type: SocketMessageCollectionType? = null,
    val id: String? = null,
    val name: String? = null,
    val method: String? = null,
    val version: String? = null,
    @SerialName("collection")
    val collectionType: SocketMessageCollection? = null,
    @SerialName("reason")
    val errorReason: String? = null,
    val support: List<String>? = null,
    val params: List<ParamValue>? = null,
    val fields: JsonObject? = null,
    @SerialName("subs")
    val subscriptions: List<String>? = null
) {
    companion object {
        private const val KEY_EVENT_NAME ="eventName"
        private const val KEY_ARRAY ="args"
    }

    fun getEventName(): String? {
        return fields?.get(KEY_EVENT_NAME)?.jsonPrimitive?.content
    }

    fun getResponseArray(): JsonArray? {
        return fields?.get(KEY_ARRAY)?.jsonArray
    }
}