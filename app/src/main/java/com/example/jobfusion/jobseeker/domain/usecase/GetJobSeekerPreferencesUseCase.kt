package com.example.jobfusion.jobseeker.domain.usecase

import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.jobseeker.domain.model.JobSeekerPreferences
import com.example.jobfusion.jobseeker.domain.repository.PreferencesRepository

class GetJobSeekerPreferencesUseCase(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(): NetworkResponse<JobSeekerPreferences> =
        repository.getJobSeekerPreferences()
}
