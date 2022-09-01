package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SortPayload(
    @SerialName("ts")
    val displayTimeSortOrder: Int? = null,
    @SerialName("uploadedAt")
    val uploadTimeSortOrder: Int? = null
)