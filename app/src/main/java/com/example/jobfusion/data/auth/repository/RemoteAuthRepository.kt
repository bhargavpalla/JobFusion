package com.example.jobfusion.data.auth.repository

import com.example.jobfusion.core.dispatcher.DefaultDispatcherProvider
import com.example.jobfusion.core.dispatcher.DispatcherProvider
import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.core.network.networkTrySuspend
import com.example.jobfusion.data.auth.mapper.toDto
import com.example.jobfusion.data.auth.remote.AuthApi
import com.example.jobfusion.data.auth.remote.model.LoginRequestDto
import com.example.jobfusion.domain.auth.model.LoginResult
import com.example.jobfusion.domain.auth.model.SignupRequest
import com.example.jobfusion.domain.auth.repository.AuthRepository
import kotlinx.coroutines.withContext

class RemoteAuthRepository(
    private val authApi: AuthApi,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : AuthRepository {
    override suspend fun login(email: String, password: String): NetworkResponse<LoginResult> =
        networkTrySuspend {
            withContext(dispatcherProvider.io) {
                val dto = authApi.login(LoginRequestDto(email = email, password = password))
                LoginResult(accessToken = dto.accessToken)
            }
        }

    override suspend fun signup(request: SignupRequest): NetworkResponse<Unit> =
        networkTrySuspend {
            withContext(dispatcherProvider.io) {
                authApi.signup(request.toDto())
            }
        }
}
