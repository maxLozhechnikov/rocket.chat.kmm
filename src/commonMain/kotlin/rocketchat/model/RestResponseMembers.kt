package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class RestResponseMembers(
    val members: List<User>? = null,
    val count: Long,
    val offset: Long,
    val total: Long,
    val success: Boolean
)