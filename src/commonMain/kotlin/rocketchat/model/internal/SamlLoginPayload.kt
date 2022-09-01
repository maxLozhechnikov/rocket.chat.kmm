package com.omegar.libs.rocketchat.model.internal
import kotlinx.serialization.Serializable

@Serializable
data class SamlLoginPayload(val saml: Boolean = true, val credentialToken: String)