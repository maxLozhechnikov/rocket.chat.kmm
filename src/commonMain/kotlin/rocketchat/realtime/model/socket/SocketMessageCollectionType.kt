package com.omegar.libs.rocketchat.realtime.model.socket

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SocketMessageCollectionType {
    @SerialName("method")
    METHOD,
    @SerialName("sub")
    SUBSCRIBE,
    @SerialName("unsub")
    UNSUBSCRIBE,
    @SerialName("connect")
    CONNECT,
    @SerialName("connected")
    CONNECTED,
    @SerialName("result")
    RESULT,
    @SerialName("ready")
    READY,
    @SerialName("nosub")
    UNSUBSCRIBED,
    @SerialName("updated")
    UPDATED,
    @SerialName("added")
    ADDED,
    @SerialName("changed")
    CHANGED,
    @SerialName("removed")
    REMOVED,
    @SerialName("ping")
    PING,
    @SerialName("pong")
    PONG,
    @SerialName("error")
    ERROR
}