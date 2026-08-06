package com.example.jobfusion.domain.auth.usecase

import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.domain.auth.model.LoginResult
import com.example.jobfusion.domain.auth.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): NetworkResponse<LoginResult> =
        repository.login(email = email, password = password)
}
