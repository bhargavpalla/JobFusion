package com.example.jobfusion.jobseeker.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jobfusion.jobseeker.domain.model.DashboardStats
import com.example.jobfusion.jobseeker.domain.model.JobMatch
import com.example.jobfusion.jobseeker.ui.dashboard.components.DashboardCardBorder
import com.example.jobfusion.jobseeker.ui.dashboard.components.DashboardMuted
import com.example.jobfusion.jobseeker.ui.dashboard.components.DashboardNavy
import com.example.jobfusion.jobseeker.ui.dashboard.components.DashboardStatCard
import com.example.jobfusion.jobseeker.ui.dashboard.components.DashboardSurfaceBlue
import com.example.jobfusion.jobseeker.ui.dashboard.components.DashboardTabRow
import com.example.jobfusion.jobseeker.ui.dashboard.components.JobMatchCard
import com.example.jobfusion.jobseeker.ui.dashboard.components.StatEstimateBg
import com.example.jobfusion.jobseeker.ui.dashboard.components.StatMarketBg
import com.example.jobfusion.jobseeker.ui.dashboard.components.StatRankedBg
import com.example.jobfusion.jobseeker.ui.dashboard.components.StatTopMatchBg
import com.example.jobfusion.jobseeker.data.dummy.AiResumeInsightsDummyData
import com.example.jobfusion.jobseeker.data.dummy.RankingComparisonDummyData
import com.example.jobfusion.jobseeker.ui.dashboard.components.AiResumeInsightsSection
import com.example.jobfusion.jobseeker.ui.dashboard.components.RankingComparisonSection
import com.example.jobfusion.jobseeker.ui.dashboard.components.UploadCard
import com.example.jobfusion.ui.theme.JobFusionTheme

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(DashboardDependencies.provideRepository())
    )
) {
    val state by viewModel.uiState.collectAsState()
    DashboardContent(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
internal fun DashboardContent(
    state: DashboardUiState,
    onEvent: (DashboardEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        state.transientBanner?.let { message ->
            item(key = "banner") {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DashboardSurfaceBlue,
                    border = BorderStroke(1.dp, DashboardNavy.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DashboardNavy,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onEvent(DashboardEvent.BannerDismissed) }) {
                            Icon(Icons.Outlined.Close, contentDescription = "Dismiss")
                        }
                    }
                }
            }
        }

        item(key = "resume") {
            StepSection(stepNumber = 1, title = "Upload your resume")
            Spacer(modifier = Modifier.height(10.dp))
            UploadCard(
                phase = state.resumePhase,
                fileName = state.resumeFileName,
                successMessage = state.resumeSuccessMessage,
                errorMessage = state.resumeErrorMessage,
                onReplaceClick = { onEvent(DashboardEvent.ResumeReplaceTapped) }
            )
        }

        item(key = "match") {
            StepSection(stepNumber = 2, title = "Run the match engine")
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { onEvent(DashboardEvent.FindPerfectJobs) },
                    enabled = !state.isMatchEngineBusy,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = DashboardNavy),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.TrackChanges,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = "Find My Perfect Jobs",
                        maxLines = 2,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                OutlinedButton(
                    onClick = { onEvent(DashboardEvent.EnhanceWithAi) },
                    enabled = !state.isMatchEngineBusy,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AutoAwesome,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp),
                        tint = DashboardNavy
                    )
                    Text(
                        text = "Enhance with AI Feedback",
                        maxLines = 2,
                        style = MaterialTheme.typography.labelLarge,
                        color = DashboardNavy
                    )
                }
            }
            if (state.isMatchEngineBusy) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = DashboardNavy
                )
            }
        }

        item(key = "stats_card") {
            RecommendationsDashboardCard(
                stats = state.dashboardStats,
                loading = state.statsLoading,
                error = state.statsError
            )
        }

        item(key = "tabs") {
            DashboardTabRow(
                selectedTab = state.selectedTab,
                onTabSelected = { onEvent(DashboardEvent.TabSelected(it)) }
            )
        }

        when (state.selectedTab) {
            DashboardTab.JobMatches -> {
                if (state.recommendationsLoading) {
                    item(key = "jobs_loading") {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            color = DashboardNavy
                        )
                    }
                } else if (state.recommendationsError != null) {
                    item(key = "jobs_error") {
                        Text(
                            text = state.recommendationsError.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(
                        items = state.jobMatches,
                        key = { it.id }
                    ) { job ->
                        JobMatchCard(
                            job = job,
                            expanded = state.expandedJobIds.contains(job.id),
                            rating = state.jobRatings[job.id] ?: 3,
                            feedbackSubmitted = state.feedbackSubmittedIds.contains(job.id),
                            feedbackSubmitting = state.feedbackSubmittingJobId == job.id,
                            onExpandToggle = { onEvent(DashboardEvent.JobExpandToggled(job.id)) },
                            onRatingChange = { r -> onEvent(DashboardEvent.JobRatingChanged(job.id, r)) },
                            onSubmitFeedback = { onEvent(DashboardEvent.SubmitFeedback(job.id)) }
                        )
                    }
                }
                if (state.feedbackError != null) {
                    item(key = "feedback_err") {
                        Text(
                            text = state.feedbackError.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            DashboardTab.RankingComparison -> {
                item(key = "tab_rank") {
                    RankingComparisonSection(
                        entries = RankingComparisonDummyData.currentRankingTop10
                    )
                }
            }
            DashboardTab.AiResumeInsights -> {
                item(key = "tab_ai") {
                    AiResumeInsightsSection(data = AiResumeInsightsDummyData.insights)
                }
            }
        }
    }
}

@Composable
private fun StepSection(stepNumber: Int, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = DashboardNavy
        ) {
            Text(
                text = "$stepNumber",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun RecommendationsDashboardCard(
    stats: DashboardStats?,
    loading: Boolean,
    error: String?
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, DashboardCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Enhanced Recommendations Dashboard",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Your personalized ranking, powered by feedback-aware AI",
                        style = MaterialTheme.typography.bodySmall,
                        color = DashboardMuted
                    )
                }
                Surface(
                    shape = RoundedCornerShape(50),
                    color = StatTopMatchBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Bolt,
                            contentDescription = null,
                            tint = DashboardNavy,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Live",
                            style = MaterialTheme.typography.labelMedium,
                            color = DashboardNavy,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            when {
                loading -> LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = DashboardNavy
                )
                error != null -> Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
                stats != null -> LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        DashboardStatCard(
                            label = "TOP MATCH",
                            value = "${stats.topMatchPercent}%",
                            subLabel = "Match Score",
                            icon = Icons.Outlined.TrackChanges,
                            iconBackground = StatTopMatchBg,
                            modifier = Modifier.width(148.dp)
                        )
                    }
                    item {
                        DashboardStatCard(
                            label = "RANKED",
                            value = "20",
                            subLabel = "Jobs Found",
                            icon = Icons.Outlined.Layers,
                            iconBackground = StatRankedBg,
                            modifier = Modifier.width(148.dp)
                        )
                    }
                    item {
                        DashboardStatCard(
                            label = "ESTIMATE",
                            value = "${stats.resumeAtsScore}",
                            subLabel = "Resume ATS Score",
                            icon = Icons.Outlined.HealthAndSafety,
                            iconBackground = StatEstimateBg,
                            modifier = Modifier.width(148.dp)
                        )
                    }
                    item {
                        DashboardStatCard(
                            label = "MARKET",
                            value = stats.salaryFitLabel,
                            subLabel = "Salary Fit",
                            icon = Icons.Outlined.AttachMoney,
                            iconBackground = StatMarketBg,
                            modifier = Modifier.width(148.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DashboardScreenPreview() {
    JobFusionTheme(dynamicColor = false) {
        DashboardContent(
            state = DashboardUiState(
                resumePhase = ResumeUploadPhase.Success,
                resumeFileName = "bhargav_4yrs.pdf",
                resumeSuccessMessage = "Resume parsed successfully",
                dashboardStats = DashboardStats(
                    topMatchPercent = 91,
                    rankedJobCount = 20,
                    resumeAtsScore = 84,
                    salaryFitLabel = "High"
                ),
                statsLoading = false,
                jobMatches = listOf(
                    JobMatch(
                        id = "p1",
                        rank = 1,
                        title = "software engineer",
                        tags = listOf("Ngerulmud, Palau", "Temporary"),
                        matchPercent = 91,
                        detailBullets = listOf("Strong stack match", "Remote-friendly team")
                    ),
                    JobMatch(
                        id = "p2",
                        rank = 2,
                        title = "software engineer",
                        tags = listOf("Dili, Timor-Leste", "Temporary"),
                        matchPercent = 77,
                        detailBullets = listOf("Good backend fit")
                    )
                ),
                recommendationsLoading = false,
                jobRatings = mapOf("p1" to 3, "p2" to 4),
                expandedJobIds = setOf("p1")
            ),
            onEvent = {}
        )
    }
}
