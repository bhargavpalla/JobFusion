package com.example.jobfusion.recruiter.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.recruiter.domain.model.RecruiterControls
import com.example.jobfusion.recruiter.domain.repository.RecruiterDashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecruiterDashboardViewModel(
    private val repository: RecruiterDashboardRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecruiterDashboardUiState())
    val uiState: StateFlow<RecruiterDashboardUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun onEvent(event: RecruiterDashboardEvent) {
        when (event) {
            is RecruiterDashboardEvent.JobDescriptionChanged ->
                _uiState.update { it.copy(controls = it.controls.copy(jobDescriptionText = event.value)) }
            RecruiterDashboardEvent.UploadJdTapped ->
                _uiState.update {
                    it.copy(
                        controls = it.controls.copy(uploadedJdFileName = "Senior_SWE_JD.pdf"),
                        uploadStatusText = "PDF uploaded successfully"
                    )
                }
            is RecruiterDashboardEvent.TopKChanged ->
                _uiState.update { it.copy(controls = it.controls.copy(topK = event.value.coerceAtLeast(1))) }
            is RecruiterDashboardEvent.MaxSalaryChanged ->
                _uiState.update { it.copy(controls = it.controls.copy(maxSalary = event.value.filter(Char::isDigit))) }
            is RecruiterDashboardEvent.MinExperienceChanged ->
                _uiState.update { it.copy(controls = it.controls.copy(minExperienceYears = event.value.filter(Char::isDigit))) }
            is RecruiterDashboardEvent.LocationChanged ->
                _uiState.update { it.copy(controls = it.controls.copy(location = event.value)) }
            is RecruiterDashboardEvent.CandidateSelected ->
                _uiState.update { state ->
                    val toggled =
                        if (state.selectedCandidate?.candidateId == event.candidate.candidateId) {
                            null
                        } else {
                            event.candidate
                        }
                    state.copy(selectedCandidate = toggled)
                }
            RecruiterDashboardEvent.RunRanking -> runRanking()
        }
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val response = repository.getDashboardData()) {
                is NetworkResponse.Loading -> Unit
                is NetworkResponse.Success -> {
                    val data = response.data
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            controls = data.controls,
                            stats = data.stats,
                            rankings = data.rankedCandidates,
                            selectedCandidate = null
                        )
                    }
                }
                is NetworkResponse.Error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = response.message.ifBlank { "Unable to load recruiter dashboard" }
                        )
                    }
            }
        }
    }

    private fun runRanking() {
        val controls = _uiState.value.controls
        viewModelScope.launch {
            _uiState.update { it.copy(isRunningRanking = true, errorMessage = null) }
            when (val response = repository.runRanking(controls)) {
                is NetworkResponse.Loading -> Unit
                is NetworkResponse.Success ->
                    _uiState.update {
                        it.copy(
                            isRunningRanking = false,
                            rankings = response.data,
                            selectedCandidate = null
                        )
                    }
                is NetworkResponse.Error ->
                    _uiState.update {
                        it.copy(
                            isRunningRanking = false,
                            errorMessage = response.message.ifBlank { "Unable to run ranking" }
                        )
                    }
            }
        }
    }
}

class RecruiterDashboardViewModelFactory(
    private val repository: RecruiterDashboardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecruiterDashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecruiterDashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
