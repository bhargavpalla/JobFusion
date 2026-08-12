package com.example.domain.auth.model

enum class UserRole {
    JOB_SEEKER,
    RECRUITER
}

enum class SalaryType {
    RANGE,
    FIXED,
    NEGOTIABLE
}

enum class SalaryPeriod(val label: String) {
    PER_YEAR("Per Year"),
    PER_MONTH("Per Month"),
    PER_HOUR("Per Hour")
}

enum class Currency(val label: String) {
    INR("INR"),
    USD("USD"),
    EUR("EUR")
}

data class LoginRequest(
    val email: String,
    val password: String
)

data class SignupRequest(
    val fullName: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: UserRole,
    val salaryType: SalaryType,
    val currency: Currency,
    val period: SalaryPeriod,
    val minSalary: Int?,
    val maxSalary: Int?,
    val expectedSalary: Int?,
    val additionalDetails: String?
)
