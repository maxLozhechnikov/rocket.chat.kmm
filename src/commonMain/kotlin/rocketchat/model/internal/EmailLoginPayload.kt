package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.Serializable

@Serializable
data class EmailLoginPayload(
    val user: String,
    val password: String,
    val code: String? = null
)