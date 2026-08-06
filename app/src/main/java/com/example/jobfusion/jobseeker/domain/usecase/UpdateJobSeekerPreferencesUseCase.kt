package com.example.jobfusion.jobseeker.domain.usecase

import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.jobseeker.domain.model.UpdatePreferencesRequest
import com.example.jobfusion.jobseeker.domain.repository.PreferencesRepository

class UpdateJobSeekerPreferencesUseCase(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(request: UpdatePreferencesRequest): NetworkResponse<Unit> =
        repository.updatePreferences(request)
}
