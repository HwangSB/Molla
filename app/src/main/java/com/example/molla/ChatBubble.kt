package com.example.molla

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatBubble(
    modifier: Modifier = Modifier,
    chatMessage: ChatMessageUiModel,
    isPostDetail: Boolean = false,
) {

    val otherMessageColor = if (isPostDetail) Color(0xFFFFFAF0) else MaterialTheme.colorScheme.surface

    Column(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = if (chatMessage.isUser) Alignment.End else Alignment.Start,
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
        ) {
            if (chatMessage.isUser) {
                Spacer(modifier = Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = chatMessage.writer,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    MessageBox(
                        message = chatMessage.message,
                        isUser = true,
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(8.dp))
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = chatMessage.writer,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    MessageBox(
                        message = chatMessage.message,
                        isUser = false,
                        otherMessageColor = otherMessageColor,
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
        chatMessage.timestamp?.let { timestamp ->
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = timestamp,
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    message: String,
    isUser: Boolean,
    otherMessageColor: Color = MaterialTheme.colorScheme.surface,
) {
    val maxWidthDp = LocalConfiguration.current.screenWidthDp.dp * 2 / 3

    Box(
        modifier = modifier
            .widthIn(max = maxWidthDp)
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant), RoundedCornerShape(8.dp))
            .background(
                if (isUser) Color(0xFFE3F2FD) else otherMessageColor,
                RoundedCornerShape(8.dp)
            )
            .padding(8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(all = 4.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatBubble() {
    ChatBubble(
        chatMessage = ChatMessageUiModel(
            message = "안녕하세요",
            isUser = false,
            timestamp = "12:00 PM",
            writer = "누구게",
        ),
        isPostDetail = true
    )
}

data class ChatMessageUiModel(
    val message: String,
    val isUser: Boolean,
    val timestamp: String? = null,
    val writer: String
)