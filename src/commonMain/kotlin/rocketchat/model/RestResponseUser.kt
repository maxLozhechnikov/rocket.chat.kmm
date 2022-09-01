package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class RestResponseUser(
    val user: User? = null,
    val success: Boolean
)