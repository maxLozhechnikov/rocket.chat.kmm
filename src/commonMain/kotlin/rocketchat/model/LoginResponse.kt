package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val status: String?,
    val data: LoginResponseData?
)