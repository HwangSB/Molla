package com.example.molla

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.molla.ui.theme.EmotionAngry
import com.example.molla.ui.theme.EmotionHappy
import com.example.molla.ui.theme.EmotionHurt
import com.example.molla.ui.theme.EmotionInsecure
import com.example.molla.ui.theme.EmotionSad

@Composable
fun JournalPage(modifier: Modifier, isDashboardOpened: Boolean) {
    // TODO: Load journal data from server

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AnimatedVisibility(
                visible = isDashboardOpened,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Text(
                        text = "대시보드",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                    DashboardCard()
                }
            }
        }
        item {
            Text(
                text = "2024년 7월",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            LabeledHorizontalDivider("25일")
        }
        items(1) { index ->
            ExpandableCard(
                index,
                Journal(
                    title = "Hello, Journal!",
                    content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    images = listOf(
                        ImageBitmap.imageResource(R.drawable.login_naver),
                        ImageBitmap.imageResource(R.drawable.login_naver),
                    ),
                    emotionType = 0,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
        item {
            LabeledHorizontalDivider("24일")
        }
        items(3) { index ->
            ExpandableCard(
                index,
                Journal(
                    title = "Hello, Journal Journal Journal Journal Journal Journal Journal Journal Journal Journal Journal $index!",
                    content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                    images = listOf(
                        ImageBitmap.imageResource(R.drawable.login_naver),
                        ImageBitmap.imageResource(R.drawable.login_naver),
                        ImageBitmap.imageResource(R.drawable.login_naver),
                        ImageBitmap.imageResource(R.drawable.login_naver),
                        ImageBitmap.imageResource(R.drawable.login_naver)
                    ),
                    emotionType = 0,
                    timestamp = System.currentTimeMillis()
                )
            )
        }

        // Bottom padding for the last item
        item { Spacer(modifier = Modifier.height(0.dp)) }
    }
}

data class Journal(
    val title: String,
    val content: String,
    val images: List<ImageBitmap>,
    val emotionType: Int,
    val timestamp: Long
)

@Composable
fun ExpandableCard(index: Int, journal: Journal) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text(
                    text = journal.title,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f),
                    maxLines = if (!expanded) { 1 } else { Int.MAX_VALUE },
                    overflow = if (!expanded) { TextOverflow.Ellipsis } else { TextOverflow.Clip }
                )
                Box(modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .width(16.dp)
                    .height(16.dp)
                    .background(EmotionAngry, RoundedCornerShape(8.dp))
                    .align(Alignment.CenterVertically)
                )
            }
            AdaptiveImageCard(
                images = journal.images
            )
            Text(
                text = journal.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
                maxLines = if (!expanded) { 3 } else { Int.MAX_VALUE },
                overflow = if (!expanded) { TextOverflow.Ellipsis } else { TextOverflow.Clip },
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdaptiveImageCard(images: List<ImageBitmap>) {
    when (images.size) {
        0 -> {
            Spacer(modifier = Modifier.height(8.dp))
        }
        1, 2 -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(vertical = 16.dp)
            ) {
                for (image in images) {
                    Image(
                        bitmap = image,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )
                }
            }
        }
        else -> {
            LazyRow(
                modifier = Modifier
                    .height(120.dp)
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                itemsIndexed(images) { _, image ->
                    Image(
                        bitmap = images[1],
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(120.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardCard() {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "7일간 감정 상태 추이",
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = "7일간 감정 상태 추이",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Column(
                    modifier = Modifier.align(Alignment.Bottom)
                ) {
                    Text(
                        text = "주의가 필요합니다",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp
                    )
                    Text(
                        // TODO: 감정 개수가 동일한 경우 최근 감정을 우선으로 표시
                        text = "불안함",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(32.dp))
                EmotionBarChart(
                    angry = 2,
                    insecure = 4,
                    sad = 0,
                    hurt = 0,
                    happy = 1,
                    modifier = Modifier.height(128.dp)
                )
            }
        }
    }
}

@Composable
fun EmotionBarChart(angry: Int, insecure: Int, sad: Int, hurt: Int, happy: Int, modifier: Modifier = Modifier) {
    val defaultColor = MaterialTheme.colorScheme.outlineVariant
    val emotionColors = listOf(EmotionAngry, EmotionInsecure, EmotionSad, EmotionHurt, EmotionHappy)
    val emotionLabels = listOf("분노", "불안", "슬픔", "상처", "행복")
    val maxVal = maxOf(angry, insecure, sad, hurt, happy)

    Column {
        Row(modifier = modifier) {
            for ((i, emotion) in listOf(angry, insecure, sad, hurt, happy).withIndex()) {
                val backgroundColor = if (emotion > 0) { emotionColors[i] } else { defaultColor }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.Bottom)
                ) {
                    Text(
                        text = "$emotion",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = 8.dp)
                            .fillMaxHeight(emotion / maxVal.toFloat())
                            .background(backgroundColor, RoundedCornerShape(8.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
        Row {
            for (label in emotionLabels) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JournalPagePreview() {
    JournalPage(Modifier, true)
}