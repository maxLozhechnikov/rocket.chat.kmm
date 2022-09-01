package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class RestResponseReadReceipts(
    val receipts: List<ReadReceipt>? = null,
    val success: Boolean
)