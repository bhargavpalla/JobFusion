package com.example.domain.jobseeker.repository

import com.example.domain.auth.OutCome
import com.example.domain.jobseeker.model.DashboardStats
import com.example.domain.jobseeker.model.FeedbackRequest
import com.example.domain.jobseeker.model.JobMatch
import com.example.domain.jobseeker.model.ResumeUploadResponse


interface DashboardRepository {
    suspend fun uploadResume(): OutCome<ResumeUploadResponse>
    suspend fun getRecommendations(): OutCome<List<JobMatch>>
    suspend fun submitFeedback(request: FeedbackRequest): OutCome<Unit>
    suspend fun getDashboardStats(): OutCome<DashboardStats>
}
