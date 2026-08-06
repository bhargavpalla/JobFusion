package com.example.jobfusion.ui.auth

import android.content.Context
import com.example.jobfusion.core.network.RetrofitProvider
import com.example.jobfusion.data.auth.local.DataStoreAuthTokenRepository
import com.example.jobfusion.data.auth.repository.FakeAuthRepository
import com.example.jobfusion.data.auth.repository.RemoteAuthRepository
import com.example.jobfusion.domain.auth.repository.AuthRepository
import com.example.jobfusion.domain.auth.repository.AuthTokenRepository

object AuthDependencies {
    private const val USE_REMOTE_SOURCE = false

    // Flip USE_REMOTE_SOURCE=true when backend contracts are available.
    val authRepository: AuthRepository by lazy { FakeAuthRepository() }
    val remoteAuthRepository: AuthRepository by lazy { RemoteAuthRepository(RetrofitProvider.authApi) }

    fun provideAuthRepository(): AuthRepository {
        return if (USE_REMOTE_SOURCE) remoteAuthRepository else authRepository
    }

    fun provideTokenRepository(context: Context): AuthTokenRepository {
        return DataStoreAuthTokenRepository(context)
    }
}
