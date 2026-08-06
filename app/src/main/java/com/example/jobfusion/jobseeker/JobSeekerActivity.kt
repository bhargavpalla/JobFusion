package com.example.jobfusion.jobseeker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import com.example.jobfusion.MainActivity
import com.example.jobfusion.jobseeker.ui.JobSeekerRootScreen
import com.example.jobfusion.ui.theme.JobFusionTheme

/**
 * Hosts the signed-in job seeker flow. Launched after successful job seeker auth; [MainActivity] is
 * finished so auth is not kept on the back stack.
 */
class JobSeekerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobFusionTheme {
                JobSeekerRootScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing),
                    onSignedOut = {
                        startActivity(
                            Intent(this, MainActivity::class.java).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            }
                        )
                        finish()
                    }
                )
            }
        }
    }
}
