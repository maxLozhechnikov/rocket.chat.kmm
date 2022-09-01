package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val color: String? = null,
    val text: String? = null,
    @SerialName("ts")
    @Serializable(with= TimeLongSerializer::class)
    val timestamp: Long? = null,
    @SerialName("thumb_url")
    val thumbnailUrl: String? = null,
    @SerialName("message_link")
    val messageLink: String? = null,
    val collapsed: Boolean = false,
    @SerialName("author_name")
    val authorName: String? = null,
    @SerialName("author_link")
    val authorLink: String? = null,
    @SerialName("author_icon")
    val authorIcon: String? = null,
    val title: String? = null,
    @SerialName("title_link")
    val titleLink: String? = null,
    @SerialName("title_link_download")
    val titleLinkDownload: Boolean = false,
    @SerialName("image_url")
    val imageUrl: String? = null,
    @SerialName("audio_url")
    val audioUrl: String? = null,
    @SerialName("video_url")
    val videoUrl: String? = null,
    val fields: List<Fields>? = null
)

@Serializable
data class Fields(
    val short: Boolean = true,
    val title: String? = null,
    val value: String? = null
)