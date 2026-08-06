package com.example.jobfusion.recruiter.data.repository

import com.example.jobfusion.core.dispatcher.DefaultDispatcherProvider
import com.example.jobfusion.core.dispatcher.DispatcherProvider
import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.recruiter.data.dummy.RecruiterDashboardDummyData
import com.example.jobfusion.recruiter.domain.model.CandidateRankingItem
import com.example.jobfusion.recruiter.domain.model.RecruiterControls
import com.example.jobfusion.recruiter.domain.model.RecruiterDashboardData
import com.example.jobfusion.recruiter.domain.repository.RecruiterDashboardRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FakeRecruiterDashboardRepository(
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : RecruiterDashboardRepository {

    override suspend fun getDashboardData(): NetworkResponse<RecruiterDashboardData> {
        return withContext(dispatcherProvider.io) {
            delay(450)
            NetworkResponse.Success(RecruiterDashboardDummyData.dashboardData)
        }
    }

    override suspend fun runRanking(controls: RecruiterControls): NetworkResponse<List<CandidateRankingItem>> {
        return withContext(dispatcherProvider.io) {
            delay(650)
            val maxSalary = controls.maxSalary.toIntOrNull() ?: Int.MAX_VALUE
            val minExp = controls.minExperienceYears.toIntOrNull() ?: 0
            val normalizedLocation = controls.location.trim().lowercase()
            val filtered = RecruiterDashboardDummyData.candidates
                .filter {
                    it.currentSalary <= maxSalary &&
                        it.yearsExperience >= minExp &&
                        (normalizedLocation.isBlank() || it.location.lowercase().contains(normalizedLocation))
                }
                .sortedByDescending { it.score }
                .take(controls.topK.coerceAtLeast(1))
                .mapIndexed { index, c -> c.copy(rank = index + 1) }
            NetworkResponse.Success(filtered)
        }
    }
}
