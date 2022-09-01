package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Permission(
    @SerialName("_id")
    val id: String,
    val roles: List<String>
)