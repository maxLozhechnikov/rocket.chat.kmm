package com.omegar.libs.rocketchat.realtime.model.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SocketMessageCollection {
    @SerialName("stream-notify-user")
    STREAM_NOTIFY_USER,
    @SerialName("stream-room-messages")
    STREAM_ROOM_MESSAGES,
    @SerialName("stream-notify-room")
    STREAM_NOTIFY_ROOM,
    @SerialName("stream-notify-logged")
    STREAM_NOTIFY_LOGGED,
    @SerialName("users")
    USERS
}