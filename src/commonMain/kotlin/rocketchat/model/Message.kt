package com.omegar.libs.rocketchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @SerialName("_id")
    val id: String? = null,
    @SerialName("rid")
    val roomId: String? = null,
    @SerialName("msg")
    val text: String? = null,
    @SerialName("ts")
    @Serializable(with= TimeLongSerializer::class)
    val displayTime: Long? = null,
    @SerialName("u")
    val sender: SimpleUser? = null,
    @SerialName("_updatedAt")
    @Serializable(with= TimeLongSerializer::class)
    val updatedAt: Long? = null,
    @Serializable(with= TimeLongSerializer::class)
    val editedAt: Long? = null,
    val editedBy: SimpleUser? = null,
    @SerialName("alias")
    val senderAlias: String? = null,
    val avatar: String? = null,
    @SerialName("t")
    val type: MessageType? = null,
    val groupable: Boolean = false,
    val parseUrls: Boolean = false,
    val urls: List<Url>? = null,
    val mentions: List<SimpleUser>? = null,
    val channels: List<SimpleRoom>? = null,
    val attachments: List<Attachment>? = null,
    val pinned: Boolean = false,
    val starred: List<SimpleUser>? = null,
    val reactions: Map<String, Usernames>? = null,
    val role: String? = null,
    val synced: Boolean = true,
    val unread: Boolean? = null,
    val emoji: String? = null
)

@Serializable
enum class MessageType {
    @SerialName("r")
    ROOM_NAME_CHANGED,
    @SerialName("au")
    USER_ADDED,
    @SerialName("ru")
    USER_REMOVED,
    @SerialName("uj")
    USER_JOINED,
    @SerialName("ul")
    USER_LEFT,
    @SerialName("wm")
    WELCOME,
    @SerialName("rm")
    MESSAGE_REMOVED,
    @SerialName("message_pinned")
    MESSAGE_PINNED,
    @SerialName("user-muted")
    USER_MUTED,
    @SerialName("user-unmuted")
    USER_UN_MUTED,
    @SerialName("subscription-role-added")
    SUBSCRIPTION_ROLE_ADDED,
    @SerialName("subscription-role-removed")
    SUBSCRIPTION_ROLE_REMOVED,
    @SerialName("room_changed_privacy")
    ROOM_CHANGED_PRIVACY,
    @SerialName("room_changed_topic")
    ROOM_CHANGED_TOPIC,
    @SerialName("room_changed_description")
    ROOM_CHANGED_DESCRIPTION,
    @SerialName("room_changed_announcement")
    ROOM_CHANGED_ANNOUNCEMENT
}

@Serializable
data class Usernames(
    @SerialName("usernames")
    val list: List<String>
)