package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.Serializable

@Serializable
data class UserPayload(
    val userId: String? = null,
    val data: UserPayloadData?,
    val avatarUrl: String? = null
)

@Serializable
data class UserPayloadData(
    val name: String? = null,
    val password: String? = null,
    val username: String? = null,
    val email: String? = null
)
