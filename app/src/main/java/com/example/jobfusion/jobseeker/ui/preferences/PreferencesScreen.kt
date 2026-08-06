package com.example.jobfusion.jobseeker.ui.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobfusion.jobseeker.ui.preferences.components.PreferenceInputCard
import com.example.jobfusion.jobseeker.ui.preferences.components.WeightSlider
import com.example.jobfusion.ui.theme.JobFusionTheme

@Composable
fun JobSeekerPreferencesRoute(
    modifier: Modifier = Modifier,
    viewModel: PreferencesViewModel = viewModel(
        factory = PreferencesViewModelFactory(PreferencesDependencies.provideRepository())
    )
) {
    val state by viewModel.uiState.collectAsState()
    JobSeekerPreferencesBody(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun JobSeekerPreferencesBody(
    state: PreferencesUiState,
    onEvent: (PreferencesEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5FA))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Your preferences",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        PreferenceInputCard(
            label = "Location",
            value = state.location,
            onValueChange = { onEvent(PreferencesEvent.LocationChanged(it)) },
            leadingIcon = Icons.Outlined.LocationOn
        )
        PreferenceInputCard(
            label = "Desired salary",
            value = state.desiredSalary,
            onValueChange = { onEvent(PreferencesEvent.DesiredSalaryChanged(it)) },
            leadingIcon = Icons.Outlined.AttachMoney
        )
        PreferenceInputCard(
            label = "Years of experience",
            value = state.yearsExperience,
            onValueChange = { onEvent(PreferencesEvent.YearsExperienceChanged(it)) },
            leadingIcon = Icons.Outlined.Work
        )

        HorizontalDivider(color = Color(0xFFE5E7EB))

        Text(
            text = "Match weights",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "Higher values prioritize that signal in recommendations.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF667085)
        )

        WeightSlider(
            label = "Location",
            percent = state.locationWeightPercent,
            onPercentChange = { onEvent(PreferencesEvent.LocationWeightChanged(it)) }
        )
        WeightSlider(
            label = "Salary",
            percent = state.salaryWeightPercent,
            onPercentChange = { onEvent(PreferencesEvent.SalaryWeightChanged(it)) }
        )
        WeightSlider(
            label = "Experience",
            percent = state.experienceWeightPercent,
            onPercentChange = { onEvent(PreferencesEvent.ExperienceWeightChanged(it)) }
        )

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (state.saveSuccessMessage != null) {
            Text(
                text = state.saveSuccessMessage.orEmpty(),
                color = Color(0xFF1A7F37),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        TextButton(
            onClick = { onEvent(PreferencesEvent.SaveClicked) },
            enabled = !state.isLoading && !state.isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (state.isSaving) "Saving…" else "Save")
        }

        if (state.isLoading) {
            Text(
                text = "Loading…",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF667085)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun JobSeekerPreferencesBodyPreview() {
    JobFusionTheme(dynamicColor = false) {
        JobSeekerPreferencesBody(
            state = PreferencesUiState(
                location = "Berlin, Germany",
                desiredSalary = "220000",
                yearsExperience = "3",
                locationWeightPercent = 30,
                salaryWeightPercent = 40,
                experienceWeightPercent = 30,
                isLoading = false
            ),
            onEvent = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
