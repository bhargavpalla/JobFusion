package com.example.jobfusion.recruiter

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.ui.Modifier
import com.example.jobfusion.MainActivity
import com.example.jobfusion.recruiter.ui.RecruiterRootScreen
import com.example.jobfusion.ui.theme.JobFusionTheme

class RecruiterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobFusionTheme {
                RecruiterRootScreen(
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
