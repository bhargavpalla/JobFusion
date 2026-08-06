package com.example.jobfusion.recruiter.ui.dashboard

import com.example.jobfusion.recruiter.domain.model.CandidateRankingItem
import com.example.jobfusion.recruiter.domain.model.RecruiterControls
import com.example.jobfusion.recruiter.domain.model.RecruiterDashboardStats

data class RecruiterDashboardUiState(
    val controls: RecruiterControls = RecruiterControls(),
    val stats: RecruiterDashboardStats = RecruiterDashboardStats(
        candidatesScreened = 0,
        topMatches = 0,
        avgMatchScorePercent = 0,
        hiringConfidencePercent = 0
    ),
    val rankings: List<CandidateRankingItem> = emptyList(),
    val selectedCandidate: CandidateRankingItem? = null,
    val isLoading: Boolean = true,
    val isRunningRanking: Boolean = false,
    val errorMessage: String? = null,
    val uploadStatusText: String? = null
)

sealed interface RecruiterDashboardEvent {
    data class JobDescriptionChanged(val value: String) : RecruiterDashboardEvent
    data object UploadJdTapped : RecruiterDashboardEvent
    data class TopKChanged(val value: Int) : RecruiterDashboardEvent
    data class MaxSalaryChanged(val value: String) : RecruiterDashboardEvent
    data class MinExperienceChanged(val value: String) : RecruiterDashboardEvent
    data class LocationChanged(val value: String) : RecruiterDashboardEvent
    data class CandidateSelected(val candidate: CandidateRankingItem) : RecruiterDashboardEvent
    data object RunRanking : RecruiterDashboardEvent
}
