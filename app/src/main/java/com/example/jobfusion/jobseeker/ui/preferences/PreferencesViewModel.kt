package com.example.jobfusion.jobseeker.ui.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.jobseeker.domain.model.UpdatePreferencesRequest
import com.example.jobfusion.jobseeker.domain.repository.PreferencesRepository
import com.example.jobfusion.jobseeker.domain.usecase.GetJobSeekerPreferencesUseCase
import com.example.jobfusion.jobseeker.domain.usecase.UpdateJobSeekerPreferencesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val getPreferences: GetJobSeekerPreferencesUseCase,
    private val updatePreferences: UpdateJobSeekerPreferencesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PreferencesUiState(isLoading = true))
    val uiState: StateFlow<PreferencesUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun onEvent(event: PreferencesEvent) {
        when (event) {
            is PreferencesEvent.LocationChanged ->
                _uiState.update { it.copy(location = event.value, errorMessage = null, saveSuccessMessage = null) }
            is PreferencesEvent.DesiredSalaryChanged ->
                _uiState.update { it.copy(desiredSalary = event.value.filter { ch -> ch.isDigit() }, errorMessage = null, saveSuccessMessage = null) }
            is PreferencesEvent.YearsExperienceChanged ->
                _uiState.update { it.copy(yearsExperience = event.value.filter { ch -> ch.isDigit() }, errorMessage = null, saveSuccessMessage = null) }
            is PreferencesEvent.LocationWeightChanged ->
                _uiState.update { it.copy(locationWeightPercent = event.percent.coerceIn(0, 100), saveSuccessMessage = null) }
            is PreferencesEvent.SalaryWeightChanged ->
                _uiState.update { it.copy(salaryWeightPercent = event.percent.coerceIn(0, 100), saveSuccessMessage = null) }
            is PreferencesEvent.ExperienceWeightChanged ->
                _uiState.update { it.copy(experienceWeightPercent = event.percent.coerceIn(0, 100), saveSuccessMessage = null) }
            PreferencesEvent.SaveClicked -> save()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val response = getPreferences()) {
                is NetworkResponse.Loading -> Unit
                is NetworkResponse.Success -> {
                    val prefs = response.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            location = prefs.location,
                            desiredSalary = prefs.desiredSalary,
                            yearsExperience = prefs.yearsExperience,
                            locationWeightPercent = prefs.locationWeightPercent,
                            salaryWeightPercent = prefs.salaryWeightPercent,
                            experienceWeightPercent = prefs.experienceWeightPercent,
                            errorMessage = null
                        )
                    }
                }
                is NetworkResponse.Error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = response.message.ifBlank { "Could not load preferences" }
                        )
                    }
            }
        }
    }

    private fun save() {
        val s = _uiState.value
        val request = UpdatePreferencesRequest(
            location = s.location.trim(),
            desiredSalary = s.desiredSalary.trim(),
            yearsExperience = s.yearsExperience.trim(),
            locationWeightPercent = s.locationWeightPercent,
            salaryWeightPercent = s.salaryWeightPercent,
            experienceWeightPercent = s.experienceWeightPercent
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null, saveSuccessMessage = null) }
            when (val response = updatePreferences(request)) {
                is NetworkResponse.Loading -> Unit
                is NetworkResponse.Success ->
                    _uiState.update {
                        it.copy(isSaving = false, saveSuccessMessage = "Preferences saved")
                    }
                is NetworkResponse.Error ->
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessage = response.message.ifBlank { "Could not save preferences" }
                        )
                    }
            }
        }
    }
}

class PreferencesViewModelFactory(
    private val repository: PreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PreferencesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PreferencesViewModel(
                getPreferences = GetJobSeekerPreferencesUseCase(repository),
                updatePreferences = UpdateJobSeekerPreferencesUseCase(repository)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
