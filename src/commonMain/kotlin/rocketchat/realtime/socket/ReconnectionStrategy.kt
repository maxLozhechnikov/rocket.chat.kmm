package com.omegar.libs.rocketchat.realtime.socket

open class ReconnectionStrategy(
    private val maxAttempts: Int = Int.MAX_VALUE,
    private val interval: Int = DEFAULT_RECONNECT_INTERVAL
) {
    companion object {
        const val DEFAULT_RECONNECT_INTERVAL = 3000
        private const val MAX_RECONNECT_INTERVAL = 30000
    }
    var numberOfAttempts: Int = 0

    fun processAttempts() {
        numberOfAttempts++
    }

    val reconnectInterval: Int
        get() {
            val value = interval * (numberOfAttempts + 1)
            return if (value > MAX_RECONNECT_INTERVAL) MAX_RECONNECT_INTERVAL else value
        }

    val shouldRetry: Boolean
        get() = maxAttempts == Int.MAX_VALUE || numberOfAttempts < maxAttempts

    fun reset() {
        numberOfAttempts = 0
    }
}