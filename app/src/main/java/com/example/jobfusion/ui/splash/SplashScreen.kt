package com.example.jobfusion.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jobfusion.ui.theme.JobFusionTheme

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        SplashScreenDefaults.GradientStart,
                        SplashScreenDefaults.GradientEnd
                    )
                )
            )
            .padding(horizontal = 24.dp, vertical = 28.dp)
    ) {
        val headingStyle = if (maxWidth < 360.dp) {
            MaterialTheme.typography.headlineMedium
        } else {
            MaterialTheme.typography.headlineLarge
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SplashHeader()
            SplashContent(
                headingStyle = headingStyle
            )
            SplashStatsRow()
        }
    }
}

@Composable
private fun SplashHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SplashScreenDefaults.LogoBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "JF",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = SplashScreenDefaults.PrimaryText
            )
        }
        Spacer(modifier = Modifier.size(12.dp))
        Text(
            text = "JobFusion",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = SplashScreenDefaults.PrimaryText
        )
    }
}

@Composable
private fun SplashContent(
    headingStyle: androidx.compose.ui.text.TextStyle,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = SplashScreenDefaults.Heading,
            style = headingStyle,
            fontWeight = FontWeight.Bold,
            color = SplashScreenDefaults.PrimaryText
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = SplashScreenDefaults.SubHeading,
            style = MaterialTheme.typography.bodyLarge,
            color = SplashScreenDefaults.SecondaryText
        )
    }
}

@Composable
private fun SplashStatsRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        StatItem(
            value = "92%",
            label = "AVG MATCH",
            modifier = Modifier.weight(1f)
        )
        StatItem(
            value = "48K+",
            label = "JOBS INDEXED",
            modifier = Modifier.weight(1f)
        )
        StatItem(
            value = "3.2x",
            label = "FASTER SHORTLISTING",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = SplashScreenDefaults.PrimaryText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = SplashScreenDefaults.SecondaryText,
            textAlign = TextAlign.Center
        )
    }
}

private object SplashScreenDefaults {
    val GradientStart = Color(0xFF0B2A70)
    val GradientEnd = Color(0xFF123E96)
    val LogoBackground = Color(0x1FFFFFFF)
    val PrimaryText = Color(0xFFF5F7FF)
    val SecondaryText = Color(0xB3E1E8FF)

    const val Heading = "Hiring, reimagined with AI that actually learns from feedback."
    const val SubHeading = "Join candidates and recruiters using JobFusion to match smarter, faster, and with clear ranking intelligence."
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SplashScreenPreview() {
    JobFusionTheme(darkTheme = true, dynamicColor = false) {
        SplashScreen()
    }
}
