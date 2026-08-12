package com.example.data.core.network

/**
 * Unified async outcome for network and repository calls.
 *
 * Suspend repository methods return only [Success] or [Error]. [Loading] is for UI / ViewModel state
 * while a request is in flight.
 */
sealed class NetworkResponse<out T> {

    data class Success<T>(val data: T) : NetworkResponse<T>()

    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : NetworkResponse<Nothing>()
}

fun Throwable.toNetworkError(fallbackMessage: String = "Something went wrong"): NetworkResponse.Error =
    NetworkResponse.Error(message = message ?: fallbackMessage, cause = this)

inline fun <T> networkTry(block: () -> T): NetworkResponse<T> =
    try {
        NetworkResponse.Success(block())
    } catch (e: Exception) {
        e.toNetworkError()
    }

suspend inline fun <T> networkTrySuspend(crossinline block: suspend () -> T): NetworkResponse<T> =
    try {
        NetworkResponse.Success(block())
    } catch (e: Exception) {
        e.toNetworkError()
    }
