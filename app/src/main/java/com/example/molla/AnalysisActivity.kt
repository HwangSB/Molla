package com.example.molla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.molla.ui.theme.EmotionAngry
import com.example.molla.ui.theme.EmotionHappy
import com.example.molla.ui.theme.EmotionHurt
import com.example.molla.ui.theme.EmotionInsecure
import com.example.molla.ui.theme.EmotionSad
import com.example.molla.ui.theme.MollaTheme

class AnalysisActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { AnalysisActivityContent() }
    }
}

@Composable
fun AnalysisActivityContent() {
//    var isLoading by remember { mutableStateOf(true) }

    MollaTheme {
        Scaffold { innerPadding ->
            AnalysisPage(modifier = Modifier.padding(innerPadding))
//            AnimatedVisibility(
//                visible = isLoading,
//                enter = fadeIn(),
//                exit = fadeOut()
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(MaterialTheme.colorScheme.surface)
//                ) {
//                    LoadingPage(modifier = Modifier.padding(innerPadding))
//                }
//            }
        }
    }
}

@Composable
fun AnalysisPage(modifier: Modifier) {
    // TODO: Get analysis result from Server
    val analysisResult = 1

    val emotionText = when (analysisResult) {
        0 -> "분노"
        1 -> "불안"
        2 -> "슬픔"
        3 -> "상처"
        4 -> "행복"
        else -> "분석 오류"
    }

    val brush = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            when (analysisResult) {
                0 -> EmotionAngry
                1 -> EmotionInsecure
                2 -> EmotionSad
                3 -> EmotionHurt
                4 -> EmotionHappy
                else -> MaterialTheme.colorScheme.outlineVariant
            }
        )
    )
    val contrastColor = when (analysisResult) {
        0 -> MaterialTheme.colorScheme.onPrimary
        1 -> MaterialTheme.colorScheme.onSurface
        2 -> MaterialTheme.colorScheme.onPrimary
        3 -> MaterialTheme.colorScheme.onPrimary
        4 -> MaterialTheme.colorScheme.onSurface
        else -> MaterialTheme.colorScheme.onSurface
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(brush)
            .then(modifier)
    ) {
        item {
            Spacer(modifier = Modifier.height(128.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emotionText,
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(64.dp))
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                text = "현재 불안한 감정을 느끼고 있으신 것 같아요.\n" +
                        "불안함은 스트레스 호르몬인 코르티솔을 증가시키고,\n" +
                        "뭐시기 뭐시기 해서 정신 건강에 좋지 않은 영향을 줄 수 있어요.\n\n" +
                        "최근 일주일 사이에 불안한 감정이 무려 4번 나타났어요.\n\n" +
                        "의학 머시기에 따르면 불안함 또는 우울한 감정이 한달 동안 2주 이상 지속될 경우 우울증으로 판단하고 있어요.\n\n" +
                        "AI 상담사가 아래에 분석 리포트를 작성했으니 함께 확인해 보세요.",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(32.dp))
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                DashboardCard("내 감정 분포", "7일간 감정 상태 추이") {
                    EmotionBarChart(
                        status = "불안",
                        caution = "주의가 필요합니다",
                        angry = 2, insecure = 4,
                        sad = 0, hurt = 0, happy = 1,
                        modifier = Modifier.height(128.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                DashboardCard("다른 사용자들의 주요 감정 분포", "7일간 감정 상태 추이") {
                    ForumEmotionBarChart(
                        angryUserMean = 4, angry = 2,
                        insecureUserMean = 3, insecure = 4,
                        sadUserMean = 0, sad = 0,
                        hurtUserMean = 0, hurt = 0,
                        happyUserMean = 0, happy = 1,
                        modifier = Modifier.height(128.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "MOLLA의 AI 상담사는 의학적 사실을 검증하지 않습니다. 사용에 주의가 필요합니다.",
                style = MaterialTheme.typography.bodySmall,
                fontSize = 8.sp,
                textAlign = TextAlign.Center,
                color = contrastColor.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ForumEmotionBarChart(
    angryUserMean: Int,
    angry: Int,
    insecureUserMean: Int,
    insecure: Int,
    sadUserMean: Int,
    sad: Int,
    hurtUserMean: Int,
    hurt: Int,
    happyUserMean: Int,
    happy: Int,
    modifier: Modifier = Modifier
) {
    val defaultColor = MaterialTheme.colorScheme.outlineVariant
    val emotionColors = listOf(EmotionAngry, EmotionInsecure, EmotionSad, EmotionHurt, EmotionHappy)
    val myEmotions = listOf(angry, insecure, sad, hurt, happy)
    val forumEmotions = listOf(angryUserMean, insecureUserMean, sadUserMean, hurtUserMean, happyUserMean)
    val maxVal = (myEmotions + forumEmotions).maxOrNull() ?: 1

    Column {
        Row(modifier = modifier) {
            for (i in 0..4) {
                val forumBackgroundColor = if (forumEmotions[i] > 0) { emotionColors[i] } else { defaultColor }
                val myBackgroundColor = if (myEmotions[i] > 0) { emotionColors[i] } else { defaultColor }

                Column(
                    modifier = Modifier
                        .weight(3f)
                        .align(Alignment.Bottom)
                ) {
                    Text(
                        text = "${forumEmotions[i]}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 8.dp)
                            .fillMaxHeight(forumEmotions[i] / maxVal.toFloat())
                            .background(forumBackgroundColor, RoundedCornerShape(8.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.Bottom)
                ) {
                    Text(
                        text = "${myEmotions[i]}",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 8.dp)
                            .fillMaxHeight(myEmotions[i] / maxVal.toFloat())
                            .background(myBackgroundColor, RoundedCornerShape(8.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
        Row {
            for (i in 0..9) {
                val text = when {
                    i % 2 == 0 -> "사용자"
                    else -> "나"
                }
                val weight = when {
                    i % 2 == 0 -> 3f
                    else -> 1f
                }
                Text(
                    modifier = Modifier.weight(weight),
                    text = text,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnalysisActivityPreview() {
    AnalysisActivityContent()
}