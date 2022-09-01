package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.*
import com.omegar.libs.rocketchat.model.RoomType

@Serializable
data class RoomIdPayload(
    val roomId: String? = null
)

@Serializable
data class RidPayload(
    @SerialName("rid")
    val roomId: String? = null
)

@Serializable
data class RoomTypePayload(
    val roomId: String,
    val type: RoomType
)

@Serializable
data class RoomRenamePayload(
    val roomId: String,
    val name: String
)

@Serializable
data class ChatRoomReadOnlyPayload(
    val roomId: String,
    val readOnly: Boolean
)

@Serializable
data class ChatRoomJoinCodePayload(
    val roomId: String,
    val joinCode: String
)

@Serializable
data class ChatRoomTopicPayload(
    val roomId: String,
    val topic: String?
)

@Serializable
data class ChatRoomDescriptionPayload(
    val roomId: String,
    val description: String?
)

@Serializable
data class ChatRoomAnnouncementPayload(
    val roomId: String,
    val announcement: String?
)

@Serializable
data class ChatRoomFavoritePayload(
    val roomId: String,
    val favorite: Boolean
)