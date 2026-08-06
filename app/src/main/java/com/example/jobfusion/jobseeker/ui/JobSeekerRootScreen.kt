package com.example.jobfusion.jobseeker.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.jobfusion.jobseeker.ui.dashboard.DashboardScreen
import com.example.jobfusion.jobseeker.ui.preferences.JobSeekerPreferencesRoute
import com.example.jobfusion.ui.auth.AuthDependencies
import kotlinx.coroutines.launch

private enum class JobSeekerMainTab {
    Home,
    Preferences,
    SavedJobs
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobSeekerRootScreen(
    modifier: Modifier = Modifier,
    onSignedOut: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(JobSeekerMainTab.Home) }
    val appContext = LocalContext.current.applicationContext
    val tokenRepository = remember(appContext) { AuthDependencies.provideTokenRepository(appContext) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Job seeker",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Dashboard") },
                    selected = selectedTab == JobSeekerMainTab.Home,
                    onClick = {
                        selectedTab = JobSeekerMainTab.Home
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Job preferences") },
                    selected = selectedTab == JobSeekerMainTab.Preferences,
                    onClick = {
                        selectedTab = JobSeekerMainTab.Preferences
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Saved jobs") },
                    selected = selectedTab == JobSeekerMainTab.SavedJobs,
                    onClick = {
                        selectedTab = JobSeekerMainTab.SavedJobs
                        scope.launch { drawerState.close() }
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                NavigationDrawerItem(
                    label = { Text("Sign out") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            tokenRepository.clear()
                            drawerState.close()
                            onSignedOut()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when (selectedTab) {
                                JobSeekerMainTab.Home -> "Dashboard"
                                JobSeekerMainTab.Preferences -> "Job preferences"
                                JobSeekerMainTab.SavedJobs -> "Saved jobs"
                            }
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = "Open menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            when (selectedTab) {
                JobSeekerMainTab.Home ->
                    DashboardScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                JobSeekerMainTab.Preferences ->
                    JobSeekerPreferencesRoute(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                JobSeekerMainTab.SavedJobs ->
                    Text(
                        text = "Saved jobs will appear here.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(24.dp)
                    )
            }
        }
    }
}
