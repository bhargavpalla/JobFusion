package com.example.jobfusion.jobseeker.data.mapper

import com.example.jobfusion.jobseeker.data.remote.model.DashboardStatsDto
import com.example.jobfusion.jobseeker.data.remote.model.JobMatchDto
import com.example.jobfusion.jobseeker.data.remote.model.ResumeUploadResponseDto
import com.example.jobfusion.jobseeker.domain.model.DashboardStats
import com.example.jobfusion.jobseeker.domain.model.JobMatch
import com.example.jobfusion.jobseeker.domain.model.ResumeUploadResponse

fun ResumeUploadResponseDto.toDomain(): ResumeUploadResponse = ResumeUploadResponse(
    fileName = fileName,
    parsedSuccessfully = parsedSuccessfully,
    message = message
)

fun JobMatchDto.toDomain(): JobMatch = JobMatch(
    id = id,
    rank = rank,
    title = title,
    tags = tags,
    matchPercent = matchPercent,
    detailBullets = detailBullets
)

fun DashboardStatsDto.toDomain(): DashboardStats = DashboardStats(
    topMatchPercent = topMatchPercent,
    rankedJobCount = rankedJobCount,
    resumeAtsScore = resumeAtsScore,
    salaryFitLabel = salaryFitLabel
)
