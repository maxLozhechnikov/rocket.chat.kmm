package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class ParsedUrl(
    val host: String? = null,
    val hash: String? = null,
    val pathname: String? = null,
    val protocol: String? = null,
    val port: String? = null,
    val search: String? = null,
    val hostname: String? = null
)