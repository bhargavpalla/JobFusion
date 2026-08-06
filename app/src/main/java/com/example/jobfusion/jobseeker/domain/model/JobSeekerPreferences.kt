package com.example.jobfusion.jobseeker.domain.model

data class JobSeekerPreferences(
    val location: String,
    val desiredSalary: String,
    val yearsExperience: String,
    val locationWeightPercent: Int,
    val salaryWeightPercent: Int,
    val experienceWeightPercent: Int
)

data class UpdatePreferencesRequest(
    val location: String,
    val desiredSalary: String,
    val yearsExperience: String,
    val locationWeightPercent: Int,
    val salaryWeightPercent: Int,
    val experienceWeightPercent: Int
)
