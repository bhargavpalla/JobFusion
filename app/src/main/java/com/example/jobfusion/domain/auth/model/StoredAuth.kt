package com.example.jobfusion.domain.auth.model

/**
 * Restored auth session from local storage (token + role).
 */
data class StoredAuth(
    val accessToken: String,
    val role: UserRole
)
