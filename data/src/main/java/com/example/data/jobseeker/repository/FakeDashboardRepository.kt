package com.example.data.jobseeker.repository

import com.example.data.core.dispatcher.DefaultDispatcherProvider
import com.example.data.core.dispatcher.DispatcherProvider
import com.example.data.jobseeker.dummy.DashboardDummyData
import com.example.domain.auth.OutCome
import com.example.domain.jobseeker.model.DashboardStats
import com.example.domain.jobseeker.model.FeedbackRequest
import com.example.domain.jobseeker.model.JobMatch
import com.example.domain.jobseeker.model.ResumeUploadResponse
import com.example.domain.jobseeker.repository.DashboardRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class FakeDashboardRepository(
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider
) : DashboardRepository {

    override suspend fun uploadResume(): OutCome<ResumeUploadResponse> {
        return withContext(dispatcherProvider.io) {
            delay(900)
            OutCome.Success(
                ResumeUploadResponse(
                    fileName = "bhargav_4yrs.pdf",
                    parsedSuccessfully = true,
                    message = null
                )
            )
        }
    }

    override suspend fun getRecommendations(): OutCome<List<JobMatch>> {
        return withContext(dispatcherProvider.io) {
            delay(600)
            OutCome.Success(DashboardDummyData.jobMatches)
        }
    }

    override suspend fun submitFeedback(request: FeedbackRequest): OutCome<Unit> {
        return withContext(dispatcherProvider.io) {
            delay(450)
            if (request.rating < 1f) {
                val ex = IllegalArgumentException("Rating must be at least 1")
                OutCome.Error(ex.message ?: "Rating must be at least 1", ex)
            } else {
                OutCome.Success(Unit)
            }
        }
    }

    override suspend fun getDashboardStats(): OutCome<DashboardStats> {
        return withContext(dispatcherProvider.io) {
            delay(400)
            val jobs = DashboardDummyData.jobMatches
            val top = jobs.maxOfOrNull { it.matchPercent } ?: 0
            OutCome.Success(
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
