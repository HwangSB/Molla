package com.example.molla.journal

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.molla.common.DraggableBox
import com.example.molla.common.LabeledHorizontalDivider
import com.example.molla.R
import com.example.molla.api.dto.response.DiaryResponse
import com.example.molla.common.AdaptiveImageCard
import com.example.molla.config.Screen
import com.example.molla.ui.theme.ActionDelete
import com.example.molla.ui.theme.ActionEdit
import com.example.molla.ui.theme.EmotionAngry
import com.example.molla.ui.theme.EmotionHappy
import com.example.molla.ui.theme.EmotionHurt
import com.example.molla.ui.theme.EmotionInsecure
import com.example.molla.ui.theme.EmotionSad
import com.example.molla.utils.convertBase64ToImageBitmap
import com.example.molla.utils.parseDateToMonthDay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun JournalPage(navController: NavController, modifier: Modifier, isDashboardOpened: Boolean) {
    val journalViewModel = JournalViewModel()
    var journals by rememberSaveable { mutableStateOf(listOf<DiaryResponse>()) }

    LaunchedEffect(Unit) {
        journalViewModel.listDiaries(
            0,
            onSuccess = {
                journals += it
            },
            onError = { Log.d("JournalPage", it) }
        )
    }

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
            }
        }
        itemsIndexed(journals) { index, journal ->
            val parseDate = parseDateToMonthDay(journal.createDate).split(" ")
            val year = parseDate[0]
            val month = parseDate[1]
            val day = parseDate[2]

            val previousJournal = journals.getOrNull(index - 1)
            val parsedPreviousJournalDate = if (previousJournal != null) {
                parseDateToMonthDay(previousJournal.createDate).split(" ")[1]
            } else {
                ""
            }
            val isDifferentMonth = parsedPreviousJournalDate != month

            if (previousJournal == null || isDifferentMonth) {
                Text(
                    text = "${year}년 ${month}월",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            LabeledHorizontalDivider("${day}일")
            Spacer(modifier = Modifier.height(16.dp))
            JournalDraggableBox(
                onEdit = {
                    val updateJournalJson = Json.encodeToString(journal)
                    navController.navigate("${Screen.WriteJournal.name}?updateJournalJson=$updateJournalJson")
                },
                onDelete = {
                    journalViewModel.deleteDiary(
                        journal.diaryId,
                        onSuccess = {
                            journals -= journal
                        },
                        onError = { Log.e("JournalPage", it) }
                    )
                }
            ) {
                ExpandableCard(journal)
            }
        }
        // Bottom padding for the last item
        item { Spacer(modifier = Modifier.height(0.dp)) }
    }
}

@Composable
fun JournalDraggableBox(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    content: @Composable () -> Unit)
{
    DraggableBox(
        actionSize = 128.dp,
        startAction = {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(84.dp))
                    .background(ActionEdit)
                    .clickable(onClick = onEdit)
            ) {
                Icon(
                    painterResource(R.drawable.edit_24px),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center)
                )
            }
        },
        endAction = {
            Box(
                modifier = Modifier
                    .size(84.dp)
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(84.dp))
                    .background(ActionDelete)
                    .clickable(onClick = onDelete),
            ) {
                Icon(
                    painterResource(R.drawable.delete_24px),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(36.dp)
                        .align(Alignment.Center)
                )
            }
        },
    ) {
        content()
    }
}

@Composable
fun ExpandableCard(journal: DiaryResponse) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val emotionColor = if (journal.diaryEmotion != null) {
        when (journal.diaryEmotion) {
            "ANGRY" -> EmotionAngry
            "INSECURE" -> EmotionInsecure
            "SAD" -> EmotionSad
            "HURT" -> EmotionHurt
            "HAPPY" -> EmotionHappy
            else -> Color.Gray
        }
    } else {
        Color.Gray
    }

    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 100.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = { expanded = !expanded }
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Text(
                    text = journal.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
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
                    .background(emotionColor, RoundedCornerShape(8.dp))
                    .align(Alignment.CenterVertically)
                )
            }
            AdaptiveImageCard(
                images = journal.images.map { convertBase64ToImageBitmap(it.base64EncodedImage) }
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
fun DashboardCard(title: String, subTitle: String, content: @Composable (() -> Unit)? = null) {
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
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                )
                Text(
                    text = subTitle,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.align(Alignment.Bottom)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            content?.invoke()
        }
    }
}

@Composable
fun EmotionBarChart(status: String, caution: String, angry: Int, insecure: Int, sad: Int, hurt: Int, happy: Int, modifier: Modifier = Modifier) {
    val defaultColor = MaterialTheme.colorScheme.outlineVariant
    val emotionColors = listOf(EmotionAngry, EmotionInsecure, EmotionSad, EmotionHurt, EmotionHappy)
    val emotionTexts = listOf("분노", "불안", "슬픔", "상처", "행복")
    val maxVal = maxOf(angry, insecure, sad, hurt, happy)

    Row {
        Column(
            modifier = Modifier
                .width(80.dp)
                .align(Alignment.Bottom)
        ) {
            Text(
                text = caution,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 10.sp
            )
            Text(
                // TODO: 감정 개수가 동일한 경우 최근 감정을 우선으로 표시
                text = status,
                style = MaterialTheme.typography.titleSmall,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(32.dp))
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
                for (text in emotionTexts) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.labelSmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JournalPagePreview() {
    val navController = rememberNavController()
    JournalPage(navController, Modifier, isDashboardOpened = true)
}