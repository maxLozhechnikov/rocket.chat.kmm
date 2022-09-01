package com.omegar.libs.rocketchat

class SystemChatLogger: ChatLogger {
    override fun log(level: ChatLogger.Level, tag: String, message: String) {
        println("$level: $tag: $message")
    }
}