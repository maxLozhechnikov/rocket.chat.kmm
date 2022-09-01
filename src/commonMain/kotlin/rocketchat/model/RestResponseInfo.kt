package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestResponseInfo(
    @SerialName("group")
    val groupRoom: Room? = null,
    @SerialName("channel")
    val channelRoom: Room? = null,
    val success: Boolean
)