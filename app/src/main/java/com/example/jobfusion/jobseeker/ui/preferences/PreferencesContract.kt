package com.example.jobfusion.jobseeker.ui.preferences

data class PreferencesUiState(
    val location: String = "",
    val desiredSalary: String = "",
    val yearsExperience: String = "",
    val locationWeightPercent: Int = 30,
    val salaryWeightPercent: Int = 40,
    val experienceWeightPercent: Int = 30,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
    val saveSuccessMessage: String? = null
)

sealed interface PreferencesEvent {
    data class LocationChanged(val value: String) : PreferencesEvent
    data class DesiredSalaryChanged(val value: String) : PreferencesEvent
    data class YearsExperienceChanged(val value: String) : PreferencesEvent
    data class LocationWeightChanged(val percent: Int) : PreferencesEvent
    data class SalaryWeightChanged(val percent: Int) : PreferencesEvent
    data class ExperienceWeightChanged(val percent: Int) : PreferencesEvent
    data object SaveClicked : PreferencesEvent
}
