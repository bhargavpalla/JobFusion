package com.example.domain.jobseeker.usecase

import com.example.domain.auth.OutCome
import com.example.domain.jobseeker.model.JobSeekerPreferences
import com.example.domain.jobseeker.repository.PreferencesRepository
class GetJobSeekerPreferencesUseCase(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(): OutCome<JobSeekerPreferences> =
        repository.getJobSeekerPreferences()
}
