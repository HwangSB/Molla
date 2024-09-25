package com.example.molla.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.molla.R

@Composable
fun ChatInputSection(
    userInput: String,
    onUserInputChange: (String) -> Unit,
    onSendMessage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceDim, RoundedCornerShape(32.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = onUserInputChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .background(Color.Transparent),
                placeholder = { Text("메시지를 입력하세요") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent.copy(alpha = 0f),
                    unfocusedContainerColor = Color.Transparent.copy(alpha = 0f),
                    disabledContainerColor = Color.Transparent.copy(alpha = 0f),
                    errorContainerColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent.copy(alpha = 0f), // 포커스된 경계선 색상 투명
                    unfocusedBorderColor = Color.Transparent.copy(alpha = 0f), // 포커스되지 않은 경계선 색상 투명
                ),
                maxLines = 1
            )
            IconButton(
                onClick = onSendMessage,
                modifier = Modifier.padding(start = 4.dp)
            ) {
                Icon(painter = painterResource(id = R.drawable.send_24px), contentDescription = null)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowPreview() {
    ChatInputSection(userInput = ("asfasdf"), onUserInputChange = {}) {
        
    }
}