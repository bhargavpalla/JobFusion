package com.example.jobfusion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.example.jobfusion.ui.splash.SplashScreen
import com.example.jobfusion.ui.theme.JobFusionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobFusionTheme {
                SplashScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}