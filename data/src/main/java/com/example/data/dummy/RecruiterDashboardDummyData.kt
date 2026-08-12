package com.example.data.dummy

import com.example.domain.auth.repository.CandidateRankingItem
import com.example.domain.auth.repository.RecruiterControls
import com.example.domain.auth.repository.RecruiterDashboardData
import com.example.domain.auth.repository.RecruiterDashboardStats


object RecruiterDashboardDummyData {
    val controls = RecruiterControls(
        topK = 20,
        maxSalary = "150000",
        minExperienceYears = "2",
        location = "Bangalore"
    )

    val stats = RecruiterDashboardStats(
        candidatesScreened = 20,
        topMatches = 6,
        avgMatchScorePercent = 78,
        hiringConfidencePercent = 96
    )

    val candidates: List<CandidateRankingItem> = listOf(
        CandidateRankingItem(
            rank = 1,
            candidateName = "Syam Kumar",
            candidateId = 269,
            avatarInitial = "P",
            role = "Software Engineer",
            yearsExperience = 3,
            currentSalary = 1750000,
            location = "Bangalore",
            email = "padma.iyer@gmail.com",
            phone = "9845098450",
            resumeSummary = "Software Engineer with 4 years of experience building scalable backend APIs, distributed services, and robust cloud-native platforms.",
            skillsMatchPercent = 92,
            resumeScore = 97,
            score = 0.91
        ),
        CandidateRankingItem(
            rank = 2,
            candidateName = "Movva Navya Sri Lakshmi",
            candidateId = 232,
            avatarInitial = "L",
            role = "Software Engineer",
            yearsExperience = 4,
            currentSalary = 1520000,
            location = "Bangalore",
            email = "lakshmi.gupta@gmail.com",
            phone = "9720154469",
            resumeSummary = "Software Engineer with 3 years of experience. Skilled in Dart, Kubernetes, Machine Learning, Spring Boot, Python. Experienced in building scalable applications.",
            skillsMatchPercent = 88,
            resumeScore = 96,
            score = 0.91
        ),
        CandidateRankingItem(
            rank = 3,
            candidateName = "Myra Sharma",
            candidateId = 390,
            avatarInitial = "M",
            role = "Software Engineer",
            yearsExperience = 2,
            currentSalary = 106958,
            location = "Hyderabad",
            email = "myra.sharma@gmail.com",
            phone = "9819012345",
            resumeSummary = "Software Engineer focused on frontend and API integrations, with strong fundamentals in React, Kotlin, and modern testing practices.",
            skillsMatchPercent = 91,
            resumeScore = 96,
            score = 0.91
        )
    )

    val dashboardData = RecruiterDashboardData(
        controls = controls,
        stats = stats,
        rankedCandidates = candidates
    )
}
