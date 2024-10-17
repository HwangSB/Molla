package com.example.molla.analysis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.molla.MollaApp
import com.example.molla.config.Screen
import com.example.molla.websocket.config.WebSocketClient
import com.google.gson.Gson
import okhttp3.WebSocket

@Composable
fun LoadAnalysisPage(navController: NavController) {
    val analysisStepLabel = remember {
        mutableListOf(
            "AI 상담사가 내용을 분석중이에요...",
            "작성한 일기의 감정을 분석중이에요...",
            "일기를 저장하고 있어요..."
        )
    }

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.size(256.dp)
            ) {
                CircularProgressIndicator(
                    progress = { 0.2f },
                    strokeCap = StrokeCap.Round,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                )
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "MOLLA",
                        fontWeight = FontWeight.Bold,
                        fontSize = 48.sp,
                        modifier = Modifier
                            .padding(top = 12.dp)
                    )
                    Text(
                        text = "내 마음을 가장 잘 아는 일기장",
                        fontSize = 13.sp
                    )
                }
            }
            for ((i, label) in analysisStepLabel.withIndex()) {
                val alphaMax = analysisStepLabel.size
                val alpha = (alphaMax - i) / alphaMax.toFloat()
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha),
                )
            }
            Spacer(modifier = Modifier.height(128.dp))
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            Text(
                text = "분석하는 동안 앱을 종료하지 마세요",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoadAnalysisPagePreview() {
    val navController = rememberNavController()
    LoadAnalysisPage(navController)
}