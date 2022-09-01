package com.omegar.libs.rocketchat.model.internal

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class SaveNotificationPayload(
    val notifications: NotificationsPayload,
    val roomId: String = ""
)

@Serializable
data class NotificationsPayload(
    val desktopNotifications: NotificationCategory? = null,
    @Serializable(with=BoolToStringSerializer::class)
    val disableNotifications: Boolean? = null,
    val emailNotifications: NotificationCategory? = null,
    val audioNotificationValue: String? = null,
    val desktopNotificationDuration: Int? = null,
    val audioNotifications: NotificationCategory? = null,
    val unreadAlert: NotificationCategory? = null,
    @Serializable(with=BoolToStringSerializer::class)
    val hideUnreadStatus: Boolean? = null,
    val mobilePushNotifications: NotificationCategory? = null
)

@Serializer(forClass = Boolean::class)
object BoolToStringSerializer {
    private const val ENCODE_VALUE_TRUE = "1"
    private const val ENCODE_VALUE_FALSE = "0"

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "BoolToStringDescriptor", PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): Boolean {
        return decoder.decodeString() == ENCODE_VALUE_TRUE
    }

    override fun serialize(encoder: Encoder, value: Boolean) {
        encoder.encodeString(if (value) ENCODE_VALUE_TRUE else ENCODE_VALUE_FALSE)
    }
}

@Serializable
enum class NotificationCategory {
    @SerialName("nothing")
    NOTHING,
    @SerialName("all")
    ALL,
    @SerialName("mentions")
    MENTIONS,
    @SerialName("default")
    DEFAULT
}


