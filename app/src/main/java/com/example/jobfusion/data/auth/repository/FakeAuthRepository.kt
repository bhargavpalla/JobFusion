package com.example.jobfusion.data.auth.repository

import com.example.jobfusion.core.dispatcher.DefaultDispatcherProvider
import com.example.jobfusion.core.dispatcher.DispatcherProvider
import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.domain.auth.model.LoginResult
import com.example.jobfusion.domain.auth.model.SignupRequest
import com.example.jobfusion.domain.auth.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FakeAuthRepository(
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : AuthRepository {

    override suspend fun login(email: String, password: String): NetworkResponse<LoginResult> {
        return withContext(dispatcherProvider.io) {
            delay(700)
            val normalized = email.trim().lowercase()
            when {
                normalized == DUMMY_JOB_SEEKER_EMAIL && password == DUMMY_JOB_SEEKER_PASSWORD ->
                    NetworkResponse.Success(LoginResult(accessToken = FAKE_JOB_SEEKER_ACCESS_TOKEN))

                normalized == DUMMY_RECRUITER_EMAIL && password == DUMMY_RECRUITER_PASSWORD ->
                    NetworkResponse.Success(LoginResult(accessToken = FAKE_RECRUITER_ACCESS_TOKEN))

                else -> {
                    val ex = IllegalArgumentException(
                        "Invalid email or password. Dummy job seeker: $DUMMY_JOB_SEEKER_EMAIL / $DUMMY_JOB_SEEKER_PASSWORD, recruiter: $DUMMY_RECRUITER_EMAIL / $DUMMY_RECRUITER_PASSWORD"
                    )
                    NetworkResponse.Error(ex.message ?: "Invalid email or password", ex)
                }
            }
        }
    }

    override suspend fun signup(request: SignupRequest): NetworkResponse<Unit> {
        return withContext(dispatcherProvider.io) {
            delay(900)
            if (request.email.contains("@") && request.password.length >= 6) {
                NetworkResponse.Success(Unit)
            } else {
                val ex = IllegalArgumentException("Please check your signup details")
                NetworkResponse.Error(ex.message ?: "Please check your signup details", ex)
            }
        }
    }

    private companion object {
        /** Fake API accounts available while using [FakeAuthRepository]. */
        const val DUMMY_JOB_SEEKER_EMAIL: String = "movva@gmail.com"
        const val DUMMY_JOB_SEEKER_PASSWORD: String = "Test@123"
        const val DUMMY_RECRUITER_EMAIL: String = "bhargav@gmail.com"
        const val DUMMY_RECRUITER_PASSWORD: String = "test@123"

        /** Placeholder JWT-shaped string for local testing (not a real signature). */
        const val FAKE_JOB_SEEKER_ACCESS_TOKEN: String =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2JzZWVrZXIiLCJtb2RlIjoiZmFrZSJ9.signature"
        const val FAKE_RECRUITER_ACCESS_TOKEN: String =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJyZWNydWl0ZXIiLCJtb2RlIjoiZmFrZSJ9.signature"
    }
}
