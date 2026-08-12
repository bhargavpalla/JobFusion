package com.example.domain.auth.repository

import com.example.domain.auth.model.StoredAuth
import com.example.domain.auth.model.UserRole

interface AuthTokenRepository {
    suspend fun saveSession(accessToken: String, role: UserRole)
    suspend fun clear()
    suspend fun getCurrentSession(): StoredAuth?
}
