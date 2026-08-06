package com.example.jobfusion.recruiter.ui.dashboard

import com.example.jobfusion.recruiter.data.repository.FakeRecruiterDashboardRepository
import com.example.jobfusion.recruiter.domain.repository.RecruiterDashboardRepository

object RecruiterDashboardDependencies {
    private val repository: RecruiterDashboardRepository by lazy {
        FakeRecruiterDashboardRepository()
    }

    fun provideRepository(): RecruiterDashboardRepository = repository
}
