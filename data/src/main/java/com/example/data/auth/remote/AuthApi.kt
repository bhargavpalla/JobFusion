package com.example.data.auth.remote

import com.example.data.auth.remote.model.LoginRequestDto
import com.example.data.auth.remote.model.LoginResponseDto
import com.example.data.auth.remote.model.SignupRequestDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequestDto)
}
