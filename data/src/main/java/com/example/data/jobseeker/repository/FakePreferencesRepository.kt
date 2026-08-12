package com.example.data.jobseeker.repository

import com.example.data.core.dispatcher.DefaultDispatcherProvider
import com.example.data.core.dispatcher.DispatcherProvider
import com.example.domain.auth.OutCome
import com.example.domain.jobseeker.model.JobSeekerPreferences
import com.example.domain.jobseeker.model.UpdatePreferencesRequest
import com.example.domain.jobseeker.repository.PreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FakePreferencesRepository(
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : PreferencesRepository {

    override suspend fun getJobSeekerPreferences(): OutCome<JobSeekerPreferences> {
        return withContext(dispatcherProvider.io) {
            delay(400)
            OutCome.Success(
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

    override suspend fun updatePreferences(request: UpdatePreferencesRequest): OutCome<Unit> {
        return withContext(dispatcherProvider.io) {
            delay(500)
            OutCome.Success(Unit)
        }
    }
}
