package com.example.data.jobseeker.dummy

import com.example.domain.jobseeker.model.AiResumeInsights
import com.example.domain.jobseeker.model.ImprovementScoreInsight
import com.example.domain.jobseeker.model.MissingSkillsInsight
import com.example.domain.jobseeker.model.StrongSectionsInsight
import com.example.domain.jobseeker.model.SuggestedKeywordsInsight


/**
 * Static AI resume insights for the dashboard tab until the insights API is wired.
 */
object AiResumeInsightsDummyData {

    val insights: AiResumeInsights = AiResumeInsights(
        missingSkills = MissingSkillsInsight(
            title = "Missing Skills",
            subtitle = "Add these to strengthen matches in your target roles.",
            skillChips = listOf("System Design", "Kubernetes", "GraphQL")
        ),
        suggestedKeywords = SuggestedKeywordsInsight(
            title = "Suggested Keywords",
            subtitle = "Natural phrasing that boosts ATS and recruiter visibility.",
            keywordChips = listOf(
                "Scalable architecture",
                "CI/CD",
                "Cross-functional collaboration",
                "Agile"
            )
        ),
        strongSections = StrongSectionsInsight(
            title = "Strong Sections",
            bulletPoints = listOf(
                "Professional Experience",
                "Technical Skills",
                "Measurable achievements"
            )
        ),
        improvementScore = ImprovementScoreInsight(
            // Narrow cards: keep phrase from breaking as "Improvemen" / "t Score"
            title = "Improvement\u00A0Score",
            score = 83,
            scoreMax = 100,
            description = "Applying the suggestions above is projected to raise your match score and ATS readiness."
        )
    )
}
