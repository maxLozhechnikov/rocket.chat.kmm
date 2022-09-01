package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.Serializable

@Serializable
data class UsernameLoginPayload(
    val username: String? = null,
    val password: String? = null,
    val code: String? = null
)