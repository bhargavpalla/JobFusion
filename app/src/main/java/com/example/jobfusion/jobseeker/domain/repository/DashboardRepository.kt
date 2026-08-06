package com.example.jobfusion.jobseeker.domain.repository

import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.jobseeker.domain.model.DashboardStats
import com.example.jobfusion.jobseeker.domain.model.FeedbackRequest
import com.example.jobfusion.jobseeker.domain.model.JobMatch
import com.example.jobfusion.jobseeker.domain.model.ResumeUploadResponse

interface DashboardRepository {
    suspend fun uploadResume(): NetworkResponse<ResumeUploadResponse>
    suspend fun getRecommendations(): NetworkResponse<List<JobMatch>>
    suspend fun submitFeedback(request: FeedbackRequest): NetworkResponse<Unit>
    suspend fun getDashboardStats(): NetworkResponse<DashboardStats>
}
