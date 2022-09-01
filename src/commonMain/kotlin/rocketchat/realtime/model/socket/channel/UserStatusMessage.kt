package com.omegar.libs.rocketchat.realtime.model.socket.channel

import com.omegar.libs.rocketchat.model.UserStatus

data class UserStatusMessage(
    val userId: String?,
    val username: String?,
    val userStatus: UserStatus?
)