package com.example.jobfusion.ui.auth

import com.example.domain.auth.model.Currency
import com.example.domain.auth.model.SalaryPeriod
import com.example.domain.auth.model.SalaryType
import com.example.domain.auth.model.UserRole
import com.example.domain.auth.validation.AuthField


data class AuthUiState(
    val isLoginMode: Boolean = true,
    /** When non-null, user has an active session (used for menu / navigation). */
    val sessionRole: UserRole? = null,
    val selectedRole: UserRole = UserRole.JOB_SEEKER,
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val retypePassword: String = "",
    val salaryType: SalaryType = SalaryType.RANGE,
    val currency: Currency = Currency.INR,
    val period: SalaryPeriod = SalaryPeriod.PER_YEAR,
    val minSalary: String = "",
    val maxSalary: String = "",
    val expectedSalary: String = "",
    val additionalDetails: String = "",
    val isLoading: Boolean = false,
    val fieldErrors: Map<AuthField, String> = emptyMap(),
    val errorMessage: String? = null,
    val successMessage: String? = null
)

sealed interface AuthEvent {
    data class ToggleAuthMode(val isLogin: Boolean) : AuthEvent
    data class RoleSelected(val role: UserRole) : AuthEvent
    data class FullNameChanged(val value: String) : AuthEvent
    data class EmailChanged(val value: String) : AuthEvent
    data class PhoneChanged(val value: String) : AuthEvent
    data class PasswordChanged(val value: String) : AuthEvent
    data class RetypePasswordChanged(val value: String) : AuthEvent
    data class SalaryTypeChanged(val value: SalaryType) : AuthEvent
    data class CurrencyChanged(val value: Currency) : AuthEvent
    data class SalaryPeriodChanged(val value: SalaryPeriod) : AuthEvent
    data class MinSalaryChanged(val value: String) : AuthEvent
    data class MaxSalaryChanged(val value: String) : AuthEvent
    data class ExpectedSalaryChanged(val value: String) : AuthEvent
    data class AdditionalDetailsChanged(val value: String) : AuthEvent
    data object SubmitLogin : AuthEvent
    data object SubmitSignup : AuthEvent
    data object ClearStatus : AuthEvent
    data object SignOut : AuthEvent
}
