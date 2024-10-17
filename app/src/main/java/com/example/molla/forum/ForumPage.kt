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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.molla.config.Screen
import com.example.molla.forum.dto.Feed
import com.example.molla.ui.theme.EmotionAngry
import com.example.molla.ui.theme.EmotionHappy
import com.example.molla.ui.theme.EmotionHurt
import com.example.molla.ui.theme.EmotionInsecure
import com.example.molla.ui.theme.EmotionSad
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Locale



@Composable
fun ForumPage(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ForumListViewModel = ForumListViewModel()
) {
    // ViewModel에서 수집한 PagingData를 LazyPagingItems로 가져옴
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()

    // 새로고침 플래그가 true일 경우에만 새로고침 수행
    val refreshNeeded = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<Boolean>("refreshNeeded")
        ?.observeAsState()

    // refreshNeeded가 true일 경우에만 refresh 호출
    LaunchedEffect(refreshNeeded?.value) {
        if (refreshNeeded?.value == true) {
            pagingData.refresh()
            navController.currentBackStackEntry?.savedStateHandle?.set("refreshNeeded", false)
        }
    }

    //val pagingData = viewModel.pagingData.collectAsLazyPagingItems()

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
//        items(5) { index ->
//            val feed = Feed(
//                feedId = index,
//                title = "Hello, Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal $index!",
//                content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
//                commentCount = 0,
//                emotionType = 0,
//                emotionCount = 3,
//                writer = "작성자",
//                timestamp = System.currentTimeMillis(),
//            )
//            ForumCard(
//                feed = feed,
//                onClick = {
//                    val feedString = Json.encodeToString(feed)
//                    navController.navigate("${Screen.DetailedFeed.name}/$feedString")
//                }
//            )
//        }
        Log.d("가져온 개수", pagingData.itemCount.toString())
        items(pagingData.itemCount) { index ->
            val feedItem = pagingData[index]
            feedItem?.let { feed ->
                ForumCard(
                    feed = Feed(
                        feedId = feed.postId.toInt(),
                        title = feed.title,
                        content = feed.content,
                        commentCount = feed.commentCount.toInt(),
                        emotionType = viewModel.getEmotionType(feed.userEmotion),
                        emotionCount = feed.userEmotionCount.toInt(),
                        writer = feed.username,
                        timestamp = viewModel.parseDateToMonthDay(feed.createDate)
                    ),
                    onClick = {
                        // TODO
                    }
                )
            }
        }



        pagingData.apply {
            when {
                loadState.refresh is LoadState.Loading && itemCount == 0 -> {
                    // 초기 로딩 상태일 때 CircularProgressIndicator 표시
                    item { CircularProgressIndicator(modifier = Modifier.fillMaxSize()) }
                }
                loadState.append is LoadState.Loading -> {
                    // 추가 페이지 로딩 중
                    item { CircularProgressIndicator() }
                }
                loadState.append is LoadState.Error -> {
                    // 로딩 에러 발생 시 에러 메시지 표시
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
fun ForumCard(feed: Feed, onClick: () -> Unit = {}) {
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
    timestamp: String,
) {
    val emotionColor = when (emotionType) {
        0 -> EmotionAngry
        1 -> EmotionInsecure
        2 -> EmotionSad
        3 -> EmotionHurt
        4 -> EmotionHappy
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
            text = timestamp,
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