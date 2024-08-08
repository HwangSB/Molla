package com.example.molla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.molla.ui.theme.MollaTheme


class PostWriteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { PostWriteActivityContent() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostWriteActivityContent() {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    MollaTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("게시글 작성") },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(
                                text = "등록",
                                style = TextStyle(
                                    fontSize = 18.sp
                                )
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(12.dp)
                    .fillMaxSize()
            ) {
                TitleAndContentInput(
                    title = title,
                    onTitleChange = { title = it },
                    content = content,
                    onContentChange = { content = it }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PostWriteActivityPreview() {
    PostWriteActivityContent()
}