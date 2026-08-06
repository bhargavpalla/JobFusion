package com.example.jobfusion.jobseeker.ui.dashboard.components

import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.jobfusion.jobseeker.ui.dashboard.DashboardTab

@Composable
fun DashboardTabRow(
    selectedTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf(
        DashboardTab.JobMatches to "Job Matches",
        DashboardTab.RankingComparison to "Ranking Comparison",
        DashboardTab.AiResumeInsights to "AI Resume Insights"
    )
    val selectedIndex = tabs.indexOfFirst { it.first == selectedTab }.coerceAtLeast(0)

    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        contentColor = DashboardNavy,
        indicator = { tabPositions ->
            if (selectedIndex < tabPositions.size) {
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                    height = 3.dp,
                    color = DashboardNavy
                )
            }
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, (tab, label) ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = label,
                        fontWeight = if (selectedIndex == index) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selectedContentColor = DashboardNavy,
                unselectedContentColor = DashboardMuted
            )
        }
    }
}
