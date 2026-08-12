package com.example.jobfusion.jobseeker.ui.preferences

import com.example.data.core.network.RetrofitProvider
import com.example.data.jobseeker.repository.FakePreferencesRepository
import com.example.data.jobseeker.repository.RemotePreferencesRepository
import com.example.domain.jobseeker.repository.PreferencesRepository


object PreferencesDependencies {
    private const val USE_REMOTE_SOURCE = false

    private val fake: PreferencesRepository by lazy { FakePreferencesRepository() }
    private val remote: PreferencesRepository by lazy {
        RemotePreferencesRepository(RetrofitProvider.preferencesApi)
    }

    fun provideRepository(): PreferencesRepository =
        if (USE_REMOTE_SOURCE) remote else fake
}
