package com.example.jobfusion.recruiter.ui.dashboard

import com.example.data.repository.FakeRecruiterDashboardRepository
import com.example.domain.auth.model.RecruiterDashboardRepository

object RecruiterDashboardDependencies {
    private val repository: RecruiterDashboardRepository by lazy {
        FakeRecruiterDashboardRepository()
    }

    fun provideRepository(): RecruiterDashboardRepository = repository
}
