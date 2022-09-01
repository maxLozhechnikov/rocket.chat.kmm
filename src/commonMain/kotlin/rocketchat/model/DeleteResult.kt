package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteResult(
    @SerialName("_id")
    val id: String,
    @SerialName("ts")
    val timestamp: Long?,
    val success: Boolean = false
)