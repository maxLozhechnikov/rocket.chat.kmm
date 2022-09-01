package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestResponsePermissions(
    @SerialName("update")
    val updateList: List<Permission>? = null,
    @SerialName("remove")
    val removeList: List<Permission>? = null,
    val success: Boolean
)