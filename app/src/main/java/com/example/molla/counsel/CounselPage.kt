package com.example.molla.counsel

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.molla.MollaApp
import com.example.molla.common.ChatBubble
import com.example.molla.common.ChatInputSection
import com.example.molla.common.ChatMessageUiModel
import com.example.molla.common.LabeledHorizontalDivider
import com.example.molla.ui.theme.MollaTheme
import com.example.molla.websocket.config.WebSocketClient
import com.example.molla.websocket.config.WebSocketPath
import com.example.molla.websocket.dto.request.ChatRequest
import com.google.gson.Gson
import okhttp3.WebSocket
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounselPage(navController: NavController) {
    val viewModel = CounselViewModel()

    val botName = "AI 상담사"
    val userName = "사용자"

    val currentDate = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("d일", Locale.getDefault())
    val dayOfMonth = dateFormat.format(currentDate) // 현재 날짜 가져옴

    var userInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val chatMessageList = remember { mutableStateListOf<ChatMessageUiModel>() }

    var webSocket by remember { mutableStateOf<WebSocket?>(null) }

    WebSocketClient(
        path = WebSocketPath.CHAT,
        onWebSocketCreated = { webSocket = it },
        onChatMessageReceived = { chatResponse ->
            chatMessageList.add(ChatMessageUiModel(chatResponse.content, false, writer = botName))
        }
    )

    LaunchedEffect(Unit) {
        viewModel.getChatHistory(
            onSuccess = { chatHistory ->
                chatMessageList.clear()
                chatMessageList.addAll(chatHistory.map {
                    ChatMessageUiModel(it.message, !it.isBot, writer = if(it.isBot) botName else userName )
                })
            },
            onError = { Log.d("CounselPage", it) }
        )
    }

    MollaTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("상담") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column (
                modifier = Modifier
                    .padding(
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                        top = innerPadding.calculateTopPadding(),
                    )
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(4.dp))
                LazyColumn (
                    modifier = Modifier.weight(1f),
                    state = listState
                ) {
                    item {
                        Text(
                            text = "2024년 7월",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            LabeledHorizontalDivider(label = dayOfMonth)
                        }
                    }
                    items(
                        items = chatMessageList,
                    ) {
                        chatMessage -> ChatBubble(chatMessage = chatMessage)
                    }
                }
                // 스크롤 상태에서 채팅 입력시 자동으로 하단 이동
                LaunchedEffect(chatMessageList.size) {
                    if (chatMessageList.size > 0) {
                        listState.animateScrollToItem(chatMessageList.size - 1)
                    }
                }

                Text(
                    text = "MOLLA의 AI 상담사는 의학적 사실을 검증하지 않습니다. 사용에 주의가 필요합니다.",
                    color = Color.Gray,
                    fontSize = 8.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                ChatInputSection(
                    innerPadding = innerPadding,
                    userInput = userInput,
                    onUserInputChange = { userInput = it },
                    onSendMessage = {
                        if (userInput.isNotEmpty()) {
                            val chatRequest = ChatRequest(
                                userId = MollaApp.instance.userId ?: -1,
                                message = userInput,
                                isBot = false,
                            )
                            webSocket?.send(Gson().toJson(chatRequest))
                            chatMessageList.add(ChatMessageUiModel(userInput, true, writer = "사용자"))
                            userInput = ""
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounselPagePreview() {
    val navController = rememberNavController()
    CounselPage(navController)
}