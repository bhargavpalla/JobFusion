package com.example.jobfusion.jobseeker.data.mapper

import com.example.jobfusion.jobseeker.data.remote.model.JobSeekerPreferencesDto
import com.example.jobfusion.jobseeker.data.remote.model.UpdatePreferencesRequestDto
import com.example.jobfusion.jobseeker.domain.model.JobSeekerPreferences
import com.example.jobfusion.jobseeker.domain.model.UpdatePreferencesRequest

fun JobSeekerPreferencesDto.toDomain(): JobSeekerPreferences = JobSeekerPreferences(
    location = location,
    desiredSalary = desiredSalary,
    yearsExperience = yearsExperience,
    locationWeightPercent = locationWeightPercent,
    salaryWeightPercent = salaryWeightPercent,
    experienceWeightPercent = experienceWeightPercent
)

fun UpdatePreferencesRequest.toDto(): UpdatePreferencesRequestDto = UpdatePreferencesRequestDto(
    location = location,
    desiredSalary = desiredSalary,
    yearsExperience = yearsExperience,
    locationWeightPercent = locationWeightPercent,
    salaryWeightPercent = salaryWeightPercent,
    experienceWeightPercent = experienceWeightPercent
)
