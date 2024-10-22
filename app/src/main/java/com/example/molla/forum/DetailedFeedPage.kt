package com.example.molla.forum

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.molla.MollaApp
import com.example.molla.api.dto.response.ForumListResponse
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
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.lifecycle.viewmodel.compose.viewModel as viewModel1

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedFeedPage(navController: NavController, feed: String) {
    val parsedFeed = Gson().fromJson(feed, ForumListResponse::class.java)
    val postId = parsedFeed.postId

    val viewModel: FeedDetailViewModel = viewModel1()
    var postDetail by remember { mutableStateOf<PostDetail?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val chatMessageList = remember { mutableStateListOf<ChatMessageUiModel>() }
    var appBarTitle by remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }

    LaunchedEffect(postId) {
        viewModel.fetchPostDetail(
            postId = postId,
            onSuccess = { data ->
                postDetail = data
                isLoading = false

                // 댓글
                chatMessageList.clear()
                chatMessageList.addAll(data.comments.map { comment ->
                    ChatMessageUiModel(
                        message = comment.content,
                        isUser = comment.username != MollaApp.instance.username,
                        timestamp = viewModel.parseDateToMonthDay(comment.createdDate),
                        writer = comment.username
                    )
                })
            },
            onError = { error ->
                errorMessage = error
                isLoading = false
            }
        )
    }

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
                    val title = postDetail?.title ?: ""
                    val scrolledTitle = if (title.length >= 6) title.substring(0, 6) + "…" else title
                    appBarTitle = if (listState.firstVisibleItemScrollOffset > 0) scrolledTitle else ""
                }

                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                errorMessage?.let { error ->
                    Log.e("error", error)
                }

                if (!isLoading && errorMessage == null) {
                    val title = postDetail?.title ?: ""
                    val username = postDetail?.username ?: ""
                    val content = postDetail?.content ?: ""
                    val userEmotionCount = postDetail?.userEmotionCount ?: 0L
                    val emotionColor = when (postDetail?.userEmotion) {
                        "ANGRY" -> EmotionAngry
                        "INSECURE" -> EmotionInsecure
                        "SAD" -> EmotionSad
                        "HURT" -> EmotionHurt
                        "HAPPY" -> EmotionHappy
                        else -> Color.Gray
                    }
                    val date = viewModel.parseDateToMonthDay(postDetail?.createDate.toString())
                    LazyColumn (
                        modifier = Modifier.weight(1f),
                        state = listState
                    ) {
                        item {
                            Text(
                                text = title,
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
                                    text = username,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Row (
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .width(156.dp)
                                ){
                                    Box(
                                        modifier = Modifier
                                            .width(16.dp)
                                            .height(16.dp)
                                            .background(emotionColor, shape = RoundedCornerShape(8.dp))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    LinearProgressIndicator(
                                        progress = { userEmotionCount / 7f },
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
                                text = content,
                                style = TextStyle(fontSize = 16.sp),
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        items(chatMessageList) { chatMessage ->
                            ChatBubble(
                                chatMessage = chatMessage,
                                isPostDetail = true
                            )
                        }
                    }

                    LaunchedEffect(chatMessageList.size) {
                        listState.animateScrollToItem(chatMessageList.size - 1)
                    }

                    ChatInputSection(
                        userInput = userInput,
                        onUserInputChange = { userInput = it },
                        onSendMessage = {
                            if (userInput.isNotEmpty()) {
//                                // TODO : 서버로 채팅 입력 내용 전송
//                                chatMessageList.add(ChatMessageUiModel(userInput, true, writer = "사용자", timestamp = "8월 8일"))
//                                userInput = ""
//
//                                // TODO : 서버로 받은 응답 값(AI 상담사의 답변) 또한 리스트에 추가
                                val currentUserId = MollaApp.instance.userId
                                Log.d("??asdf", (username == MollaApp.instance.username).toString())
                                Log.d("??asdfasdf", currentUserId.toString())
                                viewModel.saveComment(
                                    postId = postId,
                                    content = userInput,
                                    userId = currentUserId!!,
                                    createDate = LocalDateTime.now().toString(),
                                    onSuccess = { comment ->
                                        chatMessageList.add(
                                            ChatMessageUiModel(
                                                message = comment.content,
                                                isUser = username == MollaApp.instance.username,
                                                writer = comment.username,
                                                timestamp = viewModel.parseDateToMonthDay(LocalDateTime.now().toString())
                                            )
                                        )
                                        userInput = ""
                                    },
                                    onError = { error ->
                                        // 에러 처리
                                        Log.e("댓글 전송 에러", error)
                                    }
                                )
                            }
                        }
                    )
                }

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun DetailedFeedPagePreview() {
    val navController = rememberNavController()
    val feed = ForumListResponse(
        postId = 0,
        title = "Hello, Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal Jounal 0!",
        content = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
        commentCount = 0,
        userEmotion = null,
        userEmotionCount = 3,
        username = "박준힉",
        createDate = System.currentTimeMillis().toString(),
    )
    DetailedFeedPage(navController, Gson().toJson(feed))
}
