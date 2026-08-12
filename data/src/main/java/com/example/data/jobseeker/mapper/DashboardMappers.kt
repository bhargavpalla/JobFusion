package com.example.data.jobseeker.mapper

import com.example.data.jobseeker.remote.model.DashboardStatsDto
import com.example.data.jobseeker.remote.model.JobMatchDto
import com.example.data.jobseeker.remote.model.ResumeUploadResponseDto
import com.example.domain.jobseeker.model.DashboardStats
import com.example.domain.jobseeker.model.JobMatch
import com.example.domain.jobseeker.model.ResumeUploadResponse

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
