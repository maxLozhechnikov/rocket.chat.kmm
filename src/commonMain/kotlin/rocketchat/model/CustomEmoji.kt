package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CustomEmoji(
    @SerialName("_id")
    val id: String,
    val name: String? = null,
    val aliases: List<String> = emptyList(),
    val extension: String? = null,
    @SerialName("_updatedAt")
    @Serializable(with= TimeLongSerializer::class)
    val updatedAt: Long? = null
)