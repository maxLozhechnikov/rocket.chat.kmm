package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.Serializable

@Serializable
data class CasLoginPayload(val cas: CasData)

@Serializable
data class CasData(val credentialToken: String)