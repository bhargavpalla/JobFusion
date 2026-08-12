package com.example.data.auth.mapper

import com.example.data.auth.remote.model.SignupRequestDto
import com.example.domain.auth.model.SignupRequest
import com.example.domain.auth.model.UserRole

fun SignupRequest.toDto(): SignupRequestDto {
    val includeSalary = role == UserRole.JOB_SEEKER
    return SignupRequestDto(
        fullName = fullName,
        email = email,
        phone = phone,
        password = password,
        role = role.name,
        salaryType = if (includeSalary) salaryType.name else null,
        currency = if (includeSalary) currency.name else null,
        period = if (includeSalary) period.name else null,
        minSalary = if (includeSalary) minSalary else null,
        maxSalary = if (includeSalary) maxSalary else null,
        expectedSalary = if (includeSalary) expectedSalary else null,
        additionalDetails = additionalDetails
    )
}
