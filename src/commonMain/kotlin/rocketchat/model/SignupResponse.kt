package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class SignupResponse(
    val success: Boolean = false,
    val user: User? = null
)