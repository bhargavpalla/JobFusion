package com.example.jobfusion.jobseeker.ui.dashboard.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun MatchProgressBar(
    matchPercent: Int,
    modifier: Modifier = Modifier
) {
    val fraction = (matchPercent.coerceIn(0, 100)) / 100f
    LinearProgressIndicator(
        progress = { fraction },
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
        color = DashboardNavy,
        trackColor = androidx.compose.ui.graphics.Color(0xFFE5E7EB),
    )
}
