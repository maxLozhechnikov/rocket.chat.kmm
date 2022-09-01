package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.Serializable

@Serializable
data class SignUpPayload(
    val username: String?,
    val email: String?,
    val pass: String?,
    val name: String?
)