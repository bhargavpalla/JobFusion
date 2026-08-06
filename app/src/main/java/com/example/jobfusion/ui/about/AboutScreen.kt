package com.example.jobfusion.ui.about

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jobfusion.ui.theme.JobFusionTheme

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    onBackToHomeClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF3F5FA))
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "About JobFusion",
            modifier = Modifier
                .background(Color(0xFFF8FAFF), RoundedCornerShape(999.dp))
                .border(1.dp, Color(0xFFDCE3EE), RoundedCornerShape(999.dp))
                .padding(horizontal = 16.dp, vertical = 7.dp),
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF344054)
        )

        Text(
            text = "Built for the future of hiring",
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            color = Color(0xFF0B1327),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "JobFusion is an AI-powered platform that connects talented candidates with the right opportunities and helps recruiters find the right people faster. It combines semantic matching, ATS scoring, and feedback-aware re-ranking into one elegant workspace.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color(0xFF4B5563),
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        AboutFeatureCard(
            icon = Icons.Outlined.Description,
            title = "Resume Analysis",
            description = "Instant ATS scoring, skill extraction, and personalized feedback to make every resume stand out."
        )
        AboutFeatureCard(
            icon = Icons.Outlined.Search,
            title = "Job Recommendations",
            description = "Jobs ranked by location, salary, and experience fit weighted the way you care about them."
        )
        AboutFeatureCard(
            icon = Icons.Outlined.Group,
            title = "Recruiter Tools",
            description = "Upload a JD, rank candidates, filter by salary/experience, and export a clean shortlist in minutes."
        )
        AboutFeatureCard(
            icon = Icons.Outlined.TrendingUp,
            title = "Ranking Intelligence",
            description = "NDCG, Spearman, and reorder metrics show exactly how feedback is sharpening your results."
        )

        Button(
            onClick = onBackToHomeClick,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(0.62f)
        ) {
            Text(
                text = "Back to Home",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Text(
            text = "ENGINEERED TO SHAPE THE FUTURE OF HIRING",
            style = MaterialTheme.typography.labelMedium,
            color = Color(0xFF667085),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
        )
    }
}

@Composable
private fun AboutFeatureCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFDCE3EE), RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(Color(0xFFE9EEFA), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF2D5BD1)
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF111827),
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF4B5563)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AboutScreenPreview() {
    JobFusionTheme(dynamicColor = false) {
        AboutScreen(onBackToHomeClick = {})
    }
}
