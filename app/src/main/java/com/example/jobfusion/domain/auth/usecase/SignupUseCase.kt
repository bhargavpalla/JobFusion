package com.example.jobfusion.domain.auth.usecase

import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.domain.auth.model.SignupRequest
import com.example.jobfusion.domain.auth.repository.AuthRepository

class SignupUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: SignupRequest): NetworkResponse<Unit> =
        repository.signup(request)
}
