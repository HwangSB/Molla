package com.example.molla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.compose.ui.text.substring
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.molla.ui.theme.EmotionAngry
import com.example.molla.ui.theme.EmotionHappy
import com.example.molla.ui.theme.EmotionHurt
import com.example.molla.ui.theme.EmotionInsecure
import com.example.molla.ui.theme.EmotionSad
import com.example.molla.ui.theme.MollaTheme

class PostDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PostDetailActivityContent() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailActivityContent() {
    val title = "긴글입니다긴글입니다긴글입니다긴글입니다"
    val content = "내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다 내용입니다"
    val writer = "작성자"
    val date = "8월 8일"

    val scrolledTitle = if (title.length >= 6) title.substring(0, 6) + "..." else title
    var appBarTitle by remember { mutableStateOf("") }
    val expanded = remember { mutableStateOf(false) }

    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    var emotionCount = 3
    val emotionType = 0
    val emotionColor = when (emotionType) {
        0 -> EmotionAngry
        1 -> EmotionInsecure
        2 -> EmotionSad
        3 -> EmotionHurt
        4 -> EmotionHappy
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val chatMessageList = remember {
        mutableStateListOf(
            ChatMessageUiModel(
                "댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다",
                false,
                "8월 8일",
                "사용자1"
            ),
            ChatMessageUiModel(
                "댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다 댓글입니다",
                true,
                "8월 8일",
                "본인"
            ),
            ChatMessageUiModel("관종입니다.", false, "8월 8일", "사용자2"),
            ChatMessageUiModel("이딴 글 왜 쓰는거임?", false, "8월 8일", "사용자3")
        )
    }

    MollaTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = appBarTitle) },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { expanded.value = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = null)
                        }
                        DropdownMenuComponent(
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
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        ) {
                            Text(
                                text = "작성자",
                                style = TextStyle(
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Row (
                                modifier = Modifier.padding(16.dp)
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
                                    progress = { emotionCount / 7f },
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
                            // TODO : 서버로 채팅 입력 내용 전송
                            chatMessageList.add(ChatMessageUiModel(userInput, true, writer = "사용자", timestamp = "8월 8일"))
                            userInput = ""

                            // TODO : 서버로 받은 응답 값(AI 상담사의 답변) 또한 리스트에 추가
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostDetailActivityPreview() {
    PostDetailActivityContent()
}
