package com.omegar.libs.rocketchat.model

open class RocketChatException(message: String, cause: Throwable? = null, private val url: String? = null) : Exception(message, cause)

open class ApiRocketChatException(message: String, cause: Throwable? = null, private val url: String? = null) : RocketChatException(message, cause) {
    override fun toString(): String {
        return "${super.toString()}(url='$url')"
    }
}

open class SerializationRocketChatException(message: String, cause: Throwable? = null, private val url: String? = null) : RocketChatException(message, cause)

open class TypeRocketChatException(message: String, cause: Throwable? = null, private val url: String? = null) : RocketChatException(message, cause)