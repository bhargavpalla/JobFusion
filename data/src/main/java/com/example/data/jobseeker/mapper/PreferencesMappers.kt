package com.example.data.jobseeker.mapper

import com.example.data.jobseeker.remote.model.JobSeekerPreferencesDto
import com.example.data.jobseeker.remote.model.UpdatePreferencesRequestDto
import com.example.domain.jobseeker.model.JobSeekerPreferences
import com.example.domain.jobseeker.model.UpdatePreferencesRequest

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
