package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.Serializable

@Serializable
data class LdapLoginPayload(
    val ldap: Boolean = true,
    val username: String,
    val ldapPass: String,
    val ldapOptions: Array<String> = emptyArray()
)