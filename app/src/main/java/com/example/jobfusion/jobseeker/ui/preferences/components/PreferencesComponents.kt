package com.example.jobfusion.jobseeker.ui.preferences.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val BrandBlue = Color(0xFF1E459B)
private val IconTileBg = Color(0xFFE9EEFA)
private val CardBorder = Color(0xFFDCE3EE)
private val LabelMuted = Color(0xFF667085)

@Composable
fun SectionHeader(
    icon: ImageVector,
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(IconTileBg, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrandBlue,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0B1327)
        )
    }
}

@Composable
fun PreferenceInputCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = LabelMuted
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = LabelMuted
                )
            },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandBlue,
                unfocusedBorderColor = CardBorder,
                cursorColor = BrandBlue
            ),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF111827))
        )
    }
}

@Composable
fun WeightSlider(
    label: String,
    percent: Int,
    onPercentChange: (Int) -> Unit,
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
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF111827)
            )
            Text(
                text = "$percent%",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = BrandBlue
            )
        }
        Slider(
            value = percent.toFloat(),
            onValueChange = { onPercentChange(it.toInt().coerceIn(0, 100)) },
            valueRange = 0f..100f,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                activeTrackColor = BrandBlue,
                inactiveTrackColor = Color(0xFFE5E7EB),
                thumbColor = Color.White,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            )
        )
    }
}
