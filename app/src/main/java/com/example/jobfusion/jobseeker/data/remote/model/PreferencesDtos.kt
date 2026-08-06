package com.example.jobfusion.jobseeker.data.remote.model

import com.google.gson.annotations.SerializedName

data class JobSeekerPreferencesDto(
    @SerializedName("location") val location: String,
    @SerializedName("desired_salary") val desiredSalary: String,
    @SerializedName("years_experience") val yearsExperience: String,
    @SerializedName("location_weight_percent") val locationWeightPercent: Int,
    @SerializedName("salary_weight_percent") val salaryWeightPercent: Int,
    @SerializedName("experience_weight_percent") val experienceWeightPercent: Int
)

data class UpdatePreferencesRequestDto(
    @SerializedName("location") val location: String,
    @SerializedName("desired_salary") val desiredSalary: String,
    @SerializedName("years_experience") val yearsExperience: String,
    @SerializedName("location_weight_percent") val locationWeightPercent: Int,
    @SerializedName("salary_weight_percent") val salaryWeightPercent: Int,
    @SerializedName("experience_weight_percent") val experienceWeightPercent: Int
)
