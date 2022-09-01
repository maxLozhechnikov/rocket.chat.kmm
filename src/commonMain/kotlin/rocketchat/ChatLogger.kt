package com.omegar.libs.rocketchat

interface ChatLogger {
    fun log(level: Level, tag: String, message: String)

    enum class Level {
        DEBUG, INFO, ERROR
    }
}