package com.example.jobfusion.jobseeker.domain.model

/**
 * A single row in a ranking comparison list (API-ready).
 */
data class JobRankingEntry(
    val rank: Int,
    val jobTitle: String,
    /** Normalized score in [0, 1], e.g. 0.914 */
    val score: Double
)
