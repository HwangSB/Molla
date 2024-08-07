package com.example.molla

import android.widget.ProgressBar
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ForumPage(modifier: Modifier) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(top = 16.dp)
                    .background(EmotionAngry, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "현재 커뮤니티 게시판은 분노 상태입니다",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        items(5) { index ->
            ForumCard(index = index, feed = Feed(
                title = "Hello, Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal $index!",
                content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                commentCount = 0,
                emotionType = 0,
                emotionCount = 3,
                writer = "작성자",
                timestamp = System.currentTimeMillis()
            ))
        }

        // Bottom padding for the last item
        item { Spacer(modifier = Modifier.height(0.dp)) }
    }
}

data class Feed(
    val title: String,
    val content: String,
    val commentCount: Int,
    val emotionType: Int,
    val emotionCount: Int,
    val writer: String,
    val timestamp: Long,
)

@Composable
fun ForumCard(index: Int, feed: Feed) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable { /*TODO*/ },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = feed.title,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "by ${feed.content}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            CommunicationTag(
                commentCount = feed.commentCount,
                emotionType = feed.emotionType,
                emotionCount = feed.emotionCount,
                writer = feed.writer,
                timestamp = feed.timestamp
            )
        }
    }
}

@Composable
fun CommunicationTag(
    commentCount: Int,
    emotionType: Int,
    emotionCount: Int,
    writer: String,
    timestamp: Long,
) {
    val emotionColor = when (emotionType) {
        0 -> EmotionAngry
        1 -> EmotionInsecure
        2 -> EmotionSad
        3 -> EmotionHurt
        4 -> EmotionHappy
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    val simpleDateFormat = SimpleDateFormat("MM월 dd일", Locale.KOREA)

    Row(modifier = Modifier.padding(16.dp)) {
        Image(
            painterResource(R.drawable.chat_24px),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
                .align(Alignment.CenterVertically)
                .padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$commentCount",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(modifier = Modifier
            .width(16.dp)
            .height(16.dp)
            .align(Alignment.CenterVertically)
            .background(emotionColor, RoundedCornerShape(8.dp))
        )
        Spacer(modifier = Modifier.width(8.dp))
        LinearProgressIndicator(
            progress = { emotionCount / 7f },
            strokeCap = StrokeCap.Round,
            color = emotionColor,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "by $writer",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = simpleDateFormat.format(timestamp),
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForumPagePreview() {
    ForumPage(Modifier)
}