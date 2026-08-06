package com.example.jobfusion.jobseeker.ui.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Sell
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jobfusion.jobseeker.data.dummy.AiResumeInsightsDummyData
import com.example.jobfusion.jobseeker.domain.model.AiResumeInsights
import com.example.jobfusion.jobseeker.domain.model.ImprovementScoreInsight
import com.example.jobfusion.jobseeker.domain.model.MissingSkillsInsight
import com.example.jobfusion.jobseeker.domain.model.StrongSectionsInsight
import com.example.jobfusion.jobseeker.domain.model.SuggestedKeywordsInsight
import com.example.jobfusion.ui.theme.JobFusionTheme

private val InsightTitleColor = Color(0xFF0B1327)
private val MissingSkillsIconBg = Color(0xFFFFF8E6)
private val MissingSkillsIconTint = Color(0xFFE8A317)
private val KeywordChipBg = Color(0xFFE3F2FD)
private val StrongIconBg = Color(0xFFE6F7EC)
private val StrongIconTint = Color(0xFF1A7F37)
private val StrongBulletTint = Color(0xFF1A7F37)
private val NeutralChipBg = Color(0xFFF3F4F6)
private val ImprovementIconBg = Color(0xFFE8F0FF)

private val InsightIconBox = 36.dp
private val InsightIconGlyph = 20.dp
private val CardInnerPadding = 12.dp
private val CardRowGap = 10.dp
private val GridRowGap = 10.dp
private val ChipPadH = 8.dp
private val ChipPadV = 4.dp

@Composable
fun AiResumeInsightsSection(
    data: AiResumeInsights,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(GridRowGap)
    ) {
        Text(
            text = "AI Resume Insights",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = InsightTitleColor
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MissingSkillsCard(
                insight = data.missingSkills,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            SuggestedKeywordsCard(
                insight = data.suggestedKeywords,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            StrongSectionsCard(
                insight = data.strongSections,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
            ImprovementScoreCard(
                insight = data.improvementScore,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun MissingSkillsCard(
    insight: MissingSkillsInsight,
    modifier: Modifier = Modifier
) {
    InsightCardShell(modifier = modifier) {
        InsightCardHeader(
            icon = Icons.Outlined.Sell,
            iconBackground = MissingSkillsIconBg,
            iconTint = MissingSkillsIconTint,
            title = insight.title,
            subtitle = insight.subtitle
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            insight.skillChips.forEach { label ->
                Surface(
                    shape = RoundedCornerShape(50),
                    color = NeutralChipBg
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = InsightTitleColor,
                        modifier = Modifier.padding(horizontal = ChipPadH, vertical = ChipPadV)
                    )
                }
            }
        }
    }
}

@Composable
private fun SuggestedKeywordsCard(
    insight: SuggestedKeywordsInsight,
    modifier: Modifier = Modifier
) {
    InsightCardShell(modifier = modifier) {
        InsightCardHeader(
            icon = Icons.Outlined.AutoAwesome,
            iconBackground = KeywordChipBg,
            iconTint = DashboardNavy,
            title = insight.title,
            subtitle = insight.subtitle
        )
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            insight.keywordChips.chunked(2).forEach { rowChips ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    rowChips.forEach { label ->
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = KeywordChipBg
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = DashboardNavy,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(horizontal = ChipPadH, vertical = ChipPadV)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StrongSectionsCard(
    insight: StrongSectionsInsight,
    modifier: Modifier = Modifier
) {
    InsightCardShell(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(InsightIconBox)
                    .clip(RoundedCornerShape(10.dp))
                    .background(StrongIconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = StrongIconTint,
                    modifier = Modifier.size(InsightIconGlyph)
                )
            }
            Text(
                text = insight.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = InsightTitleColor,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        insight.bulletPoints.forEach { line ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.Top,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Text(
                    text = "•",
                    style = MaterialTheme.typography.bodySmall,
                    color = StrongBulletTint,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = line,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ImprovementScoreCard(
    insight: ImprovementScoreInsight,
    modifier: Modifier = Modifier
) {
    val fraction = (insight.score.toFloat() / insight.scoreMax.toFloat()).coerceIn(0f, 1f)
    InsightCardShell(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(InsightIconBox)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ImprovementIconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.TrendingUp,
                    contentDescription = null,
                    tint = DashboardNavy,
                    modifier = Modifier.size(InsightIconGlyph)
                )
            }
            Text(
                text = insight.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = InsightTitleColor,
                modifier = Modifier.weight(1f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = "${insight.score} / ${insight.scoreMax}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = DashboardNavy
        )
        LinearProgressIndicator(
            progress = { fraction },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = DashboardNavy,
            trackColor = Color(0xFFE5E7EB)
        )
        Text(
            text = insight.description,
            style = MaterialTheme.typography.bodySmall,
            color = DashboardMuted
        )
    }
}

@Composable
private fun InsightCardShell(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 1.dp,
        border = BorderStroke(1.dp, DashboardCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CardInnerPadding),
            verticalArrangement = Arrangement.spacedBy(CardRowGap, Alignment.Top),
            horizontalAlignment = Alignment.Start,
            content = content
        )
    }
}

@Composable
private fun InsightCardHeader(
    icon: ImageVector,
    iconBackground: Color,
    iconTint: Color,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(InsightIconBox)
                .clip(RoundedCornerShape(10.dp))
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(InsightIconGlyph)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = InsightTitleColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = DashboardMuted,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 400)
@Composable
private fun AiResumeInsightsSectionPreview() {
    JobFusionTheme(dynamicColor = false) {
        AiResumeInsightsSection(
            data = AiResumeInsightsDummyData.insights,
            modifier = Modifier.padding(16.dp)
        )
    }
}
