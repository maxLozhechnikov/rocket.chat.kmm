package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SimpleUser(
    @SerialName("_id")
    val id: String? = null,
    val username: String? = null,
    val name: String? = null
)