package com.example.jobfusion.jobseeker.ui.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.Locale
import com.example.jobfusion.jobseeker.data.dummy.RankingComparisonDummyData
import com.example.jobfusion.jobseeker.domain.model.JobRankingEntry
import com.example.jobfusion.ui.theme.JobFusionTheme

@Composable
fun RankingComparisonSection(
    entries: List<JobRankingEntry>,
    modifier: Modifier = Modifier,
    headerTitle: String? = null
) {
    val resolvedHeader = headerTitle ?: "Current Ranking (Top ${entries.size})"
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append("Click ")
                withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = DashboardNavy)) {
                    append("Enhance with AI Feedback")
                }
                append(" to see how your ratings re-ranked the matches.")
            },
            style = MaterialTheme.typography.bodySmall,
            color = DashboardMuted
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = DashboardNavy
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Layers,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = resolvedHeader,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            entries.forEach { entry ->
                RankingRowCard(entry = entry)
            }
        }
    }
}

@Composable
private fun RankingRowCard(entry: JobRankingEntry) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, DashboardCardBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = DashboardNavy,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${entry.rank}",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )
            }
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = entry.jobTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0B1327)
                )
                Text(
                    text = "score: ${formatRankingScore(entry.score)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = DashboardMuted
                )
            }
        }
    }
}

private fun formatRankingScore(score: Double): String =
    String.format(Locale.US, "%.3f", score)

@Preview(showBackground = true, widthDp = 400)
@Composable
private fun RankingComparisonSectionPreview() {
    JobFusionTheme(dynamicColor = false) {
        RankingComparisonSection(
            entries = RankingComparisonDummyData.currentRankingTop10,
            modifier = Modifier.padding(16.dp)
        )
    }
}
