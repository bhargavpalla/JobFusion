package com.example.jobfusion.jobseeker.ui.dashboard

import com.example.jobfusion.jobseeker.domain.model.DashboardStats
import com.example.jobfusion.jobseeker.domain.model.JobMatch

enum class DashboardTab {
    JobMatches,
    RankingComparison,
    AiResumeInsights
}

enum class ResumeUploadPhase {
    Idle,
    Loading,
    Success,
    Error
}

data class DashboardUiState(
    val resumePhase: ResumeUploadPhase = ResumeUploadPhase.Success,
    val resumeFileName: String = "bhargav_4yrs.pdf",
    val resumeErrorMessage: String? = null,
    val resumeSuccessMessage: String? = "Resume parsed successfully",
    val isMatchEngineBusy: Boolean = false,
    val dashboardStats: DashboardStats? = null,
    val statsLoading: Boolean = true,
    val statsError: String? = null,
    val selectedTab: DashboardTab = DashboardTab.JobMatches,
    val jobMatches: List<JobMatch> = emptyList(),
    val recommendationsLoading: Boolean = true,
    val recommendationsError: String? = null,
    val expandedJobIds: Set<String> = emptySet(),
    /** Local slider value 1–5 per job id. */
    val jobRatings: Map<String, Int> = emptyMap(),
    val feedbackSubmittedIds: Set<String> = emptySet(),
    val feedbackSubmittingJobId: String? = null,
    val feedbackError: String? = null,
    val transientBanner: String? = null
)

sealed interface DashboardEvent {
    data object ResumeReplaceTapped : DashboardEvent
    data object FindPerfectJobs : DashboardEvent
    data object EnhanceWithAi : DashboardEvent
    data class TabSelected(val tab: DashboardTab) : DashboardEvent
    data class JobExpandToggled(val jobId: String) : DashboardEvent
    data class JobRatingChanged(val jobId: String, val rating: Int) : DashboardEvent
    data class SubmitFeedback(val jobId: String) : DashboardEvent
    data object BannerDismissed : DashboardEvent
}
