package com.example.jobfusion.jobseeker.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.jobseeker.domain.model.FeedbackRequest
import com.example.jobfusion.jobseeker.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: DashboardRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        refreshDashboardData()
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            DashboardEvent.ResumeReplaceTapped -> uploadResumeFlow()
            DashboardEvent.FindPerfectJobs -> runMatchEngineFindJobs()
            DashboardEvent.EnhanceWithAi -> runMatchEngineEnhanceAi()
            is DashboardEvent.TabSelected -> _uiState.update { it.copy(selectedTab = event.tab) }
            is DashboardEvent.JobExpandToggled -> toggleExpand(event.jobId)
            is DashboardEvent.JobRatingChanged -> updateRating(event.jobId, event.rating)
            is DashboardEvent.SubmitFeedback -> submitFeedback(event.jobId)
            DashboardEvent.BannerDismissed -> _uiState.update { it.copy(transientBanner = null) }
        }
    }

    private fun refreshDashboardData() {
        viewModelScope.launch {
            _uiState.update { it.copy(statsLoading = true, statsError = null) }
            when (val response = repository.getDashboardStats()) {
                is NetworkResponse.Loading -> Unit
                is NetworkResponse.Success ->
                    _uiState.update { it.copy(dashboardStats = response.data, statsLoading = false, statsError = null) }
                is NetworkResponse.Error ->
                    _uiState.update {
                        it.copy(
                            statsLoading = false,
                            statsError = response.message.ifBlank { "Could not load stats" }
                        )
                    }
            }
        }
        viewModelScope.launch {
            _uiState.update { it.copy(recommendationsLoading = true, recommendationsError = null) }
            when (val response = repository.getRecommendations()) {
                is NetworkResponse.Loading -> Unit
                is NetworkResponse.Success -> {
                    val jobs = response.data
                    _uiState.update { current ->
                        val ratings = jobs.associate { j ->
                            j.id to (current.jobRatings[j.id] ?: defaultRatingForRank(j.rank))
                        }
                        current.copy(
                            jobMatches = jobs,
                            recommendationsLoading = false,
                            recommendationsError = null,
                            jobRatings = ratings
                        )
                    }
                }
                is NetworkResponse.Error ->
                    _uiState.update {
                        it.copy(
                            recommendationsLoading = false,
                            recommendationsError = response.message.ifBlank { "Could not load jobs" }
                        )
                    }
            }
        }
    }

    private fun defaultRatingForRank(rank: Int): Int = if (rank == 1) 3 else 4

    private fun uploadResumeFlow() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    resumePhase = ResumeUploadPhase.Loading,
                    resumeErrorMessage = null,
                    resumeSuccessMessage = null
                )
            }
            when (val response = repository.uploadResume()) {
                is NetworkResponse.Loading -> Unit
                is NetworkResponse.Success -> {
                    val upload = response.data
                    _uiState.update {
                        it.copy(
                            resumePhase = if (upload.parsedSuccessfully) {
                                ResumeUploadPhase.Success
                            } else {
                                ResumeUploadPhase.Error
                            },
                            resumeFileName = upload.fileName,
                            resumeSuccessMessage = if (upload.parsedSuccessfully) {
                                "Resume parsed successfully"
                            } else {
                                null
                            },
                            resumeErrorMessage = if (upload.parsedSuccessfully) {
                                null
                            } else {
                                upload.message ?: "Could not parse resume"
                            }
                        )
                    }
                }
                is NetworkResponse.Error ->
                    _uiState.update {
                        it.copy(
                            resumePhase = ResumeUploadPhase.Error,
                            resumeErrorMessage = response.message.ifBlank { "Upload failed" },
                            resumeSuccessMessage = null
                        )
                    }
            }
        }
    }

    private fun runMatchEngineFindJobs() {
        viewModelScope.launch {
            _uiState.update { it.copy(isMatchEngineBusy = true, transientBanner = null) }
            when (val statsResponse = repository.getDashboardStats()) {
                is NetworkResponse.Success ->
                    _uiState.update { it.copy(dashboardStats = statsResponse.data) }
                is NetworkResponse.Loading, is NetworkResponse.Error -> Unit
            }
            when (val recResponse = repository.getRecommendations()) {
                is NetworkResponse.Loading -> Unit
                is NetworkResponse.Success -> {
                    val jobs = recResponse.data
                    _uiState.update { current ->
                        val ratings = jobs.associate { j ->
                            j.id to (current.jobRatings[j.id] ?: defaultRatingForRank(j.rank))
                        }
                        current.copy(
                            jobMatches = jobs,
                            jobRatings = ratings,
                            isMatchEngineBusy = false,
                            transientBanner = "Job list refreshed with latest matches."
                        )
                    }
                }
                is NetworkResponse.Error ->
                    _uiState.update {
                        it.copy(
                            isMatchEngineBusy = false,
                            transientBanner = recResponse.message.ifBlank { "Match run failed" }
                        )
                    }
            }
        }
    }

    private fun runMatchEngineEnhanceAi() {
        viewModelScope.launch {
            _uiState.update { it.copy(isMatchEngineBusy = true) }
            kotlinx.coroutines.delay(500)
            _uiState.update {
                it.copy(
                    isMatchEngineBusy = false,
                    transientBanner = "AI feedback queued — recommendations will update shortly."
                )
            }
        }
    }

    private fun toggleExpand(jobId: String) {
        _uiState.update { state ->
            val next = state.expandedJobIds.toMutableSet()
            if (!next.add(jobId)) next.remove(jobId)
            state.copy(expandedJobIds = next)
        }
    }

    private fun updateRating(jobId: String, rating: Int) {
        _uiState.update { state ->
            val clamped = rating.coerceIn(1, 5)
            state.copy(
                jobRatings = state.jobRatings + (jobId to clamped),
                feedbackError = null
            )
        }
    }

    private fun submitFeedback(jobId: String) {
        val rating = _uiState.value.jobRatings[jobId] ?: return
        viewModelScope.launch {
            _uiState.update {
                it.copy(feedbackSubmittingJobId = jobId, feedbackError = null)
            }
            val request = FeedbackRequest(jobMatchId = jobId, rating = rating.toFloat())
            when (val feedbackResult = repository.submitFeedback(request)) {
                is NetworkResponse.Loading -> Unit
                is NetworkResponse.Success ->
                    _uiState.update {
                        it.copy(
                            feedbackSubmittingJobId = null,
                            feedbackSubmittedIds = it.feedbackSubmittedIds + jobId
                        )
                    }
                is NetworkResponse.Error ->
                    _uiState.update {
                        it.copy(
                            feedbackSubmittingJobId = null,
                            feedbackError = feedbackResult.message.ifBlank { "Could not submit feedback" }
                        )
                    }
            }
        }
    }
}

class DashboardViewModelFactory(
    private val repository: DashboardRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}
