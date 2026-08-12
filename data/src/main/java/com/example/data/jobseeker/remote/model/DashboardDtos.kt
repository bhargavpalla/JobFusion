package com.example.data.jobseeker.remote.model

import com.google.gson.annotations.SerializedName

/** Wire format for POST /job-seeker/resume/upload (example). */
data class ResumeUploadResponseDto(
    @SerializedName("file_name") val fileName: String,
    @SerializedName("parsed_successfully") val parsedSuccessfully: Boolean,
    @SerializedName("message") val message: String?
)

data class JobMatchDto(
    @SerializedName("id") val id: String,
    @SerializedName("rank") val rank: Int,
    @SerializedName("title") val title: String,
    @SerializedName("tags") val tags: List<String>,
    @SerializedName("match_percent") val matchPercent: Int,
    @SerializedName("detail_bullets") val detailBullets: List<String>
)

data class DashboardStatsDto(
    @SerializedName("top_match_percent") val topMatchPercent: Int,
    @SerializedName("ranked_job_count") val rankedJobCount: Int,
    @SerializedName("resume_ats_score") val resumeAtsScore: Int,
    @SerializedName("salary_fit_label") val salaryFitLabel: String
)

data class FeedbackRequestDto(
    @SerializedName("job_match_id") val jobMatchId: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("comment") val comment: String?
)
