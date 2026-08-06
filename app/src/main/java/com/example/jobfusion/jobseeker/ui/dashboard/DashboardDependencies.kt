package com.example.jobfusion.jobseeker.ui.dashboard

import com.example.jobfusion.jobseeker.data.repository.FakeDashboardRepository
import com.example.jobfusion.jobseeker.domain.repository.DashboardRepository

object DashboardDependencies {
    private val repository: DashboardRepository by lazy { FakeDashboardRepository() }

    fun provideRepository(): DashboardRepository = repository
}
