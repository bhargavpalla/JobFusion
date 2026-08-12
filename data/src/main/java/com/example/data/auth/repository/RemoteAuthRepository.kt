package com.example.data.auth.repository


import com.example.data.auth.mapper.toDto
import com.example.data.auth.mapper.toOutCome
import com.example.data.auth.remote.AuthApi
import com.example.data.auth.remote.model.LoginRequestDto
import com.example.data.core.dispatcher.DefaultDispatcherProvider
import com.example.data.core.dispatcher.DispatcherProvider
import com.example.data.core.network.networkTrySuspend
import com.example.domain.auth.OutCome
import com.example.domain.auth.model.LoginResult
import com.example.domain.auth.model.SignupRequest
import com.example.domain.auth.repository.AuthRepository
import kotlinx.coroutines.withContext

class RemoteAuthRepository(
    private val authApi: AuthApi,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : AuthRepository {
    override suspend fun login(email: String, password: String): OutCome<LoginResult> =


        networkTrySuspend {
            withContext(dispatcherProvider.io) {
                val dto = authApi.login(LoginRequestDto(email = email, password = password))
                LoginResult(accessToken = dto.accessToken)
            }
        }.toOutCome()

    override suspend fun signup(request: SignupRequest): OutCome<Unit> =
        networkTrySuspend {
            withContext(dispatcherProvider.io) {
                authApi.signup(request.toDto())
            }
        }.toOutCome()

}
