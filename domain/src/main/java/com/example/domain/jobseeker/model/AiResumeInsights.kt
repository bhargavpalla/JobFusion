package com.example.domain.jobseeker.model

data class AiResumeInsights(
    val missingSkills: MissingSkillsInsight,
    val suggestedKeywords: SuggestedKeywordsInsight,
    val strongSections: StrongSectionsInsight,
    val improvementScore: ImprovementScoreInsight
)

data class MissingSkillsInsight(
    val title: String,
    val subtitle: String,
    val skillChips: List<String>
)

data class SuggestedKeywordsInsight(
    val title: String,
    val subtitle: String,
    val keywordChips: List<String>
)

data class StrongSectionsInsight(
    val title: String,
    val bulletPoints: List<String>
)

data class ImprovementScoreInsight(
    val title: String,
    val score: Int,
    val scoreMax: Int,
    val description: String
)
