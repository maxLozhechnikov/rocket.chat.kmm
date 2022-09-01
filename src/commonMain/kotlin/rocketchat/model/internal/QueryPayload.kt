package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QueryPayload(
    @SerialName("starred._id")
    val starredId: QuerySelection? = null,
    val pinned: Boolean? = null
)

@Serializable
data class QuerySelection(
    @SerialName("\$in")
    val inList: List<String>
)