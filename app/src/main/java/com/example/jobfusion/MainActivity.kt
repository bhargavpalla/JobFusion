package com.example.jobfusion

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.domain.auth.model.UserRole
import com.example.jobfusion.jobseeker.JobSeekerActivity
import com.example.jobfusion.recruiter.RecruiterActivity
import com.example.jobfusion.ui.about.AboutScreen
import com.example.jobfusion.ui.auth.AuthDependencies
import com.example.jobfusion.ui.auth.AuthScreen
import com.example.jobfusion.ui.splash.SplashScreen
import com.example.jobfusion.ui.theme.JobFusionTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
//tic ta
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobFusionTheme {
               // CountDownTimer()
                JobFusionApp(modifier = Modifier.fillMaxSize())
                //TicTacToeScreen(3)
            }
        }
    }
}

@Composable
private fun JobFusionApp(modifier: Modifier = Modifier) {
    var showSplash by remember { mutableStateOf(true) }
    var showAbout by remember { mutableStateOf(false) }
    val activity = LocalContext.current as ComponentActivity
    val appContext = LocalContext.current.applicationContext
    val sessionRepository = remember(appContext) {
        AuthDependencies.provideTokenRepository(appContext)
    }

    LaunchedEffect(Unit) {
        val session = withContext(Dispatchers.IO) {
            sessionRepository.getCurrentSession()
        }
        delay(3_000)
        when (session?.role) {
            UserRole.JOB_SEEKER -> {
                activity.startActivity(Intent(activity, JobSeekerActivity::class.java))
                activity.finish()
            }
            UserRole.RECRUITER -> {
                activity.startActivity(Intent(activity, RecruiterActivity::class.java))
                activity.finish()
            }
            null -> {
                showSplash = false
            }
        }
    }

    when {
        showSplash -> SplashScreen(modifier = modifier)
        showAbout -> AboutScreen(
            modifier = modifier,
            onBackToHomeClick = { showAbout = false }
        )
        else -> AuthScreen(
            modifier = modifier,
            onAboutClick = { showAbout = true },
            onJobSeekerAuthenticated = {
                activity.startActivity(Intent(activity, JobSeekerActivity::class.java))
                activity.finish()
            },
            onRecruiterAuthenticated = {
                activity.startActivity(Intent(activity, RecruiterActivity::class.java))
                activity.finish()
            }
        )
    }
}
