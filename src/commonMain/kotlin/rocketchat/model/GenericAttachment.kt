package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GenericAttachment(
    @SerialName("_id")
    val id: String,
    val name: String?,
    val type: String?,
    val size: String?,
    val userId: String,
    val user: UserData,
    val path: String?,
    @Serializable(with= TimeLongSerializer::class)
    val uploadedAt: Long?
)

@Serializable
data class UserData(val username: String?, val name: String?)