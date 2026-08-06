package com.example.jobfusion.domain.auth.repository

import com.example.jobfusion.domain.auth.model.StoredAuth
import com.example.jobfusion.domain.auth.model.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthTokenRepository {
    suspend fun saveSession(accessToken: String, role: UserRole)
    suspend fun clear()
    suspend fun getCurrentSession(): StoredAuth?
    fun observeSession(): Flow<StoredAuth?>
}
