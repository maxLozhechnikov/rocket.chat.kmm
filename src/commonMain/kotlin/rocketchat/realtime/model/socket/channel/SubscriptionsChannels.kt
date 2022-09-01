package com.omegar.libs.rocketchat.realtime.model.socket.channel

import com.omegar.libs.rocketchat.model.Message
import com.omegar.libs.rocketchat.model.Room
import com.omegar.libs.rocketchat.model.Subscription
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel

@ExperimentalCoroutinesApi
object SubscriptionsChannels {
    val usersTypingChannel= BroadcastChannel<UserTypingMessage>(Channel.BUFFERED)
    val usersStatusChannel= BroadcastChannel<UserStatusMessage>(Channel.BUFFERED)
    val messagesChannel= BroadcastChannel<Message>(Channel.BUFFERED)
    val roomChannel= BroadcastChannel<Room>(Channel.BUFFERED)
    val subscriptionChannel= BroadcastChannel<Subscription>(Channel.BUFFERED)
}