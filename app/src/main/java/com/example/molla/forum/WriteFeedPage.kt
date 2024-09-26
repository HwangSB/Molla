package com.example.molla.forum

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.molla.common.TitleAndContentInput
import com.example.molla.ui.theme.MollaTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.molla.MollaApp
import com.example.molla.config.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteFeedPage(navController: NavController, viewModel: WriteForumViewModel = viewModel()) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var showErrorMessage by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    MollaTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("게시글 작성") },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
                        }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                val error = viewModel.validateInput(title, content)
                                if (error == null) {
                                    viewModel.saveForum(
                                        title = title,
                                        content = content,
                                        userId = MollaApp.instance.userId.toString(),
                                        onSuccess = {
                                            navController.navigate(Screen.Forum.name) {
                                                popUpTo(Screen.Main.name)
                                            }
                                        },
                                        onError = { errorMessage ->
                                            validationError = errorMessage
                                            showErrorMessage = true
                                        }
                                    )
                                } else {
                                    validationError = error
                                    showErrorMessage = true
                                }
                            }
                        ) {
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

        if (showErrorMessage) {
            AlertDialog(
                onDismissRequest = { showErrorMessage = false },
                title = { Text(text = "입력 오류") },
                text = { Text(text = validationError ?: "") },
                confirmButton = {
                    TextButton(onClick = { showErrorMessage = false }) {
                        Text("확인")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WriteFeedPagePreview() {
    val navController = rememberNavController()
    WriteFeedPage(navController)
}