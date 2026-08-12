package com.example.data.repository


import com.example.data.core.dispatcher.DefaultDispatcherProvider
import com.example.data.core.dispatcher.DispatcherProvider
import com.example.data.dummy.RecruiterDashboardDummyData
import com.example.domain.auth.OutCome
import com.example.domain.auth.model.RecruiterDashboardRepository
import com.example.domain.auth.repository.CandidateRankingItem
import com.example.domain.auth.repository.RecruiterControls
import com.example.domain.auth.repository.RecruiterDashboardData
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FakeRecruiterDashboardRepository(
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : RecruiterDashboardRepository {

    override suspend fun getDashboardData(): OutCome<RecruiterDashboardData> {
        return withContext(dispatcherProvider.io) {
            delay(450)
            OutCome.Success(RecruiterDashboardDummyData.dashboardData)
        }
    }

    override suspend fun runRanking(controls: RecruiterControls): OutCome<List<CandidateRankingItem>> {
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
            OutCome.Success(filtered)
        }
    }
}
