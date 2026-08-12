package com.example.jobfusion.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.auth.model.Currency
import com.example.domain.auth.model.SalaryPeriod
import com.example.domain.auth.model.SalaryType
import com.example.domain.auth.model.UserRole
import com.example.domain.auth.validation.AuthField
import com.example.jobfusion.ui.auth.components.AuthDimens
import com.example.jobfusion.ui.auth.components.DropdownField
import com.example.jobfusion.ui.auth.components.FooterAction
import com.example.jobfusion.ui.auth.components.InputField
import com.example.jobfusion.ui.auth.components.PasswordField
import com.example.jobfusion.ui.auth.components.PrimaryButton
import com.example.jobfusion.ui.auth.components.SectionTitle
import com.example.jobfusion.ui.auth.components.StatusSection
import com.example.jobfusion.ui.auth.components.ToggleTab
import com.example.jobfusion.ui.theme.JobFusionTheme

@Composable
fun AuthScreen(
    modifier: Modifier = Modifier,
    onAboutClick: () -> Unit = {},
    onJobSeekerAuthenticated: () -> Unit = {},
    onRecruiterAuthenticated: () -> Unit = {}
) {
    val appContext = LocalContext.current.applicationContext
    val factory = remember(appContext) {
        AuthViewModelFactory(
            AuthDependencies.provideAuthRepository(),
            AuthDependencies.provideTokenRepository(appContext)
        )
    }
    val viewModel: AuthViewModel = viewModel(factory = factory)
    val state by viewModel.uiState.collectAsState()
    AuthContent(
        state = state,
        onEvent = viewModel::onEvent,
        onAboutClick = onAboutClick,
        onJobSeekerAuthenticated = onJobSeekerAuthenticated,
        onRecruiterAuthenticated = onRecruiterAuthenticated,
        modifier = modifier
    )
}

@Composable
private fun AuthContent(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onAboutClick: () -> Unit,
    onJobSeekerAuthenticated: () -> Unit,
    onRecruiterAuthenticated: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (state.sessionRole) {
        UserRole.JOB_SEEKER -> {
            LaunchedEffect(Unit) {
                onJobSeekerAuthenticated()
            }
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        UserRole.RECRUITER -> {
            LaunchedEffect(Unit) {
                onRecruiterAuthenticated()
            }
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        null -> {
            AuthScrollableBody(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                state = state,
                onEvent = onEvent,
                onAboutClick = onAboutClick
            )
        }
    }
}

@Composable
private fun AuthScrollableBody(
    modifier: Modifier,
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit,
    onAboutClick: () -> Unit
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(AuthDimens.ScreenPadding),
        verticalArrangement = Arrangement.spacedBy(AuthDimens.SectionSpacing)
    ) {
        ToggleTab(
            options = listOf("Log in", "Create account"),
            selectedIndex = if (state.isLoginMode) 0 else 1,
            onSelected = { onEvent(AuthEvent.ToggleAuthMode(isLogin = it == 0)) }
        )

        if (state.isLoginMode) {
            LoginSection(
                email = state.email,
                password = state.password,
                isLoading = state.isLoading,
                errorMessage = state.errorMessage,
                successMessage = state.successMessage,
                fieldErrors = state.fieldErrors,
                onAboutClick = onAboutClick,
                onEvent = onEvent
            )
        } else {
            SignupSection(state = state, onEvent = onEvent)
        }
    }
}

@Composable
private fun LoginSection(
    email: String,
    password: String,
    isLoading: Boolean,
    errorMessage: String?,
    successMessage: String?,
    fieldErrors: Map<AuthField, String>,
    onAboutClick: () -> Unit,
    onEvent: (AuthEvent) -> Unit
) {
    SectionTitle(
        title = "Welcome back",
        subtitle = "Log in to continue to your JobFusion workspace."
    )
    InputField(
        label = "Email",
        value = email,
        placeholder = "you@company.com",
        onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
        keyboardType = KeyboardType.Email,
        errorText = fieldErrors[AuthField.EMAIL]
    )
    PasswordField(
        label = "Password",
        value = password,
        onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
        errorText = fieldErrors[AuthField.PASSWORD]
    )
    StatusSection(errorMessage = errorMessage, successMessage = successMessage)
    PrimaryButton(
        text = if (isLoading) "Loading..." else "Log in",
        enabled = !isLoading,
        onClick = { onEvent(AuthEvent.SubmitLogin) }
    )
    FooterAction(
        prefix = "New here?",
        action = "Create an account",
        onClick = { onEvent(AuthEvent.ToggleAuthMode(isLogin = false)) }
    )
    AboutBottomSection(onAboutClick = onAboutClick)
}

@Composable
private fun AboutBottomSection(onAboutClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFF), RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFFDCE3EE), RoundedCornerShape(14.dp))
            .clickable { onAboutClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = "About JobFusion",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1E459B)
        )
        Text(
            text = "See how our AI-powered hiring platform works.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF4B5563)
        )
    }
}

@Composable
private fun SignupSection(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit
) {
    SectionTitle(
        title = "Create your account",
        subtitle = "A few details and you're in - it takes less than a minute."
    )
    ToggleTab(
        title = "I am a",
        options = listOf("Job Seeker", "Recruiter"),
        selectedIndex = if (state.selectedRole == UserRole.JOB_SEEKER) 0 else 1,
        onSelected = {
            onEvent(
                AuthEvent.RoleSelected(
                    if (it == 0) UserRole.JOB_SEEKER else UserRole.RECRUITER
                )
            )
        }
    )

    InputField(
        label = "Full name",
        value = state.fullName,
        placeholder = "Jane Doe",
        onValueChange = { onEvent(AuthEvent.FullNameChanged(it)) },
        errorText = state.fieldErrors[AuthField.FULL_NAME]
    )

    InputField(
        label = "Email",
        value = state.email,
        placeholder = "you@company.com",
        onValueChange = { onEvent(AuthEvent.EmailChanged(it)) },
        keyboardType = KeyboardType.Email,
        errorText = state.fieldErrors[AuthField.EMAIL]
    )

    InputField(
        label = "Phone",
        value = state.phone,
        placeholder = "(123) 456-7890",
        onValueChange = { onEvent(AuthEvent.PhoneChanged(it)) },
        keyboardType = KeyboardType.Phone,
        errorText = state.fieldErrors[AuthField.PHONE]
    )

    PasswordField(
        label = "Password",
        value = state.password,
        onValueChange = { onEvent(AuthEvent.PasswordChanged(it)) },
        errorText = state.fieldErrors[AuthField.PASSWORD]
    )

    PasswordField(
        label = "Retype password",
        value = state.retypePassword,
        onValueChange = { onEvent(AuthEvent.RetypePasswordChanged(it)) },
        errorText = state.fieldErrors[AuthField.RETYPE_PASSWORD]
    )

    if (state.selectedRole == UserRole.JOB_SEEKER) {
        SalarySection(state = state, onEvent = onEvent)
    }

    InputField(
        label = "Additional details (optional)",
        value = state.additionalDetails,
        placeholder = "Tell us a bit about yourself or your company...",
        onValueChange = { onEvent(AuthEvent.AdditionalDetailsChanged(it)) },
        singleLine = false,
        minLines = 4
    )

    StatusSection(errorMessage = state.errorMessage, successMessage = state.successMessage)
    PrimaryButton(
        text = if (state.isLoading) "Loading..." else "Create account",
        enabled = !state.isLoading,
        onClick = { onEvent(AuthEvent.SubmitSignup) }
    )
    FooterAction(
        prefix = "Already have an account?",
        action = "Log in",
        onClick = { onEvent(AuthEvent.ToggleAuthMode(isLogin = true)) }
    )
}

@Composable
private fun SalarySection(
    state: AuthUiState,
    onEvent: (AuthEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F9FC), RoundedCornerShape(16.dp))
            .border(width = 1.dp, color = Color(0xFFDCE3EE), shape = RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(AuthDimens.FieldSpacing)
    ) {
        SectionTitle(title = "SALARY EXPECTATION", subtitle = "")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(AuthDimens.FieldSpacing)
        ) {
            DropdownField(
                label = "Type",
                options = SalaryType.entries,
                selectedValue = state.salaryType,
                optionLabel = {
                    when (it) {
                        SalaryType.RANGE -> "Range"
                        SalaryType.FIXED -> "Fixed"
                        SalaryType.NEGOTIABLE -> "Negotiable"
                    }
                },
                onSelected = { onEvent(AuthEvent.SalaryTypeChanged(it)) },
                modifier = Modifier.weight(1f)
            )
            DropdownField(
                label = "Currency",
                options = Currency.entries,
                selectedValue = state.currency,
                optionLabel = { it.label },
                onSelected = { onEvent(AuthEvent.CurrencyChanged(it)) },
                modifier = Modifier.weight(1f)
            )
            DropdownField(
                label = "Period",
                options = SalaryPeriod.entries,
                selectedValue = state.period,
                optionLabel = { it.label },
                onSelected = { onEvent(AuthEvent.SalaryPeriodChanged(it)) },
                modifier = Modifier.weight(1f)
            )
        }

        when (state.salaryType) {
            SalaryType.RANGE -> Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(AuthDimens.FieldSpacing)
            ) {
                InputField(
                    label = "Min salary",
                    value = state.minSalary,
                    placeholder = "e.g. 80000",
                    onValueChange = { onEvent(AuthEvent.MinSalaryChanged(it)) },
                    keyboardType = KeyboardType.Number,
                    errorText = state.fieldErrors[AuthField.MIN_SALARY],
                    modifier = Modifier.weight(1f)
                )
                InputField(
                    label = "Max salary",
                    value = state.maxSalary,
                    placeholder = "e.g. 150000",
                    onValueChange = { onEvent(AuthEvent.MaxSalaryChanged(it)) },
                    keyboardType = KeyboardType.Number,
                    errorText = state.fieldErrors[AuthField.MAX_SALARY],
                    modifier = Modifier.weight(1f)
                )
            }

            SalaryType.FIXED -> InputField(
                label = "Expected salary",
                value = state.expectedSalary,
                placeholder = "e.g. 120000",
                onValueChange = { onEvent(AuthEvent.ExpectedSalaryChanged(it)) },
                keyboardType = KeyboardType.Number,
                errorText = state.fieldErrors[AuthField.EXPECTED_SALARY]
            )

            SalaryType.NEGOTIABLE -> Unit
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthScreenLoginPreview() {
    JobFusionTheme(dynamicColor = false) {
        AuthContent(
            state = AuthUiState(isLoginMode = true),
            onEvent = {},
            onAboutClick = {},
            onJobSeekerAuthenticated = {},
            onRecruiterAuthenticated = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AuthScreenSignupPreview() {
    JobFusionTheme(dynamicColor = false) {
        AuthContent(
            state = AuthUiState(isLoginMode = false),
            onEvent = {},
            onAboutClick = {},
            onJobSeekerAuthenticated = {},
            onRecruiterAuthenticated = {}
        )
    }
}
