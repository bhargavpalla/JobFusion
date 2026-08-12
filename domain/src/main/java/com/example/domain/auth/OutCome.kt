package com.example.domain.auth

sealed class OutCome<out T> {

    data class Success<T>(val data: T) : OutCome<T>()

    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : OutCome<Nothing>()
}