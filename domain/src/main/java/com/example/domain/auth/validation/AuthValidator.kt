package com.example.domain.auth.validation

import com.example.domain.auth.model.SalaryType
import com.example.domain.auth.model.UserRole

class AuthValidator {
    data class ValidationResult(
        val fieldErrors: Map<AuthField, String>
    ) {
        val firstError: String? get() = fieldErrors.values.firstOrNull()
        val isValid: Boolean get() = fieldErrors.isEmpty()
    }

    data class SignupValidationInput(
        val fullName: String,
        val email: String,
        val phone: String,
        val password: String,
        val retypePassword: String,
        val selectedRole: UserRole,
        val salaryType: SalaryType,
        val minSalary: String,
        val maxSalary: String,
        val expectedSalary: String
    )

    fun validateLogin(email: String, password: String): ValidationResult {
        val errors = linkedMapOf<AuthField, String>()
        if (!email.contains("@")) errors[AuthField.EMAIL] = "Enter a valid email"
        if (password.length < 6) errors[AuthField.PASSWORD] = "Password must be at least 6 characters"
        return ValidationResult(fieldErrors = errors)
    }

    fun validateSignup(input: SignupValidationInput): ValidationResult {
        val errors = linkedMapOf<AuthField, String>()
        if (input.fullName.isBlank()) errors[AuthField.FULL_NAME] = "Full name is required"
        if (!input.email.contains("@")) errors[AuthField.EMAIL] = "Enter a valid email"
        if (input.phone.length < 10) errors[AuthField.PHONE] = "Enter a valid phone number"
        if (input.password.length < 6) errors[AuthField.PASSWORD] = "Password must be at least 6 characters"
        if (input.password != input.retypePassword) errors[AuthField.RETYPE_PASSWORD] = "Passwords do not match"

        if (input.selectedRole == UserRole.RECRUITER) return ValidationResult(fieldErrors = errors)

        when (input.salaryType) {
            SalaryType.RANGE -> {
                if (input.minSalary.isBlank()) errors[AuthField.MIN_SALARY] = "Enter min salary"
                if (input.maxSalary.isBlank()) errors[AuthField.MAX_SALARY] = "Enter max salary"
            }
            SalaryType.FIXED -> {
                if (input.expectedSalary.isBlank()) errors[AuthField.EXPECTED_SALARY] = "Enter expected salary"
            }
            SalaryType.NEGOTIABLE -> Unit
        }
        return ValidationResult(fieldErrors = errors)
    }
}


enum class AuthField {
    FULL_NAME,
    EMAIL,
    PHONE,
    PASSWORD,
    RETYPE_PASSWORD,
    MIN_SALARY,
    MAX_SALARY,
    EXPECTED_SALARY
}
