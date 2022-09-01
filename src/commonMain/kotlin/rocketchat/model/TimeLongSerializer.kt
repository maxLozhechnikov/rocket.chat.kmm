package com.omegar.libs.rocketchat.model

import com.omegar.libs.rocketchat.DefaultSerializer
import io.ktor.util.date.GMTDate
import io.ktor.util.date.Month
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

@Serializer(forClass = Long::class)
object TimeLongSerializer {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor(
        "TimeLongDescriptor", PrimitiveKind.STRING
    )

    override fun deserialize(decoder: Decoder): Long {
        val input = decoder as? JsonDecoder ?:throw SerializationRocketChatException(
            "TimeSerializerLong can be loaded only by Json"
        )

        return when(val tree = input.decodeJsonElement()) {
            is JsonPrimitive -> {
                val dataInfo = tree.content.split("-", "T", ":", ".", "Z")
                if (dataInfo.size < 7)
                    throw SerializationRocketChatException(
                        "TimeSerializerLong incorrect time format: ${tree.content}"
                                + " require: YYYY-MM-dd'T'hh:mm:ss.SSS'Z'"
                    )
                GMTDate(
                    dataInfo[5].toInt(),
                    dataInfo[4].toInt(),
                    dataInfo[3].toInt(),
                    dataInfo[2].toInt(),
                    Month.from(dataInfo[1].toInt() - 1),
                    dataInfo[0].toInt()
                ).timestamp + dataInfo[6].toLong()
            }
            is JsonObject -> DefaultSerializer.fromJson<LongDate>(tree.toString()).date
            else -> throw SerializationRocketChatException("TimeSerializerLong incorrect type")
        }
    }

    @Serializable
    data class LongDate (
        @SerialName("\$date")
        val date: Long
    )
}