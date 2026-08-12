package com.example.domain.auth.model

/**
 * Successful login payload from the backend (e.g. JWT or opaque session token).
 */
data class LoginResult(
    val accessToken: String
)
