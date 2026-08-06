package com.example.jobfusion.data.auth.remote.model

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class LoginResponseDto(
    @SerializedName("access_token") val accessToken: String
)

data class SignupRequestDto(
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: String,
    val salaryType: String?,
    val currency: String?,
    val period: String?,
    val minSalary: Int?,
    val maxSalary: Int?,
    val expectedSalary: Int?,
    val additionalDetails: String?
)
