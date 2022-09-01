package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseData(
    val authToken: String?,
    val userId: String?
)