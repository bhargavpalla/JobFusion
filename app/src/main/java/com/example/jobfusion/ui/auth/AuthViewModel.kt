package com.example.jobfusion.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.domain.auth.model.SalaryType
import com.example.jobfusion.domain.auth.model.SignupRequest
import com.example.jobfusion.domain.auth.model.UserRole
import com.example.jobfusion.domain.auth.repository.AuthRepository
import com.example.jobfusion.domain.auth.repository.AuthTokenRepository
import com.example.jobfusion.domain.auth.usecase.LoginUseCase
import com.example.jobfusion.domain.auth.usecase.SignupUseCase
import com.example.jobfusion.domain.auth.validation.AuthValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase,
    private val validator: AuthValidator,
    private val authTokenRepository: AuthTokenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val session = authTokenRepository.getCurrentSession()
            if (session?.role == UserRole.RECRUITER) {
                updateState { copy(sessionRole = UserRole.RECRUITER) }
            }
        }
    }

    fun onEvent(event: AuthEvent) {
        when (event) {
            is AuthEvent.ToggleAuthMode -> updateState {
                copy(
                    isLoginMode = event.isLogin,
                    fieldErrors = emptyMap(),
                    errorMessage = null,
                    successMessage = null,
                    sessionRole = null
                )
            }
            is AuthEvent.RoleSelected -> updateState {
                if (event.role == UserRole.RECRUITER) {
                    copy(
                        selectedRole = event.role,
                        minSalary = "",
                        maxSalary = "",
                        expectedSalary = "",
                        fieldErrors = fieldErrors
                            .minus(AuthField.MIN_SALARY)
                            .minus(AuthField.MAX_SALARY)
                            .minus(AuthField.EXPECTED_SALARY)
                    )
                } else {
                    copy(selectedRole = event.role)
                }
            }
            is AuthEvent.FullNameChanged -> updateState { copy(fullName = event.value, fieldErrors = fieldErrors.minus(AuthField.FULL_NAME)) }
            is AuthEvent.EmailChanged -> updateState { copy(email = event.value, fieldErrors = fieldErrors.minus(AuthField.EMAIL)) }
            is AuthEvent.PhoneChanged -> updateState { copy(phone = event.value, fieldErrors = fieldErrors.minus(AuthField.PHONE)) }
            is AuthEvent.PasswordChanged -> updateState {
                copy(
                    password = event.value,
                    fieldErrors = fieldErrors.minus(AuthField.PASSWORD).minus(AuthField.RETYPE_PASSWORD)
                )
            }
            is AuthEvent.RetypePasswordChanged -> updateState { copy(retypePassword = event.value, fieldErrors = fieldErrors.minus(AuthField.RETYPE_PASSWORD)) }
            is AuthEvent.SalaryTypeChanged -> updateState {
                copy(
                    salaryType = event.value,
                    minSalary = "",
                    maxSalary = "",
                    expectedSalary = "",
                    fieldErrors = fieldErrors
                        .minus(AuthField.MIN_SALARY)
                        .minus(AuthField.MAX_SALARY)
                        .minus(AuthField.EXPECTED_SALARY)
                )
            }
            is AuthEvent.CurrencyChanged -> updateState { copy(currency = event.value) }
            is AuthEvent.SalaryPeriodChanged -> updateState { copy(period = event.value) }
            is AuthEvent.MinSalaryChanged -> updateState { copy(minSalary = event.value.filter { it.isDigit() }, fieldErrors = fieldErrors.minus(AuthField.MIN_SALARY)) }
            is AuthEvent.MaxSalaryChanged -> updateState { copy(maxSalary = event.value.filter { it.isDigit() }, fieldErrors = fieldErrors.minus(AuthField.MAX_SALARY)) }
            is AuthEvent.ExpectedSalaryChanged -> updateState { copy(expectedSalary = event.value.filter { it.isDigit() }, fieldErrors = fieldErrors.minus(AuthField.EXPECTED_SALARY)) }
            is AuthEvent.AdditionalDetailsChanged -> updateState { copy(additionalDetails = event.value) }
            AuthEvent.SubmitLogin -> submitLogin()
            AuthEvent.SubmitSignup -> submitSignup()
            AuthEvent.ClearStatus -> updateState { copy(errorMessage = null, successMessage = null, fieldErrors = emptyMap()) }
            AuthEvent.SignOut -> {
                viewModelScope.launch {
                    authTokenRepository.clear()
                    updateState {
                        copy(
                            sessionRole = null,
                            successMessage = null,
                            errorMessage = null,
                            fieldErrors = emptyMap()
                        )
                    }
                }
            }
        }
    }

    private fun submitLogin() {
        val state = _uiState.value
        val validationResult = validator.validateLogin(
            email = state.email.trim(),
            password = state.password
        )
        if (!validationResult.isValid) {
            updateState {
                copy(
                    fieldErrors = validationResult.fieldErrors,
                    errorMessage = validationResult.firstError,
                    successMessage = null
                )
            }
            return
        }

        viewModelScope.launch {
            updateState { copy(isLoading = true, fieldErrors = emptyMap(), errorMessage = null, successMessage = null) }
            when (val response = loginUseCase(email = state.email.trim(), password = state.password)) {
                is NetworkResponse.Loading -> Unit
                is NetworkResponse.Success -> {
                    val loggedInRole = resolveLoginRole(state.email)
                    authTokenRepository.saveSession(
                        accessToken = response.data.accessToken,
                        role = loggedInRole
                    )
                    updateState {
                        copy(
                            isLoading = false,
                            successMessage = "Login successful",
                            errorMessage = null,
                            sessionRole = loggedInRole
                        )
                    }
                }
                is NetworkResponse.Error -> {
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = response.message.ifBlank { "Unable to login" },
                            successMessage = null
                        )
                    }
                }
            }
        }
    }

    private fun submitSignup() {
        val state = _uiState.value
        val validationResult = validator.validateSignup(
            AuthValidator.SignupValidationInput(
                fullName = state.fullName.trim(),
                email = state.email.trim(),
                phone = state.phone.trim(),
                password = state.password,
                retypePassword = state.retypePassword,
                selectedRole = state.selectedRole,
                salaryType = state.salaryType,
                minSalary = state.minSalary,
                maxSalary = state.maxSalary,
                expectedSalary = state.expectedSalary
            )
        )
        if (!validationResult.isValid) {
            updateState {
                copy(
                    fieldErrors = validationResult.fieldErrors,
                    errorMessage = validationResult.firstError,
                    successMessage = null
                )
            }
            return
        }

        val request = SignupRequest(
            fullName = state.fullName.trim(),
            email = state.email.trim(),
            phone = state.phone.trim(),
            password = state.password,
            role = state.selectedRole,
            salaryType = state.salaryType,
            currency = state.currency,
            period = state.period,
            minSalary = state.minSalary.toIntOrNull(),
            maxSalary = state.maxSalary.toIntOrNull(),
            expectedSalary = state.expectedSalary.toIntOrNull(),
            additionalDetails = state.additionalDetails.trim().ifBlank { null }
        )

        viewModelScope.launch {
            updateState { copy(isLoading = true, fieldErrors = emptyMap(), errorMessage = null, successMessage = null) }
            when (val response = signupUseCase(request)) {
                is NetworkResponse.Loading -> Unit
                is NetworkResponse.Success -> {
                    authTokenRepository.saveSession(
                        accessToken = localSignupSessionToken(request.email),
                        role = state.selectedRole
                    )
                    updateState {
                        copy(
                            isLoading = false,
                            successMessage = "Account created successfully",
                            errorMessage = null,
                            sessionRole = state.selectedRole
                        )
                    }
                }
                is NetworkResponse.Error -> {
                    updateState {
                        copy(
                            isLoading = false,
                            errorMessage = response.message.ifBlank { "Unable to create account" },
                            successMessage = null
                        )
                    }
                }
            }
        }
    }

    private fun updateState(reducer: AuthUiState.() -> AuthUiState) {
        _uiState.update(reducer)
    }

    private companion object {
        private const val DUMMY_RECRUITER_EMAIL = "bhargav@gmail.com"

        fun resolveLoginRole(email: String): UserRole {
            return if (email.trim().lowercase() == DUMMY_RECRUITER_EMAIL) {
                UserRole.RECRUITER
            } else {
                UserRole.JOB_SEEKER
            }
        }

        /** Placeholder until signup API returns a real access token. */
        fun localSignupSessionToken(email: String): String =
            "local-signup-${email.trim().lowercase().hashCode()}"
    }
}

class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val authTokenRepository: AuthTokenRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(
                loginUseCase = LoginUseCase(repository),
                signupUseCase = SignupUseCase(repository),
                validator = AuthValidator(),
                authTokenRepository = authTokenRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
