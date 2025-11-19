package com.wbprofit.feature.auth.impl.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val DEFAULT_MAX_REQUESTS = 3
private const val DEFAULT_WINDOW_MILLIS = 30_000L

internal class AuthRateLimiter(
    private val timeSource: () -> Long = { System.currentTimeMillis() },
    private val maxRequests: Int = DEFAULT_MAX_REQUESTS,
    private val windowMillis: Long = DEFAULT_WINDOW_MILLIS,
) {
    private val mutex = Mutex()
    private val timestamps = ArrayDeque<Long>()

    suspend fun acquire(): RateLimiterResult = mutex.withLock {
        val now = timeSource()
        purgeExpired(now)

        if (timestamps.size >= maxRequests) {
            val oldest = timestamps.first()
            val retryAfter = (oldest + windowMillis) - now
            RateLimiterResult.Denied(retryAfterMillis = retryAfter.coerceAtLeast(0L))
        } else {
            timestamps.addLast(now)
            RateLimiterResult.Allowed
        }
    }

    private fun purgeExpired(nowMillis: Long) {
        while (timestamps.isNotEmpty() && nowMillis - timestamps.first() >= windowMillis) {
            timestamps.removeFirst()
        }
    }

    suspend fun reset() = mutex.withLock {
        timestamps.clear()
    }
}

internal sealed interface RateLimiterResult {
    data class Denied(val retryAfterMillis: Long) : RateLimiterResult

    data object Allowed : RateLimiterResult
}
