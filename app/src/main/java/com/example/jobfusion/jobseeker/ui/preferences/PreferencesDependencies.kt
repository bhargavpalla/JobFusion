package com.example.jobfusion.jobseeker.ui.preferences

import com.example.jobfusion.core.network.RetrofitProvider
import com.example.jobfusion.jobseeker.data.repository.FakePreferencesRepository
import com.example.jobfusion.jobseeker.data.repository.RemotePreferencesRepository
import com.example.jobfusion.jobseeker.domain.repository.PreferencesRepository

object PreferencesDependencies {
    private const val USE_REMOTE_SOURCE = false

    private val fake: PreferencesRepository by lazy { FakePreferencesRepository() }
    private val remote: PreferencesRepository by lazy {
        RemotePreferencesRepository(RetrofitProvider.preferencesApi)
    }

    fun provideRepository(): PreferencesRepository =
        if (USE_REMOTE_SOURCE) remote else fake
}
