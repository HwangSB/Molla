package com.example.molla.forum

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.molla.api.dto.response.DiaryResponse
import com.example.molla.api.dto.response.PostDetail
import com.example.molla.common.ChatBubble
import com.example.molla.common.ChatInputSection
import com.example.molla.common.ChatMessageUiModel
import com.example.molla.common.DropdownMenu
import com.example.molla.ui.theme.EmotionAngry
import com.example.molla.ui.theme.EmotionHappy
import com.example.molla.ui.theme.EmotionHurt
import com.example.molla.ui.theme.EmotionInsecure
import com.example.molla.ui.theme.EmotionSad
import com.example.molla.ui.theme.MollaTheme
import java.text.SimpleDateFormat
import java.util.Locale
import com.example.molla.forum.dto.Feed
import com.example.molla.utils.convertBase64ToByteArray
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedFeedPage(navController: NavController, feed: String) {
    val viewModel: FeedDetailViewModel = viewModel()
    val simpleDateFormat = SimpleDateFormat("M월 d일", Locale.KOREA)
    var postDetail by remember { mutableStateOf<PostDetail?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()
    Log.d("정답을 알려줘", feed)

    val parsedFeed = remember(feed) { Json.decodeFromString<Feed>(feed) }

    val scrolledTitle = if (parsedFeed.title.length >= 6) parsedFeed.title.substring(0, 6) + "..." else parsedFeed.title
    var appBarTitle by remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }
    var userInput by remember { mutableStateOf("") }

    LaunchedEffect(parsedFeed.postId) {
        viewModel.fetchPostDetail(
            postId = parsedFeed.postId.toLong(),
            onSuccess = { detail ->
                postDetail = detail
            },
            onError = { error ->
                errorMessage = error
            }
        )
    }

//    var emotionCount = 3
//    val emotionType = 0
//    val emotionColor = when (emotionType) {
//        0 -> EmotionAngry
//        1 -> EmotionInsecure
//        2 -> EmotionSad
//        3 -> EmotionHurt
//        4 -> EmotionHappy
//        else -> MaterialTheme.colorScheme.onSurfaceVariant
//    }
//
//    val chatMessageList = remember {
//        mutableStateListOf(
//            ChatMessageUiModel(
//                "댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다",
//                false,
//                "8월 8일",
//                "사용자1"
//            ),
//            ChatMessageUiModel(
//                "댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다",
//                true,
//                "8월 8일",
//                "본인"
//            ),
//            ChatMessageUiModel("관종입니다.", false, "8월 8일", "사용자2"),
//            ChatMessageUiModel("이딴 글 왜 쓰는거임?", false, "8월 8일", "사용자3")
//        )
//    }

    MollaTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = appBarTitle) },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { expanded.value = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onEditClick = { /* TODO */ },
                            onDeleteClick = { /* TODO */ },
                            onReportClick = { /* TODO */ }
                        )
                    },
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                LaunchedEffect(listState.firstVisibleItemScrollOffset) {
                    appBarTitle = if (listState.firstVisibleItemScrollOffset > 0) scrolledTitle else ""
                }

                if (postDetail != null) {
                    val detail = postDetail!!
                    val date = simpleDateFormat.format(detail.createDate)
                    val emotionColor = when (detail.postEmotion) {
                        "Angry" -> EmotionAngry
                        "Insecure" -> EmotionInsecure
                        "Sad" -> EmotionSad
                        "Hurt" -> EmotionHurt
                        "Happy" -> EmotionHappy
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        state = listState
                    ) {
                        item {
                            Text(
                                text = detail.title,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.padding(vertical = 8.dp))
                        }
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp)
                            ) {
                                Text(
                                    text = detail.username,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .width(156.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(16.dp)
                                            .height(16.dp)
                                            .background(emotionColor, shape = RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    LinearProgressIndicator(
                                        progress = { detail.userEmotionCount / 7f },
                                        modifier = Modifier
                                            .weight(1f)
                                            .align(Alignment.CenterVertically),
                                        color = emotionColor,
                                        strokeCap = StrokeCap.Round,
                                    )
                                }
                            }
                        }
                        item {
                            Text(
                                text = date,
                                style = TextStyle(
                                    fontSize = 12.sp
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                        item {
                            Text(
                                text = detail.content,
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        items(detail.comments) { comment ->
                            val isUserComment = comment.username == detail.username
                            ChatBubble(
                                chatMessage = ChatMessageUiModel(
                                    message = comment.content,
                                    isUser = isUserComment,
                                    timestamp = simpleDateFormat.format(comment.createdDate),
                                    writer = comment.username
                                ),
                                isPostDetail = true
                            )
                        }
                    }
                } else if (errorMessage != null) {
                    Text(
                        text = "에러 발생: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                if (postDetail != null) {
                    ChatInputSection(
                        userInput = userInput,
                        onUserInputChange = { userInput = it },
                        onSendMessage = {
                            if (userInput.isNotEmpty()) {
                                // TODO : 서버로 채팅 입력 내용 전송
                                userInput = ""
                                // TODO : 서버로 받은 응답 값(AI 상담사의 답변) 또한 리스트에 추가
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailActivityPreview() {
    val navController = rememberNavController()
    val feed = Feed(
        postId = 0,
        title = "Hello, Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal 0!",
        content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        commentCount = 0,
        userEmotion = "HAPPY",
        emotionCount = 3,
        writer = "박준힉",
        timestamp = System.currentTimeMillis().toString(),
    )
    DetailedFeedPage(navController, Json.encodeToString(feed))
}
