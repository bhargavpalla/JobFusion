package com.example.jobfusion.jobseeker.data.remote

import com.example.jobfusion.jobseeker.data.remote.model.JobSeekerPreferencesDto
import com.example.jobfusion.jobseeker.data.remote.model.UpdatePreferencesRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface PreferencesApi {
    @GET("preferences/job-seeker")
    suspend fun getJobSeekerPreferences(): JobSeekerPreferencesDto

    @PUT("preferences/job-seeker")
    suspend fun updateJobSeekerPreferences(@Body body: UpdatePreferencesRequestDto)
}
