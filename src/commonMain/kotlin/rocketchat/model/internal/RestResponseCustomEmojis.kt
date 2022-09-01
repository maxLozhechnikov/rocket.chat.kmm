package com.omegar.libs.rocketchat.model.internal

import com.omegar.libs.rocketchat.model.CustomEmoji
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RestResponseCustomEmojis(
    val emojis: EmojisChanges? = null,
    val success: Boolean
)

@Serializable
data class EmojisChanges(
    @SerialName("update")
    val updateList: List<CustomEmoji>? = null,
    @SerialName("remove")
    val removeList: List<CustomEmoji>? = null
)