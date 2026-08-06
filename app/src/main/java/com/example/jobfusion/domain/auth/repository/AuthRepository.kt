package com.example.jobfusion.domain.auth.repository

import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.domain.auth.model.LoginResult
import com.example.jobfusion.domain.auth.model.SignupRequest

interface AuthRepository {
    suspend fun login(email: String, password: String): NetworkResponse<LoginResult>
    suspend fun signup(request: SignupRequest): NetworkResponse<Unit>
}
