package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.Serializable

@Serializable
data class OwnBasicInformationPayload(val data: OwnBasicInformationPayloadData)

@Serializable
data class OwnBasicInformationPayloadData(
    val email: String? = null,
    val currentPassword: String? = null,
    val newPassword: String? = null,
    val username: String? = null,
    val name: String? = null
)