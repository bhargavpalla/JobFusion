package com.example.jobfusion.recruiter.ui.dashboard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.QueryStats
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.auth.repository.CandidateRankingItem
import com.example.domain.auth.repository.RecruiterControls
import com.example.domain.auth.repository.RecruiterDashboardStats
import com.example.jobfusion.ui.theme.JobFusionTheme
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.data.dummy.RecruiterDashboardDummyData
import java.util.Locale

@Composable
fun RecruiterDashboardRoute(
    modifier: Modifier = Modifier,
    viewModel: RecruiterDashboardViewModel = viewModel(
        factory = RecruiterDashboardViewModelFactory(RecruiterDashboardDependencies.provideRepository())
    )
) {
    val state by viewModel.uiState.collectAsState()
    RecruiterDashboardScreen(
        state = state,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
fun RecruiterDashboardScreen(
    state: RecruiterDashboardUiState,
    onEvent: (RecruiterDashboardEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val expandedCandidateId = state.selectedCandidate?.candidateId
    var selectedTab by rememberSaveable { mutableStateOf(RecruiterDashboardTab.CandidateRanking) }

    Column(
        modifier = modifier
            .background(Color(0xFFF8FAFF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Smart Candidate Ranking Dashboard",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Feedback-aware ranking with live intelligence metrics",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF667085)
        )

        JdControlsRow(
            controls = state.controls,
            onEvent = onEvent
        )

        SmartFiltersRow(
            controls = state.controls,
            onEvent = onEvent
        )

        StatsCardsRow(stats = state.stats)

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            border = BorderStroke(1.dp, Color(0xFFE4EAF5))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                RankingHeader(onRunRanking = { onEvent(RecruiterDashboardEvent.RunRanking) }, isRunningRanking = state.isRunningRanking)
                DashboardTabs(
                    selectedTab = selectedTab,
                    onTabChanged = { selectedTab = it }
                )

                when (selectedTab) {
                    RecruiterDashboardTab.CandidateRanking -> {
                        RankingTableHeader()
                        if (state.isLoading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        } else {
                            state.rankings.forEach { candidate ->
                                CandidateRow(
                                    item = candidate,
                                    isExpanded = expandedCandidateId == candidate.candidateId,
                                    onClick = { onEvent(RecruiterDashboardEvent.CandidateSelected(candidate)) }
                                )
                            }
                        }
                    }
                    RecruiterDashboardTab.RankingIntelligence -> {
                        RankingIntelligenceSection(rankings = state.rankings)
                    }
                }
            }
        }

        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

    }
}

private enum class RecruiterDashboardTab {
    CandidateRanking,
    RankingIntelligence
}

@Composable
private fun DashboardTabs(
    selectedTab: RecruiterDashboardTab,
    onTabChanged: (RecruiterDashboardTab) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabPill(
            label = "Candidate Ranking",
            isSelected = selectedTab == RecruiterDashboardTab.CandidateRanking,
            onClick = { onTabChanged(RecruiterDashboardTab.CandidateRanking) }
        )
        Spacer(Modifier.width(8.dp))
        TabPill(
            label = "Ranking Intelligence",
            isSelected = selectedTab == RecruiterDashboardTab.RankingIntelligence,
            onClick = { onTabChanged(RecruiterDashboardTab.RankingIntelligence) }
        )
    }
}

@Composable
private fun TabPill(label: String, isSelected: Boolean, onClick: () -> Unit) {
    val textColor = if (isSelected) Color(0xFF175CD3) else Color(0xFF667085)
    val underline = if (isSelected) Color(0xFF175CD3) else Color.Transparent
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp)
    ) {
        Text(text = label, color = textColor, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Surface(
            color = underline,
            modifier = Modifier
                .width(96.dp)
                .height(2.dp)
        ) {}
    }
}

@Composable
private fun JdControlsRow(
    controls: RecruiterControls,
    onEvent: (RecruiterDashboardEvent) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE4EAF5))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Paste Job Description",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            OutlinedTextField(
                value = controls.jobDescriptionText,
                onValueChange = { onEvent(RecruiterDashboardEvent.JobDescriptionChanged(it)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                singleLine = false,
                maxLines = 8,
                placeholder = { Text("Paste the JD here....") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD5DEE9),
                    unfocusedBorderColor = Color(0xFFDDE4EE)
                )
            )
            Text(
                text = "Or upload JD (PDF)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEvent(RecruiterDashboardEvent.UploadJdTapped) },
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF6FAFF),
                border = BorderStroke(1.dp, Color(0xFFAFC7FF))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Outlined.UploadFile, contentDescription = null, tint = Color(0xFF2C6CF6))
                    Text(
                        text = controls.uploadedJdFileName ?: "Choose PDF file",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2C6CF6),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun SmartFiltersRow(
    controls: RecruiterControls,
    onEvent: (RecruiterDashboardEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(bottom = 2.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        FilterField(
            title = "Top K Candidates",
            value = controls.topK.toString(),
            width = 140.dp,
            keyboardType = KeyboardType.Number,
            leadingIcon = { Icon(Icons.Outlined.Tune, contentDescription = null, tint = Color(0xFF667085)) },
            onValueChange = { onEvent(RecruiterDashboardEvent.TopKChanged(it.filter(Char::isDigit).toIntOrNull() ?: 1)) }
        )
        FilterField(
            title = "Max Salary",
            value = controls.maxSalary,
            width = 150.dp,
            keyboardType = KeyboardType.Number,
            leadingIcon = { Icon(Icons.Outlined.AttachMoney, contentDescription = null, tint = Color(0xFF667085)) },
            onValueChange = { onEvent(RecruiterDashboardEvent.MaxSalaryChanged(it)) }
        )
        FilterField(
            title = "Min Exp",
            value = controls.minExperienceYears,
            width = 130.dp,
            keyboardType = KeyboardType.Number,
            leadingIcon = { Icon(Icons.Outlined.BusinessCenter, contentDescription = null, tint = Color(0xFF667085)) },
            onValueChange = { onEvent(RecruiterDashboardEvent.MinExperienceChanged(it)) }
        )
        FilterField(
            title = "Location",
            value = controls.location,
            width = 170.dp,
            leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Color(0xFF667085)) },
            onValueChange = { onEvent(RecruiterDashboardEvent.LocationChanged(it)) }
        )
    }
}

@Composable
private fun FilterField(
    title: String,
    value: String,
    width: androidx.compose.ui.unit.Dp,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable () -> Unit,
    onValueChange: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFDDE4EE))
    ) {
        Column(
            modifier = Modifier
                .width(width)
                .padding(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = Color(0xFF667085),
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 2.dp)
            )
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                leadingIcon = leadingIcon,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFD5DEE9),
                    unfocusedBorderColor = Color(0xFFDDE4EE)
                )
            )
        }
    }
}

@Composable
private fun StatsCardsRow(stats: RecruiterDashboardStats) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        StatCard(title = "Candidates Screened", value = "${stats.candidatesScreened}", icon = Icons.Outlined.Groups, iconBg = Color(0xFFEFF4FF))
        StatCard(title = "Top Matches", value = "${stats.topMatches}", icon = Icons.Outlined.Star, iconBg = Color(0xFFECFDF3))
        StatCard(title = "Avg Match Score", value = "${stats.avgMatchScorePercent}%", icon = Icons.Outlined.QueryStats, iconBg = Color(0xFFEFF4FF))
        StatCard(title = "Hiring Confidence", value = "${stats.hiringConfidencePercent}%", icon = Icons.Outlined.Shield, iconBg = Color(0xFFFFFBEA))
    }
}

@Composable
private fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconBg: Color
) {
    Surface(
        modifier = Modifier.width(170.dp),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, Color(0xFFE4EAF5)),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(shape = RoundedCornerShape(10.dp), color = iconBg) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF175CD3),
                    modifier = Modifier.padding(8.dp)
                )
            }
            Text(title, style = MaterialTheme.typography.bodySmall, color = Color(0xFF667085))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun RankingHeader(onRunRanking: () -> Unit, isRunningRanking: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Candidate Ranking", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        Button(
            onClick = onRunRanking,
            enabled = !isRunningRanking,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E459B))
        ) {
            if (isRunningRanking) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = Color.White)
            } else {
                Text("Submit & Re-rank")
            }
        }
    }
    HorizontalDivider(color = Color(0xFFEEF2F8))
}

@Composable
private fun RankingTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderText("CANDIDATE", Modifier.weight(1.5f))
        HeaderText("MATCH %", Modifier.weight(0.8f))
        HeaderText("SALARY", Modifier.weight(1.2f))
        HeaderText("SKILLS", Modifier.weight(1f))
    }
    HorizontalDivider(color = Color(0xFFEEF2F8))
}

@Composable
private fun RankingIntelligenceSection(rankings: List<CandidateRankingItem>) {
    val top = rankings.take(4)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IntelligenceMetricCard("NDCG BEFORE", "0.0000", Color(0xFFEFF4FF), Icons.Outlined.QueryStats)
            IntelligenceMetricCard("NDCG AFTER", "0.0000", Color(0xFFEFF4FF), Icons.Outlined.QueryStats)
            IntelligenceMetricCard("LIFT (NDCG)", "0.0000", Color(0xFFECFDF3), Icons.Outlined.Psychology)
            IntelligenceMetricCard("AI CONFIDENCE", "96%", Color(0xFFFFFBEA), Icons.Outlined.Shield)
        }
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IntelligenceMetricCard("SPEARMAN RANK", "1.0000", Color(0xFFEFF4FF), Icons.Outlined.QueryStats)
            IntelligenceMetricCard("REORDERED", "0%", Color(0xFFEFF4FF), Icons.Outlined.SwapVert)
        }

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            IntelligenceListCard(
                title = "Before Feedback",
                subtitle = "BASELINE",
                scorePrefix = "score",
                items = top,
                modifier = Modifier.width(320.dp)
            )
            IntelligenceListCard(
                title = "After Feedback",
                subtitle = "",
                scorePrefix = "adj",
                items = top,
                modifier = Modifier.width(320.dp)
            )
        }
    }
}

@Composable
private fun IntelligenceMetricCard(
    title: String,
    value: String,
    bgColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.width(170.dp),
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(1.dp, Color(0xFFE4EAF5))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Surface(shape = RoundedCornerShape(8.dp), color = Color.White.copy(alpha = 0.7f)) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF175CD3),
                    modifier = Modifier.padding(6.dp)
                )
            }
            Text(title, style = MaterialTheme.typography.labelSmall, color = Color(0xFF667085))
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF101828))
        }
    }
}

@Composable
private fun IntelligenceListCard(
    title: String,
    subtitle: String,
    scorePrefix: String,
    items: List<CandidateRankingItem>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFE4EAF5))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0F172A))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
                if (subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
                        color = Color(0xFF98A2B3),
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                }
            }
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFF1E459B)) {
                        Text(
                            text = "${index + 1}",
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(item.candidateName, fontWeight = FontWeight.SemiBold)
                        Text("$scorePrefix: ${String.format(Locale.US, "%.3f", item.score)}", color = Color(0xFF667085))
                    }
                }
                if (index != items.lastIndex) HorizontalDivider(color = Color(0xFFEEF2F8))
            }
        }
    }
}

@Composable
private fun HeaderText(label: String, modifier: Modifier = Modifier) {
    Text(
        text = label,
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = Color(0xFF667085),
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun CandidateRow(
    item: CandidateRankingItem,
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val background = if (isExpanded) Color(0xFFF4F8FF) else Color.White
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.weight(1.5f),
                verticalAlignment = Alignment.CenterVertically
            ) {
//                Surface(shape = CircleShape, color = Color(0xFF1E459B)) {
//                    Text(
//                        item.avatarInitial.uppercase(Locale.US),
//                        color = Color.White,
//                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
//                        style = MaterialTheme.typography.labelLarge
//                    )
//                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = item.candidateName.firstNameOnly(),
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            PillText("${(item.score * 100).toInt()}%", modifier = Modifier.weight(0.8f))
            InfoText("₹${formatMoney(item.currentSalary)}", modifier = Modifier.weight(1.2f))
            InfoText("${item.skillsMatchPercent}%", modifier = Modifier.weight(1f))
        }

        if (isExpanded) {
            Spacer(modifier = Modifier.height(10.dp))
            CandidateDetailPanel(candidate = item)
        }
    }
    HorizontalDivider(color = Color(0xFFEEF2F8))
}

@Composable
private fun InfoText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium,
        color = Color(0xFF344054)
    )
}

@Composable
private fun PillText(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        color = Color(0xFFEFFAF5),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, Color(0xFFD1FADF))
    ) {
        Text(
            text = text,
            color = Color(0xFF067647),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CandidateDetailPanel(candidate: CandidateRankingItem) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FAFF),
        border = BorderStroke(1.dp, Color(0xFFE4EAF5)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = candidate.candidateName,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = candidate.role,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF667085)
            )
            Text(
                text = "₹${formatMoney(candidate.currentSalary)} · ${candidate.location}",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF667085)
            )

            Text(
                text = "Resume summary",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF667085)
            )
            Text(candidate.resumeSummary, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF344054))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ContactChip(candidate.email)
                ContactChip(candidate.phone)
            }
        }
    }
}

@Composable
private fun ContactChip(value: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF2F4F7),
        border = BorderStroke(1.dp, Color(0xFFE4E7EC))
    ) {
        Text(value, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), style = MaterialTheme.typography.bodySmall)
    }
}

private fun formatMoney(amount: Int): String = String.format(Locale.US, "%,d", amount)

private fun String.firstNameOnly(): String =
    trim().split(Regex("\\s+")).firstOrNull().orEmpty().ifBlank { this }

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RecruiterDashboardPreview() {
    JobFusionTheme(dynamicColor = false) {
        RecruiterDashboardScreen(
            state = RecruiterDashboardUiState(
                controls = RecruiterDashboardDummyData.controls,
                stats = RecruiterDashboardDummyData.stats,
                rankings = RecruiterDashboardDummyData.candidates,
                selectedCandidate = RecruiterDashboardDummyData.candidates[1],
                isLoading = false
            ),
            onEvent = {}
        )
    }
}
