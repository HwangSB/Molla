package com.example.molla.forum

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.molla.R
import com.example.molla.api.dto.response.ForumListResponse
import com.example.molla.config.Screen
import com.example.molla.ui.theme.EmotionAngry
import com.example.molla.ui.theme.EmotionHappy
import com.example.molla.ui.theme.EmotionHurt
import com.example.molla.ui.theme.EmotionInsecure
import com.example.molla.ui.theme.EmotionSad
import com.google.gson.Gson


@Composable
fun ForumPage(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ForumListViewModel = ForumListViewModel()
) {
    val listState = rememberLazyListState()
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = listState
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

        items(pagingData.itemCount) { index ->
            val feedItem = pagingData[index]
            feedItem?.let { feed ->
                ForumCard(
                    feed = feed,
                    onClick = {
                        val feedJson = Gson().toJson(feed)
                        navController.navigate("${Screen.DetailedFeed.name}?detailedFeedJson=${feedJson}")
                    }
                )
            }
        }

        pagingData.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    Log.d("ForumPage", "Refresh Loading")
                }
                loadState.refresh is LoadState.Error -> {
                    Log.d("ForumPage", "Refresh Error")
                    item {
                        Text("Error: ${(loadState.append as LoadState.Error).error.message}")
                    }
                }
                loadState.append is LoadState.Loading -> {
                    Log.d("ForumPage", "Append Loading")
                }
                loadState.append is LoadState.Error -> {
                    Log.d("ForumPage", "Append Error")
                    item {
                        Text("Error: ${(loadState.append as LoadState.Error).error.message}")
                    }
                }
            }
        }

        // Bottom padding for the last item
        item { Spacer(modifier = Modifier.height(0.dp)) }
    }
}

@Composable
fun ForumCard(feed: ForumListResponse, onClick: () -> Unit = {}) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        onClick = onClick
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
                text = feed.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            CommunicationTag(
                commentCount = feed.commentCount,
                userEmotion = feed.userEmotion,
                userEmotionCount = feed.userEmotionCount,
                username = feed.username,
                createDate = feed.createDate
            )
        }
    }
}

@Composable
fun CommunicationTag(
    commentCount: Long,
    userEmotion: String?,
    userEmotionCount: Long,
    username: String,
    createDate: String,
) {
    val emotionColor = when (userEmotion) {
        "ANGRY" -> EmotionAngry
        "INSECURE" -> EmotionInsecure
        "SAD" -> EmotionSad
        "HURT" -> EmotionHurt
        "HAPPY" -> EmotionHappy
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }
    //val simpleDateFormat = SimpleDateFormat("MM월 dd일", Locale.KOREA)

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
            progress = { userEmotionCount / 7f },
            strokeCap = StrokeCap.Round,
            color = emotionColor,
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "by $username",
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = createDate,
            style = MaterialTheme.typography.bodyMedium,
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForumPagePreview() {
    val navController = rememberNavController()
    ForumPage(navController, Modifier)
}