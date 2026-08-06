package com.example.jobfusion.jobseeker.ui.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.jobfusion.jobseeker.ui.dashboard.ResumeUploadPhase

@Composable
fun UploadCard(
    phase: ResumeUploadPhase,
    fileName: String,
    successMessage: String?,
    errorMessage: String?,
    onReplaceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dashColor = DashboardNavy
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind {
                    val stroke = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
                    )
                    drawRoundRect(
                        color = dashColor,
                        style = stroke,
                        cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
                    )
                }
                .clickable(onClick = onReplaceClick, enabled = phase != ResumeUploadPhase.Loading)
                .padding(2.dp),
            shape = RoundedCornerShape(16.dp),
            color = DashboardSurfaceBlue
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 28.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (phase) {
                    ResumeUploadPhase.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(40.dp),
                            color = DashboardNavy,
                            strokeWidth = 3.dp
                        )
                        Text(
                            text = "Uploading…",
                            style = MaterialTheme.typography.bodyMedium,
                            color = DashboardMuted
                        )
                    }
                    else -> {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            shadowElevation = 1.dp
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Upload,
                                contentDescription = null,
                                modifier = Modifier.padding(14.dp),
                                tint = DashboardNavy
                            )
                        }
                        Text(
                            text = fileName.ifBlank { "No file selected" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Click or drop to replace",
                            style = MaterialTheme.typography.bodySmall,
                            color = DashboardMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                if (phase == ResumeUploadPhase.Success && successMessage != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = DashboardSuccess,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = successMessage,
                            style = MaterialTheme.typography.bodyMedium,
                            color = DashboardSuccess
                        )
                    }
                }

                if (phase == ResumeUploadPhase.Error && errorMessage != null) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center
                    )
                }

                if (phase == ResumeUploadPhase.Idle) {
                    Text(
                        text = "PDF or DOCX, up to 10 MB",
                        style = MaterialTheme.typography.bodySmall,
                        color = DashboardMuted,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
