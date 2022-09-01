package com.omegar.libs.rocketchat.realtime.model.socket

import com.omegar.libs.rocketchat.model.TypeRocketChatException
import com.omegar.libs.rocketchat.model.UserStatus
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Encoder

@Serializable
sealed class ParamValue {

    companion object {
        fun paramList(vararg args: Any): List<ParamValue> {
            return args.map {
                when (it) {
                    is Boolean -> BoolParamValue(it)
                    is String -> StringParamValue(it)
                    is SocketParameterLogin -> LoginParamValue(it)
                    is UserStatus -> StatusParamValue(it)
                    else -> throw TypeRocketChatException("paramList incorrect input type")
                }
            }
        }
    }

    @Serializer(forClass = ParamValue::class)
    object ParamValueSerializer: KSerializer<ParamValue> {

        override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
            "ParamValue", PrimitiveKind.STRING
        )

        override fun serialize(encoder: Encoder, value: ParamValue) {
            when (value) {
                is BoolParamValue -> encoder.encodeBoolean(value.boolValue)
                is StringParamValue -> encoder.encodeString(value.stringValue)
                is LoginParamValue -> encoder.encodeSerializableValue(
                    SocketParameterLogin.serializer(), value.loginValue)
                is StatusParamValue -> encoder.encodeSerializableValue(UserStatus.serializer(), value.statusValue)
            }
        }
    }

    @Serializable
    data class BoolParamValue(
        val boolValue: Boolean
    ): ParamValue()

    @Serializable
    data class StringParamValue(
        val stringValue: String
    ): ParamValue()

    @Serializable
    data class LoginParamValue(
        val loginValue: SocketParameterLogin
    ): ParamValue()

    @Serializable
    data class StatusParamValue(
        val statusValue: UserStatus
    ): ParamValue()
}