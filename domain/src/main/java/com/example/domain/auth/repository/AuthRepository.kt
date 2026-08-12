package com.example.domain.auth.repository

import com.example.domain.auth.OutCome
import com.example.domain.auth.model.LoginResult
import com.example.domain.auth.model.SignupRequest

interface AuthRepository {
    suspend fun login(email: String, password: String): OutCome<LoginResult>
    suspend fun signup(request: SignupRequest): OutCome<Unit>
}
