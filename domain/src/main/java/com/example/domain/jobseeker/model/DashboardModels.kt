package com.example.domain.jobseeker.model

/**
 * Domain job match row (API-ready; maps from DTO in remote layer).
 */
data class JobMatch(
    val id: String,
    val rank: Int,
    val title: String,
    val tags: List<String>,
    val matchPercent: Int,
    val detailBullets: List<String>
)

data class DashboardStats(
    val topMatchPercent: Int,
    val rankedJobCount: Int,
    val resumeAtsScore: Int,
    val salaryFitLabel: String
)

data class ResumeUploadResponse(
    val fileName: String,
    val parsedSuccessfully: Boolean,
    val message: String?
)

data class FeedbackRequest(
    val jobMatchId: String,
    val rating: Float,
    val comment: String? = null
)
