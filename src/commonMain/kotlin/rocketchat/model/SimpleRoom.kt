package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleRoom(
    @SerialName("_id")
    val id: String,
    val name: String?
)