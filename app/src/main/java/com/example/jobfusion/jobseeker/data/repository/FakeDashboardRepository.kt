package com.example.jobfusion.jobseeker.data.repository

import com.example.jobfusion.core.dispatcher.DefaultDispatcherProvider
import com.example.jobfusion.core.dispatcher.DispatcherProvider
import com.example.jobfusion.core.network.NetworkResponse
import com.example.jobfusion.jobseeker.domain.model.DashboardStats
import com.example.jobfusion.jobseeker.domain.model.FeedbackRequest
import com.example.jobfusion.jobseeker.domain.model.JobMatch
import com.example.jobfusion.jobseeker.domain.model.ResumeUploadResponse
import com.example.jobfusion.jobseeker.domain.repository.DashboardRepository
import com.example.jobfusion.jobseeker.data.dummy.DashboardDummyData
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FakeDashboardRepository(
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : DashboardRepository {

    override suspend fun uploadResume(): NetworkResponse<ResumeUploadResponse> {
        return withContext(dispatcherProvider.io) {
            delay(900)
            NetworkResponse.Success(
                ResumeUploadResponse(
                    fileName = "bhargav_4yrs.pdf",
                    parsedSuccessfully = true,
                    message = null
                )
            )
        }
    }

    override suspend fun getRecommendations(): NetworkResponse<List<JobMatch>> {
        return withContext(dispatcherProvider.io) {
            delay(600)
            NetworkResponse.Success(DashboardDummyData.jobMatches)
        }
    }

    override suspend fun submitFeedback(request: FeedbackRequest): NetworkResponse<Unit> {
        return withContext(dispatcherProvider.io) {
            delay(450)
            if (request.rating < 1f) {
                val ex = IllegalArgumentException("Rating must be at least 1")
                NetworkResponse.Error(ex.message ?: "Rating must be at least 1", ex)
            } else {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun getDashboardStats(): NetworkResponse<DashboardStats> {
        return withContext(dispatcherProvider.io) {
            delay(400)
            val jobs = DashboardDummyData.jobMatches
            val top = jobs.maxOfOrNull { it.matchPercent } ?: 0
            NetworkResponse.Success(
                DashboardStats(
                    topMatchPercent = top,
                    rankedJobCount = jobs.size,
                    resumeAtsScore = 83,
                    salaryFitLabel = "High"
                )
            )
        }
    }
}
