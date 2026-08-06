package com.example.jobfusion.recruiter.domain.repository

import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.recruiter.domain.model.CandidateRankingItem
import com.example.jobfusion.recruiter.domain.model.RecruiterControls
import com.example.jobfusion.recruiter.domain.model.RecruiterDashboardData

interface RecruiterDashboardRepository {
    suspend fun getDashboardData(): NetworkResponse<RecruiterDashboardData>
    suspend fun runRanking(controls: RecruiterControls): NetworkResponse<List<CandidateRankingItem>>
}
