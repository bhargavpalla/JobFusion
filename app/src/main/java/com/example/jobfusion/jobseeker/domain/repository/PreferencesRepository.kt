package com.example.jobfusion.jobseeker.domain.repository

import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.jobseeker.domain.model.JobSeekerPreferences
import com.example.jobfusion.jobseeker.domain.model.UpdatePreferencesRequest

interface PreferencesRepository {
    suspend fun getJobSeekerPreferences(): NetworkResponse<JobSeekerPreferences>
    suspend fun updatePreferences(request: UpdatePreferencesRequest): NetworkResponse<Unit>
}
