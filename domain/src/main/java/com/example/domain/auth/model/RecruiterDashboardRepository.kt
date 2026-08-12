package com.example.domain.auth.model

import com.example.domain.auth.OutCome
import com.example.domain.auth.repository.CandidateRankingItem
import com.example.domain.auth.repository.RecruiterControls
import com.example.domain.auth.repository.RecruiterDashboardData

interface RecruiterDashboardRepository {
    suspend fun getDashboardData(): OutCome<RecruiterDashboardData>
    suspend fun runRanking(controls: RecruiterControls): OutCome<List<CandidateRankingItem>>
}