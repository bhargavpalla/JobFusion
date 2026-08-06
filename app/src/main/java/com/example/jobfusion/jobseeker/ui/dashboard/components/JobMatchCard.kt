package com.example.jobfusion.jobseeker.ui.dashboard.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.jobfusion.jobseeker.domain.model.JobMatch

@Composable
fun JobMatchCard(
    job: JobMatch,
    expanded: Boolean,
    rating: Int,
    feedbackSubmitted: Boolean,
    feedbackSubmitting: Boolean,
    onExpandToggle: () -> Unit,
    onRatingChange: (Int) -> Unit,
    onSubmitFeedback: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 220),
        label = "chevron"
    )

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = BorderStroke(1.dp, DashboardCardBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    color = DashboardNavy,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${job.rank}",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = job.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        job.tags.forEach { tag ->
                            AssistChip(
                                onClick = {},
                                label = {
                                    Text(
                                        text = tag,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                },
                                enabled = false,
                                colors = AssistChipDefaults.assistChipColors(
                                    disabledContainerColor = StatTopMatchBg,
                                    disabledLabelColor = DashboardNavy
                                ),
                                border = null
                            )
                        }
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${job.matchPercent}%",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = DashboardNavy
                    )
                    Text(
                        text = "MATCH",
                        style = MaterialTheme.typography.labelSmall,
                        color = DashboardMuted
                    )
                }
            }

            MatchProgressBar(matchPercent = job.matchPercent)

            TextButton(
                onClick = onExpandToggle,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "View details",
                        color = DashboardNavy,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        tint = DashboardNavy,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(20.dp)
                            .rotate(chevronRotation)
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(tween(220)),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(tween(180))
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    job.detailBullets.forEach { line ->
                        Text(
                            text = "• $line",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            FeedbackSlider(
                rating = rating,
                onRatingChange = onRatingChange,
                enabled = !feedbackSubmitted && !feedbackSubmitting
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = onSubmitFeedback,
                    enabled = !feedbackSubmitted && !feedbackSubmitting
                ) {
                    Text(
                        text = if (feedbackSubmitting) "Submitting…" else "Submit feedback",
                        color = DashboardNavy
                    )
                }
            }
        }
    }
}
