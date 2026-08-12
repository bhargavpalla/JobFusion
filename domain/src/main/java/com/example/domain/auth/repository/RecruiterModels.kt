package com.example.domain.auth.repository

data class RecruiterControls(
    val jobDescriptionText: String = "",
    val uploadedJdFileName: String? = null,
    val topK: Int = 20,
    val maxSalary: String = "150000",
    val minExperienceYears: String = "0",
    val location: String = ""
)

data class CandidateRankingItem(
    val rank: Int,
    val candidateName: String,
    val candidateId: Int,
    val avatarInitial: String,
    val role: String,
    val yearsExperience: Int,
    val currentSalary: Int,
    val location: String,
    val email: String,
    val phone: String,
    val resumeSummary: String,
    val skillsMatchPercent: Int,
    val resumeScore: Int,
    /** Normalized score in [0, 1]. */
    val score: Double
)

data class RecruiterDashboardStats(
    val candidatesScreened: Int,
    val topMatches: Int,
    val avgMatchScorePercent: Int,
    val hiringConfidencePercent: Int
)

data class RecruiterDashboardData(
    val controls: RecruiterControls,
    val stats: RecruiterDashboardStats,
    val rankedCandidates: List<CandidateRankingItem>
)
