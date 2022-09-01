package com.omegar.libs.rocketchat.realtime.model.socket

import kotlinx.serialization.Serializable

@Serializable
data class SocketParameterLogin(
    val resume: String
)