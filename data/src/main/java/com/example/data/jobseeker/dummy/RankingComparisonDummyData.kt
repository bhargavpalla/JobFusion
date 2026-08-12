package com.example.data.jobseeker.dummy

import com.example.domain.jobseeker.model.JobRankingEntry


/**
 * Ranking comparison rows are derived from [DashboardDummyData.jobMatches] so ranks, titles,
 * and scores always match the **Job Matches** tab (score = matchPercent / 100).
 */
object RankingComparisonDummyData {

    val currentRankingTop10: List<JobRankingEntry>
        get() = DashboardDummyData.jobRankingEntries
}
