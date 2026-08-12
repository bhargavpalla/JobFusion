package com.example.domain.jobseeker.usecase

import com.example.domain.auth.OutCome
import com.example.domain.jobseeker.model.UpdatePreferencesRequest
import com.example.domain.jobseeker.repository.PreferencesRepository


class UpdateJobSeekerPreferencesUseCase(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(request: UpdatePreferencesRequest): OutCome<Unit> =
        repository.updatePreferences(request)
}
