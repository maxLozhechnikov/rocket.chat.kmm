package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestResponseMentions(
    @SerialName("mentions")
    val messages: List<Message>? = null,
    val count: Long? = null,
    val offset: Long? = null,
    val total: Long? = null,
    val success: Boolean
)