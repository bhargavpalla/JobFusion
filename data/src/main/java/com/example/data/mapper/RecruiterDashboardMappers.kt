package com.example.data.mapper

import com.example.data.model.CandidateRankingItemDto
import com.example.data.model.RecruiterControlsDto
import com.example.data.model.RecruiterDashboardResponseDto
import com.example.domain.auth.repository.CandidateRankingItem
import com.example.domain.auth.repository.RecruiterControls
import com.example.domain.auth.repository.RecruiterDashboardData
import com.example.domain.auth.repository.RecruiterDashboardStats

fun RecruiterControlsDto.toDomain(): RecruiterControls = RecruiterControls(
    jobDescriptionText = jobDescriptionText,
    uploadedJdFileName = uploadedJdFileName,
    topK = topK,
    maxSalary = maxSalary,
    minExperienceYears = minExperienceYears,
    location = location
)

fun CandidateRankingItemDto.toDomain(): CandidateRankingItem = CandidateRankingItem(
    rank = rank,
    candidateName = candidateName,
    candidateId = rank * 100 + 10,
    avatarInitial = candidateName.firstOrNull()?.uppercase() ?: "C",
    role = role,
    yearsExperience = yearsExperience,
    currentSalary = currentSalary,
    location = "Unknown",
    email = "${candidateName.lowercase().replace(" ", ".")}@example.com",
    phone = "0000000000",
    resumeSummary = "$candidateName profile summary is unavailable in remote payload.",
    skillsMatchPercent = (score * 100).toInt().coerceIn(0, 100),
    resumeScore = ((score * 100).toInt() + 5).coerceIn(0, 100),
    score = score
)

fun RecruiterDashboardResponseDto.toDomain(): RecruiterDashboardData = RecruiterDashboardData(
    controls = controls.toDomain(),
    stats = RecruiterDashboardStats(
        candidatesScreened = rankedCandidates.size,
        topMatches = rankedCandidates.size,
        avgMatchScorePercent = if (rankedCandidates.isEmpty()) 0 else rankedCandidates.map { (it.score * 100).toInt() }
            .average().toInt(),
        hiringConfidencePercent = 90
    ),
    rankedCandidates = rankedCandidates.map { it.toDomain() }
)
