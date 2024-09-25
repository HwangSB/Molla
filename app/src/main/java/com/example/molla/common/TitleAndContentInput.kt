package com.example.molla.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TitleAndContentInput(
    title: String,
    onTitleChange: (String) -> Unit,
    content: String,
    onContentChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TextField(
            value = title,
            onValueChange = onTitleChange,
            placeholder = {
                Text(
                    "제목",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                )
            },
            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
        )
        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        TextField(
            value = content,
            onValueChange = onContentChange,
            placeholder = {
                Text(
                    "내용",
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                )
            },
            textStyle = TextStyle(fontSize = 20.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TitleAndContentInputPreview() {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    TitleAndContentInput(
        title = title,
        onTitleChange = { title = it },
        content = content,
        onContentChange = { content = it }
    )
}