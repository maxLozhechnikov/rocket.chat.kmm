package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class RestResponseSubscription(
    val subscription: Subscription? = null,
    val success: Boolean
)