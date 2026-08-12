package com.example.data.jobseeker.dummy

import com.example.domain.jobseeker.model.JobMatch
import com.example.domain.jobseeker.model.JobRankingEntry
import kotlin.collections.map

/**
 * Static job matches for local / fake API flows. Replace with remote DTO mapping when backend is live.
 */
object DashboardDummyData {

    /**
     * Same ordering and titles as [jobMatches]; [JobRankingEntry.score] is [JobMatch.matchPercent] / 100.
     * Keeps **Job Matches** and **Ranking comparison** tabs in sync for dummy data.
     */
    val jobRankingEntries: List<JobRankingEntry>
        get() = jobMatches.map { it.toRankingEntry() }

    val jobMatches: List<JobMatch> = listOf(
        JobMatch(
            id = "jm-1",
            rank = 1,
            title = "Backend Engineer",
            tags = listOf("Bangalore", "Full-time"),
            matchPercent = 91,
            detailBullets = listOf(
                "Strong overlap with your React and Kotlin experience.",
                "Company size matches your preference for small teams.",
                "Visa sponsorship not listed — confirm with recruiter."
            )
        ),
        JobMatch(
            id = "jm-2",
            rank = 2,
            title = "Java Developer",
            tags = listOf("Bangalore", "Full-time"),
            matchPercent = 87,
            detailBullets = listOf(
                "Good fit on backend APIs and cloud exposure.",
                "Salary band partially aligned with your target range."
            )
        ),
        JobMatch(
            id = "jm-3",
            rank = 3,
            title = "Senior Android developer",
            tags = listOf("Berlin, Germany", "Hybrid", "Full-time"),
            matchPercent = 74,
            detailBullets = listOf(
                "Jetpack Compose and Material 3 called out in the JD.",
                "Team ships consumer apps with monthly release trains."
            )
        ),
        JobMatch(
            id = "jm-4",
            rank = 4,
            title = "Kotlin backend engineer",
            tags = listOf("Remote — EU", "Contract"),
            matchPercent = 71,
            detailBullets = listOf(
                "Ktor + PostgreSQL stack close to your recent projects.",
                "Contract length 12 months with possible extension."
            )
        ),
        JobMatch(
            id = "jm-5",
            rank = 5,
            title = "Full stack engineer",
            tags = listOf("Amsterdam, NL", "On-site", "Full-time"),
            matchPercent = 68,
            detailBullets = listOf(
                "React + Spring split matches your full-stack profile.",
                "Commute: office 3 days / week per job description."
            )
        ),
        JobMatch(
            id = "jm-6",
            rank = 6,
            title = "Mobile engineer (iOS + Android)",
            tags = listOf("London, UK", "Hybrid"),
            matchPercent = 64,
            detailBullets = listOf(
                "Cross-platform emphasis; Kotlin Multiplatform mentioned once.",
                "Slightly heavier iOS ownership than your last role."
            )
        ),
        JobMatch(
            id = "jm-7",
            rank = 7,
            title = "Staff software engineer — platform",
            tags = listOf("Remote — US", "Full-time"),
            matchPercent = 61,
            detailBullets = listOf(
                "Leadership scope beyond IC track you selected in preferences.",
                "Strong infra and observability themes align with your CV."
            )
        ),
        JobMatch(
            id = "jm-8",
            rank = 8,
            title = "DevOps engineer",
            tags = listOf("Singapore", "Full-time"),
            matchPercent = 58,
            detailBullets = listOf(
                "Tilt toward Terraform and Kubernetes vs app development.",
                "Worth a look if you want to stretch into platform reliability."
            )
        ),
        JobMatch(
            id = "jm-9",
            rank = 9,
            title = "Frontend engineer",
            tags = listOf("Toronto, Canada", "Remote"),
            matchPercent = 55,
            detailBullets = listOf(
                "TypeScript + Next.js heavy; less Kotlin than your typical targets.",
                "Design-system work could be a good learning angle."
            )
        ),
        JobMatch(
            id = "jm-10",
            rank = 10,
            title = "QA automation engineer",
            tags = listOf("Bangalore, India", "Hybrid", "Full-time"),
            matchPercent = 52,
            detailBullets = listOf(
                "Espresso and Compose testing tooling overlaps your Android skills.",
                "Role is test-focused rather than feature delivery."
            )
        )
    )
}

private fun JobMatch.toRankingEntry(): JobRankingEntry = JobRankingEntry(
    rank = rank,
    jobTitle = title,
    score = matchPercent / 100.0
)
