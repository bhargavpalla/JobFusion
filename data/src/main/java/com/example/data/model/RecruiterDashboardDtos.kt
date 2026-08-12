package com.example.data.model

import com.google.gson.annotations.SerializedName

data class RecruiterControlsDto(
    @SerializedName("job_description_text") val jobDescriptionText: String,
    @SerializedName("uploaded_jd_file_name") val uploadedJdFileName: String?,
    @SerializedName("top_k") val topK: Int,
    @SerializedName("max_salary") val maxSalary: String,
    @SerializedName("min_experience_years") val minExperienceYears: String,
    @SerializedName("location") val location: String
)

data class CandidateRankingItemDto(
    @SerializedName("rank") val rank: Int,
    @SerializedName("candidate_name") val candidateName: String,
    @SerializedName("role") val role: String,
    @SerializedName("years_experience") val yearsExperience: Int,
    @SerializedName("current_salary") val currentSalary: Int,
    @SerializedName("score") val score: Double
)

data class RecruiterDashboardResponseDto(
    @SerializedName("controls") val controls: RecruiterControlsDto,
    @SerializedName("ranked_candidates") val rankedCandidates: List<CandidateRankingItemDto>
)
