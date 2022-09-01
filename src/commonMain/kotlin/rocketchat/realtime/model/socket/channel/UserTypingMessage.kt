package com.omegar.libs.rocketchat.realtime.model.socket.channel

data class UserTypingMessage(
    val user: String?,
    val isTyping: Boolean?
)