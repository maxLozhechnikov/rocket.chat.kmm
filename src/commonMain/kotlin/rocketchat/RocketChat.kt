package com.omegar.libs.rocketchat

import com.omegar.libs.rocketchat.model.*
import com.omegar.libs.rocketchat.model.internal.DefaultResult
import com.omegar.libs.rocketchat.model.RestResponseDirectMessages
import com.omegar.libs.rocketchat.model.internal.QueryPayload
import com.omegar.libs.rocketchat.model.internal.QuerySelection
import com.omegar.libs.rocketchat.model.internal.RestResponseCustomEmojis
import com.omegar.libs.rocketchat.model.Command
import com.omegar.libs.rocketchat.realtime.model.socket.SocketMessage
import com.omegar.libs.rocketchat.realtime.model.socket.channel.SubscriptionsChannels
import com.omegar.libs.rocketchat.realtime.socket.SocketApi
import com.omegar.libs.rocketchat.rest.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

class RocketChat(val apiUrl: String, val logger: ChatLogger) {
    private val restClient = RestClient(apiUrl, logger, true)
    private val rocketChatLoginApi: RocketChatLoginApi = RocketChatLoginApi(restClient, logger)
    private val rocketChatUserApi: RocketChatUserApi = RocketChatUserApi(restClient, logger)
    private val rocketChatMessageApi: RocketChatMessageApi = RocketChatMessageApi(restClient, logger)
    private val rocketChatChatApi: RocketChatChatApi = RocketChatChatApi(restClient, logger)
    private val rocketChatServerApi: RocketChatServerApi = RocketChatServerApi(restClient, logger)
    @OptIn(ExperimentalCoroutinesApi::class)
    val subscriptionsChannels: SubscriptionsChannels = SubscriptionsChannels
    private val socketApi: SocketApi = SocketApi(restClient, logger, subscriptionsChannels)

    //TODO: add login function selection depending on input data
    suspend fun login(username: String, password: String): Boolean {
        return rocketChatLoginApi.login(username = username, password = password)
    }

    suspend fun signup(email: String, username: String, password: String, name: String): SignupResponse {
        return rocketChatLoginApi.signup(email = email, username = username, password = password, name = name)
    }

    suspend fun forgotPassword(email: String): Boolean {
        return rocketChatLoginApi.forgotPassword(email = email)
    }

    suspend fun logout(): Boolean {
        return rocketChatLoginApi.logout()
    }

    suspend fun me(): Boolean {
        return rocketChatUserApi.me()
    }

    suspend fun updateProfile(
        userId: String,
        email: String? = null,
        name: String? = null,
        password: String? = null,
        username: String? = null
    ): RestResponseUser {
        return rocketChatUserApi.updateProfile(userId, email, name, password, username)
    }

    suspend fun users(count: Long, offset: Long): RestResponseUsers {
        return rocketChatUserApi.users(offset = offset, count = count)
    }

    suspend fun getProfileByUserId(userId: String): RestResponseUser {
        return rocketChatUserApi.getProfileByUserId(userId = userId)
    }

    suspend fun getProfileByUsername(username: String): RestResponseUser {
        return rocketChatUserApi.getProfileByUsername(username = username)
    }

    suspend fun updateOwnBasicInfo(
        email: String? = null,
        currentPassword: String? = null,
        newPassword: String? = null,
        username: String? = null,
        name: String? = null
    ): RestResponseUser {
        return rocketChatUserApi.updateOwnBasicInfo(email, currentPassword, newPassword, username, name)
    }

    suspend fun resetAvatar(userId: String): DefaultResult {
        return rocketChatUserApi.resetAvatar(userId)
    }

    suspend fun setAvatar(fileName: String, mimeType: String, fileData: ByteArray): Boolean {
        return rocketChatUserApi.setAvatar(fileName, mimeType, fileData)
    }

    suspend fun setAvatarUrl(avatarUrl: String): Boolean {
        return rocketChatUserApi.setAvatar(avatarUrl)
    }

    suspend fun getAvatar(username: String): String {
        return rocketChatUserApi.getAvatar(username)
    }

    suspend fun getSubscription(roomId: String): RestResponseSubscription {
        return rocketChatUserApi.getSubscription(roomId)
    }

    suspend fun getSubscriptions(updatedSince: String? = null): RestResponseSubscriptionsChanges {
        return rocketChatUserApi.getSubscriptions(updatedSince)
    }

    suspend fun getRooms(updatedSince: String? = null): RestResponseRoomsChanges {
        return rocketChatUserApi.listRooms(updatedSince)
    }

    suspend fun getGroups(count: Long, offset: Long): RestResponseGroups {
        return rocketChatUserApi.listGroups(offset = offset, count = count)
    }

    suspend fun getDirectMessages(count: Long, offset: Long): RestResponseDirectMessages {
        return rocketChatUserApi.getDirectMessages(offset = offset, count = count)
    }

    suspend fun sendMessage(message: Message): RestResponseMessage {
        return rocketChatMessageApi.sendMessage(message)
    }

    suspend fun postMessage(message: Message): RestResponseMessage {
        return rocketChatMessageApi.postMessage(message)
    }

    suspend fun updateMessage(message: Message): RestResponseMessage {
        return rocketChatMessageApi.updateMessage(message)
    }

    suspend fun deleteMessage(roomId: String, messageId: String, asUser: Boolean = false): DeleteResult {
        return rocketChatMessageApi.deleteMessage(roomId, messageId, asUser)
    }

    suspend fun starMessage(messageId: String): DefaultResult {
        return rocketChatMessageApi.starMessage(messageId)
    }

    suspend fun unStarMessage(messageId: String): DefaultResult {
        return rocketChatMessageApi.unStarMessage(messageId)
    }

    suspend fun pinMessage(messageId: String): DefaultResult {
        return rocketChatMessageApi.pinMessage(messageId)
    }

    suspend fun unPinMessage(messageId: String): DefaultResult {
        return rocketChatMessageApi.unPinMessage(messageId)
    }

    suspend fun toggleReaction(messageId: String, emoji: String): DefaultResult {
        return rocketChatMessageApi.toggleReaction(messageId, emoji)
    }

    suspend fun uploadFile(file: UploadFile): Boolean {
        return rocketChatMessageApi.uploadFile(file)
    }

    suspend fun getMessages(
        roomId: String,
        roomType: RoomType,
        offset: Long,
        count: Long
    ): RestResponseMessages {
        return rocketChatMessageApi.getMessages(roomId, roomType, offset, count)
    }

    suspend fun getMessage(messageId: String): RestResponseMessage {
        return rocketChatMessageApi.getMessage(messageId)
    }

    suspend fun getHistory(
        roomId: String,
        roomType: RoomType,
        count: Long = 50,
        oldest: String? = null,
        latest: String? = null
    ): RestResponseMessages {
        return rocketChatMessageApi.getHistory(roomId, roomType, count, oldest, latest)
    }

    suspend fun getMessageReadReceipts(
        messageId: String,
        count: Long = 50,
        oldest: String? = null,
        latest: String? = null
    ): RestResponseReadReceipts {
        return rocketChatMessageApi.getMessageReadReceipts(messageId, count, oldest, latest)
    }

    suspend fun getMembers(roomId: String, roomType: RoomType, offset: Long, count: Long): RestResponseMembers {
        return rocketChatChatApi.getMembers(roomId, roomType, offset, count)
    }

    suspend fun getMentions(roomId: String, offset: Long, count: Long): RestResponseMentions {
        return rocketChatChatApi.getMentions(roomId, offset, count)
    }

    suspend fun getFavoriteMessages(roomId: String, roomType: RoomType, offset: Long): RestResponseMessages {
        return rocketChatChatApi.getMessagesQuery(
            roomId,
            roomType,
            offset,
            query = QueryPayload(starredId = QuerySelection(listOf(restClient.currentUserId)))
        )
    }

    suspend fun getPinnedMessages(roomId: String, roomType: RoomType, offset: Long): RestResponseMessages {
        return rocketChatChatApi.getMessagesQuery(
            roomId,
            roomType,
            offset,
            query = QueryPayload(pinned = true)
        )
    }

    suspend fun getFiles(roomId: String, roomType: RoomType, offset: Long): RestResponseFiles {
        return rocketChatChatApi.getFiles(roomId, roomType, offset)
    }

    suspend fun getInfo(roomId: String? = null, roomName: String? = null, roomType: RoomType): Room? {
        return rocketChatChatApi.getInfo(roomId, roomName, roomType)
    }

    suspend fun markAsRead(roomId: String): Boolean {
        return rocketChatChatApi.markAsRead(roomId)
    }

    suspend fun joinChat(roomId: String): Boolean {
        return rocketChatChatApi.joinChat(roomId)
    }

    suspend fun leaveChat(roomId: String, roomType: RoomType): Boolean {
        return rocketChatChatApi.leaveChat(roomId, roomType)
    }

    suspend fun renameRoom(roomId: String, roomType: RoomType, newName: String): Boolean {
        return rocketChatChatApi.rename(roomId, roomType, newName)
    }

    suspend fun setReadOnly(roomId: String, roomType: RoomType, readOnly: Boolean): Boolean {
        return rocketChatChatApi.setReadOnly(roomId, roomType, readOnly)
    }

    suspend fun setRoomType(roomId: String, roomType: RoomType, newType: RoomType): Boolean {
        return rocketChatChatApi.setType(roomId, roomType, newType)
    }

    suspend fun setJoinCode(roomId: String, roomType: RoomType, joinCode: String): Boolean {
        return rocketChatChatApi.setJoinCode(roomId, roomType, joinCode)
    }

    suspend fun setTopic(roomId: String, roomType: RoomType, topic: String?): Boolean {
        return rocketChatChatApi.setTopic(roomId, roomType, topic)
    }

    suspend fun setDescription(roomId: String, roomType: RoomType, description: String?): Boolean {
        return rocketChatChatApi.setDescription(roomId, roomType, description)
    }

    suspend fun setAnnouncement(roomId: String, roomType: RoomType, announcement: String?): Boolean {
        return rocketChatChatApi.setAnnouncement(roomId, roomType, announcement)
    }

    suspend fun hideRoom(roomId: String, roomType: RoomType): Boolean {
        return rocketChatChatApi.hide(roomId, roomType)
    }

    suspend fun showRoom(roomId: String, roomType: RoomType): Boolean {
        return rocketChatChatApi.show(roomId, roomType)
    }

    suspend fun setFavorite(roomId: String, favorite: Boolean): Boolean {
        return rocketChatChatApi.favorite(roomId, favorite)
    }

    suspend fun searchMessages(roomId: String, searchText: String): RestResponseMessages {
        return rocketChatChatApi.searchMessages(roomId, searchText)
    }

    suspend fun getRoomRoles(roomType: RoomType, roomName: String): RestResponseChatRoomRoles {
        return rocketChatChatApi.chatRoomRoles(roomType, roomName)
    }

    suspend fun saveNotification(roomId: String, disable: Boolean): Boolean {
        return rocketChatChatApi.saveNotification(roomId, disable)
    }

    suspend fun kickUser(roomId: String, roomType: RoomType, userId: String): Boolean {
        return rocketChatChatApi.kickUser(roomId, roomType, userId)
    }

    suspend fun inviteUser(roomId: String, roomType: RoomType, userId: String): Boolean {
        return rocketChatChatApi.inviteUser(roomId, roomType, userId)
    }

    suspend fun closeDirectMessages(roomId: String): Boolean {
        return rocketChatChatApi.closeDirectMessages(roomId)
    }

    suspend fun getCommands(count: Long = 50, offset: Long = 0): RestResponseCommands {
        return rocketChatChatApi.getCommands(offset = offset, count = count)
    }

    suspend fun runCommand(command: Command, roomId: String): Boolean {
        return rocketChatChatApi.runCommand(command, roomId)
    }

    suspend fun registerPushToken(token: String): Boolean {
        return rocketChatChatApi.registerPushToken(token)
    }

    suspend fun unregisterPushToken(token: String): Boolean {
        return rocketChatChatApi.unregisterPushToken(token)
    }

    suspend fun getCustomEmojis(): RestResponseCustomEmojis {
        return rocketChatChatApi.getCustomEmojis()
    }

    suspend fun getPermissions(): RestResponsePermissions {
        return rocketChatChatApi.getPermissions()
    }

    suspend fun getServerInfo(): RestResponseServerInfo {
        return rocketChatServerApi.getServerInfo()
    }

    suspend fun getConfigurations(): RestResponseConfigurations {
        return rocketChatServerApi.getConfigurations()
    }

    suspend fun getSettings(): RestResponseSettings {
        return rocketChatServerApi.getSettings()
    }

    suspend fun getSettingsOauth(): RestResponseSettingsOauth {
        return rocketChatServerApi.getSettingsOauth()
    }

    suspend fun getSpotlight(query: String): RestResponseSpotlight {
        return rocketChatChatApi.getSpotlight(query)
    }

    suspend fun startWebSocket(): Boolean {
        socketApi.startWebSocketSession()
        return true
    }

    suspend fun disconnectWebSocket(disconnectTime: Long = 0): Boolean {
        socketApi.disconnect(disconnectTime)
        return true
    }

    internal suspend fun unsubscribeAll() {
        socketApi.unsubscribeAll()
    }

    suspend fun sendWebSocketMessage(socketMessage: SocketMessage) {
        socketApi.sendWebSocketMessage(socketMessage)
    }

    suspend fun sendSubscriptionMessages() {
        socketApi.sendSubscriptionMessages()
    }

    suspend fun setTypingStatus(roomId: String, username: String, isTyping: Boolean) {
        socketApi.setTypingStatus(roomId, username, isTyping)
    }

    suspend fun subscribeTypingStatus(roomId: String): String {
        return socketApi.subscribeTypingStatus(roomId)
    }

    suspend fun subscribeRooms(): String {
        return socketApi.subscribeRooms()
    }

    suspend fun subscribeRoomMessages(roomId: String): String {
        return socketApi.subscribeRoomMessages(roomId)
    }

    suspend fun createDirectMessage(username: String): String {
        return socketApi.createDirectMessage(username)
    }

    suspend fun subscribeSubscriptions(): String {
        return socketApi.subscribeSubscriptions()
    }

    suspend fun unsubscribe(subId: String) {
        return socketApi.unsubscribe(subId)
    }

    suspend fun setDefaultStatus(status: UserStatus) {
        socketApi.setDefaultStatus(status)
    }

    suspend fun setTemporaryStatus(status: UserStatus) {
        socketApi.setTemporaryStatus(status)
    }

    suspend fun subscribeUserData(): String {
        return socketApi.subscribeUserData()
    }

    suspend fun subscribeActiveUsers(): String {
        return socketApi.subscribeActiveUsers()
    }

    suspend fun subscribeUserStatus(): String {
        return socketApi.subscribeUserStatus()
    }
}