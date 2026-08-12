package com.example.data.jobseeker.remote

import com.example.data.jobseeker.remote.model.JobSeekerPreferencesDto
import com.example.data.jobseeker.remote.model.UpdatePreferencesRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface PreferencesApi {
    @GET("preferences/job-seeker")
    suspend fun getJobSeekerPreferences(): JobSeekerPreferencesDto

    @PUT("preferences/job-seeker")
    suspend fun updateJobSeekerPreferences(@Body body: UpdatePreferencesRequestDto)
}
