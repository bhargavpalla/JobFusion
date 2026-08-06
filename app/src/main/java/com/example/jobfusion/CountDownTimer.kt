package com.example.jobfusion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@Composable
fun CountDownTimer() {

    var timeLeft by rememberSaveable {
        mutableIntStateOf(50)
    }

    var startTimer by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(startTimer) {
        if(startTimer) {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
        }
        startTimer = false
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "$timeLeft",
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                startTimer = true
            },
            enabled = (!startTimer && timeLeft >0)
        ) {
            Text("Start")
        }
    }
}


@Composable
fun LaunchedEffectCode(
    key: Any?,
    block: suspend  () -> Unit
) {
    val scope = rememberCoroutineScope()
    DisposableEffect(key) {
        val job = scope.launch {
            //
            supervisorScope {
                scope.launch {
                    block()
                }
            }
        }
        onDispose {
            job.cancel()
        }
    }

}