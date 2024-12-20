package com.example.molla.journal

import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.molla.MollaApp
import com.example.molla.api.dto.response.DiaryResponse
import com.example.molla.common.TitleAndContentInput
import com.example.molla.config.Screen
import com.example.molla.ui.theme.MollaTheme
import com.example.molla.utils.convertBase64ToByteArray
import com.example.molla.websocket.config.WebSocketClient
import com.example.molla.websocket.config.WebSocketPath
import com.example.molla.websocket.dto.request.EmotionAnalysisRequest
import com.google.gson.Gson
import okhttp3.WebSocket

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteJournalPage(navController: NavController, diary: DiaryResponse? = null) {
    val journalViewModel: JournalViewModel = viewModel()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf(listOf<Pair<String, ByteArray>>()) }

    var webSocket by remember { mutableStateOf<WebSocket?>(null) }
    var analysisResult by remember { mutableStateOf("") }
    var navigateToAnalysis by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream = MollaApp.instance.contentResolver.openInputStream(uri)
            val newImage = inputStream?.readBytes()
            newImage?.let {
                val fileName = getFileNameFromUri(uri)
                selectedImages = selectedImages + Pair(fileName, newImage)
            }
        }
    }

    if (diary != null) {
        title = diary.title
        content = diary.content
        selectedImages = diary.images.map { Pair("prev.jpg", convertBase64ToByteArray(it.base64EncodedImage)) }
    }

    WebSocketClient(
        WebSocketPath.EMOTION,
        onWebSocketCreated = { webSocket = it },
        onEmotionAnalysisMessageReceived = { response ->
            analysisResult = response.result
            navigateToAnalysis = true
        },
    )

    LaunchedEffect(navigateToAnalysis) {
        if (navigateToAnalysis) {
            navController.navigate("${Screen.Analysis.name}?analysisResult=${analysisResult}") {
                popUpTo(Screen.Main.name)
            }
        }
    }

    MollaTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("일기 작성") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    },
                    actions = {
                        TextButton(
                            onClick = {
                                if (diary == null) {
                                    journalViewModel.writeDiary(
                                        title = title,
                                        content = content,
                                        images = selectedImages,
                                        onSuccess = { diaryId ->
                                            val emotionAnalysisRequest = createEmotionAnalysisRequest(diaryId, content)
                                            webSocket?.send(Gson().toJson(emotionAnalysisRequest))
                                        },
                                        onError = { Log.e("WriteJournalPage.write", it) }
                                    )
                                } else {
                                    journalViewModel.updateDiary(
                                        diaryId = diary.diaryId,
                                        title = title,
                                        content = content,
                                        updateImages = selectedImages,
                                        deleteImageIds = diary.images.map { it.imageId },
                                        onSuccess = { diaryId ->
                                            val emotionAnalysisRequest = createEmotionAnalysisRequest(diaryId, content)
                                            webSocket?.send(Gson().toJson(emotionAnalysisRequest))
                                        },
                                        onError = { Log.e("WriteJournalPage.update", it) }
                                    )
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
                Text(
                    text = "오늘을 표현할 수 있는 사진을 첨부해주세요.",
                    style = TextStyle(fontSize = 14.sp)
                )
                LazyRow(
                    modifier = Modifier.padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { imagePickerLauncher.launch("image/*") },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
                        }
                    }
                    items(selectedImages.size) { index ->
                        val image = selectedImages[index].second
                        Image(
                            painter = rememberAsyncImagePainter(image),
                            contentDescription = null,
                            modifier = Modifier
                                .size(100.dp)
                                .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                                .aspectRatio(1f)
                        )
                    }
                }
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

private fun createEmotionAnalysisRequest(diaryId: Long, content: String): EmotionAnalysisRequest {
    return EmotionAnalysisRequest(
        userId = MollaApp.instance.userId ?: -1,
        targetId = diaryId,
        content = content,
        domain = "DIARY"
    )
}

private fun getFileNameFromUri(uri: Uri): String {
    var name = ""
    val cursor = MollaApp.instance.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) {
                name = it.getString(nameIndex)
            }
        }
    }
    return name
}

@Preview(showBackground = true)
@Composable
fun WriteJournalPagePreview() {
    val navController = rememberNavController()
    WriteJournalPage(navController, null)
}