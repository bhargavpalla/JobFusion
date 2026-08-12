package com.example.domain.auth.usecase

import com.example.domain.auth.OutCome
import com.example.domain.auth.model.LoginResult
import com.example.domain.auth.repository.AuthRepository

class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): OutCome<LoginResult> =
        repository.login(email = email, password = password)
}
