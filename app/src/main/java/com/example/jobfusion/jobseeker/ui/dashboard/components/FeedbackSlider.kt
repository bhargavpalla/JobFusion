package com.example.jobfusion.jobseeker.ui.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FeedbackSlider(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Your rating",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${rating.coerceIn(1, 5)}/5",
                style = MaterialTheme.typography.labelLarge,
                color = DashboardNavy
            )
        }
        Slider(
            value = rating.toFloat(),
            onValueChange = { onRatingChange(it.toInt().coerceIn(1, 5)) },
            valueRange = 1f..5f,
            steps = 3,
            enabled = enabled,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.surface,
                activeTrackColor = DashboardNavy,
                inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        )
    }
}
