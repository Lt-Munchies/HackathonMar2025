package com.devconnect.network.util

import kotlinx.coroutines.delay
import timber.log.Timber
import kotlin.math.min
import kotlin.math.pow

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String, val code: Int? = null) : NetworkResult<Nothing>()
    data object Loading : NetworkResult<Nothing>()
}

suspend fun <T> withRetry(
    maxAttempts: Int = 3,
    initialDelayMs: Long = 1000,
    maxDelayMs: Long = 5000,
    block: suspend () -> T
): NetworkResult<T> {
    var currentDelay = initialDelayMs
    repeat(maxAttempts) { attempt ->
        try {
            return NetworkResult.Success(block())
        } catch (e: Exception) {
            Timber.e(e, "Network call failed (attempt ${attempt + 1}/$maxAttempts)")
            
            if (attempt == maxAttempts - 1) {
                return NetworkResult.Error(
                    when (e) {
                        is retrofit2.HttpException -> when (e.code()) {
                            400 -> "Invalid request. Please check your input."
                            404 -> "Resource not found."
                            500 -> "Server error. Please try again later."
                            503 -> "AI service is currently unavailable."
                            else -> "Network error (${e.code()})"
                        }
                        is java.net.UnknownHostException -> "No internet connection."
                        is java.net.SocketTimeoutException -> "Request timed out."
                        else -> e.message ?: "Unknown error occurred."
                    },
                    (e as? retrofit2.HttpException)?.code()
                )
            }

            delay(currentDelay)
            currentDelay = min(currentDelay * 2, maxDelayMs)
        }
    }
    
    return NetworkResult.Error("All retry attempts failed")
}
