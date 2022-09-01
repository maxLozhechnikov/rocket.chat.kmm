package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    val title: String? = null,
    val description: String? = null,
    val text: String? = null,
    val imageUrl: String? = null,
    val raw: Map<String, String>? = null
)