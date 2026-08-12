package com.example.domain.auth.usecase

import com.example.domain.auth.OutCome
import com.example.domain.auth.model.SignupRequest
import com.example.domain.auth.repository.AuthRepository

class SignupUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(request: SignupRequest): OutCome<Unit> =
        repository.signup(request)
}
