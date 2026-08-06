package com.example.jobfusion.jobseeker.data.repository

import com.example.jobfusion.core.dispatcher.DefaultDispatcherProvider
import com.example.jobfusion.core.dispatcher.DispatcherProvider
import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.core.network.networkTrySuspend
import com.example.jobfusion.jobseeker.data.mapper.toDomain
import com.example.jobfusion.jobseeker.data.mapper.toDto
import com.example.jobfusion.jobseeker.data.remote.PreferencesApi
import com.example.jobfusion.jobseeker.domain.model.JobSeekerPreferences
import com.example.jobfusion.jobseeker.domain.model.UpdatePreferencesRequest
import com.example.jobfusion.jobseeker.domain.repository.PreferencesRepository
import kotlinx.coroutines.withContext

class RemotePreferencesRepository(
    private val api: PreferencesApi,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : PreferencesRepository {

    override suspend fun getJobSeekerPreferences(): NetworkResponse<JobSeekerPreferences> =
        networkTrySuspend {
            withContext(dispatcherProvider.io) {
                api.getJobSeekerPreferences().toDomain()
            }
        }

    override suspend fun updatePreferences(request: UpdatePreferencesRequest): NetworkResponse<Unit> =
        networkTrySuspend {
            withContext(dispatcherProvider.io) {
                api.updateJobSeekerPreferences(request.toDto())
            }
        }
}
