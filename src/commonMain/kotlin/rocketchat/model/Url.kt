package com.omegar.libs.rocketchat.model

import kotlinx.serialization.Serializable

@Serializable
data class Url(
    val url: String,
    val meta: Meta? = null,
    val headers: Map<String, String>? = null,
    val parsedUrl: ParsedUrl? = null,
    val ignoreParse: Boolean = false
)