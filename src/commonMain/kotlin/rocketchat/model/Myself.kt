package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Myself(
    val success: Boolean,
    @SerialName("_id")
    val id: String,
    val active: Boolean?,
    val username: String?,
    val name: String?,
    val status: UserStatus?,
    val statusConnection: UserStatus?,
    val statusDefault: UserStatus?=null,
    val utcOffset: Float?,
    val emails: List<Email>?,
    val roles: List<String>?
)