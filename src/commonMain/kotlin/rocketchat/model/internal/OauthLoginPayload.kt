package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.Serializable

@Serializable
data class OauthLoginPayload(val oauth: OauthData)

@Serializable
data class OauthData(val credentialToken: String, val credentialSecret: String)