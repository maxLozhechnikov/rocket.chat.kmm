package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class LogoutResponse(
    val status: String? = null,
    val data: LogoutData? = null
)

@Serializable
data class LogoutData(
    val message: String? = null
)