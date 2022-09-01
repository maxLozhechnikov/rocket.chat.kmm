package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestResponseSubscriptionsChanges(
    @SerialName("update")
    val updateList: List<Subscription>? = null,
    @SerialName("remove")
    val removeList: List<Subscription>? = null,
    val success: Boolean
)