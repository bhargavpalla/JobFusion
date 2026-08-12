package com.example.data.jobseeker.repository


import com.example.data.auth.mapper.toOutCome
import com.example.data.core.dispatcher.DefaultDispatcherProvider
import com.example.data.core.dispatcher.DispatcherProvider
import com.example.data.core.network.networkTrySuspend
import com.example.data.jobseeker.mapper.toDomain
import com.example.data.jobseeker.mapper.toDto
import com.example.data.jobseeker.remote.PreferencesApi
import com.example.domain.auth.OutCome
import com.example.domain.jobseeker.model.JobSeekerPreferences
import com.example.domain.jobseeker.model.UpdatePreferencesRequest
import com.example.domain.jobseeker.repository.PreferencesRepository

import kotlinx.coroutines.withContext

class RemotePreferencesRepository(
    private val api: PreferencesApi,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : PreferencesRepository {

    override suspend fun getJobSeekerPreferences(): OutCome<JobSeekerPreferences> =
        networkTrySuspend {
            withContext(dispatcherProvider.io) {
                api.getJobSeekerPreferences().toDomain()
            }
        }.toOutCome()

    override suspend fun updatePreferences(request: UpdatePreferencesRequest): OutCome<Unit> =
        networkTrySuspend {
            withContext(dispatcherProvider.io) {
                api.updateJobSeekerPreferences(request.toDto())
            }
        }.toOutCome()
}
