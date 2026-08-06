package com.example.jobfusion.recruiter.ui

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
import com.example.jobfusion.recruiter.ui.dashboard.RecruiterDashboardRoute
import com.example.jobfusion.ui.auth.AuthDependencies
import kotlinx.coroutines.launch

private enum class RecruiterMenuTab {
    Dashboard,
    Pipeline
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecruiterRootScreen(
    modifier: Modifier = Modifier,
    onSignedOut: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val appContext = LocalContext.current.applicationContext
    val tokenRepository = remember(appContext) { AuthDependencies.provideTokenRepository(appContext) }
    var selectedTab by remember { mutableStateOf(RecruiterMenuTab.Dashboard) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text(
                    text = "Recruiter",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
                )
                NavigationDrawerItem(
                    label = { Text("Smart ranking") },
                    selected = selectedTab == RecruiterMenuTab.Dashboard,
                    onClick = {
                        selectedTab = RecruiterMenuTab.Dashboard
                        scope.launch { drawerState.close() }
                    }
                )
                NavigationDrawerItem(
                    label = { Text("Pipeline") },
                    selected = selectedTab == RecruiterMenuTab.Pipeline,
                    onClick = {
                        selectedTab = RecruiterMenuTab.Pipeline
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
                            if (selectedTab == RecruiterMenuTab.Dashboard) {
                                "Recruiter dashboard"
                            } else {
                                "Pipeline"
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
                RecruiterMenuTab.Dashboard -> RecruiterDashboardRoute(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
                RecruiterMenuTab.Pipeline -> Text(
                    text = "Pipeline screen coming soon.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(20.dp)
                )
            }
        }
    }
}
