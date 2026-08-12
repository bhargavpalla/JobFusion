package com.example.jobfusion.jobseeker.ui.dashboard

import com.example.data.jobseeker.repository.FakeDashboardRepository
import com.example.domain.jobseeker.repository.DashboardRepository

object DashboardDependencies {
    private val repository: DashboardRepository by lazy { FakeDashboardRepository() }

    fun provideRepository(): DashboardRepository = repository
}
