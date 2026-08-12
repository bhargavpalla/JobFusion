package com.example.domain.jobseeker.repository

import com.example.domain.auth.OutCome
import com.example.domain.jobseeker.model.JobSeekerPreferences
import com.example.domain.jobseeker.model.UpdatePreferencesRequest

interface PreferencesRepository {
    suspend fun getJobSeekerPreferences(): OutCome<JobSeekerPreferences>
    suspend fun updatePreferences(request: UpdatePreferencesRequest): OutCome<Unit>
}
