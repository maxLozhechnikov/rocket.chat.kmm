package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class Email(
    val address: String,
    val verified: Boolean = false
)