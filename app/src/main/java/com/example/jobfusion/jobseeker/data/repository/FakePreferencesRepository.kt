package com.example.jobfusion.jobseeker.data.repository

import com.example.jobfusion.core.dispatcher.DefaultDispatcherProvider
import com.example.jobfusion.core.dispatcher.DispatcherProvider
import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.jobseeker.domain.model.JobSeekerPreferences
import com.example.jobfusion.jobseeker.domain.model.UpdatePreferencesRequest
import com.example.jobfusion.jobseeker.domain.repository.PreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FakePreferencesRepository(
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : PreferencesRepository {

    override suspend fun getJobSeekerPreferences(): NetworkResponse<JobSeekerPreferences> {
        return withContext(dispatcherProvider.io) {
            delay(400)
            NetworkResponse.Success(
                JobSeekerPreferences(
                    location = "Berlin Germany",
                    desiredSalary = "220000",
                    yearsExperience = "3",
                    locationWeightPercent = 30,
                    salaryWeightPercent = 40,
                    experienceWeightPercent = 30
                )
            )
        }
    }

    override suspend fun updatePreferences(request: UpdatePreferencesRequest): NetworkResponse<Unit> {
        return withContext(dispatcherProvider.io) {
            delay(500)
            NetworkResponse.Success(Unit)
        }
    }
}
