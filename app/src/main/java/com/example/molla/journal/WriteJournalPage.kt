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
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteJournalPage(navController: NavController, updateJournalJson: String? = null, journalViewModel: JournalViewModel = viewModel()) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf(listOf<Pair<String, ByteArray>>()) }
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

    var isEdit = false
    var diaryId = 0L
    var previousImageIds = emptyList<Long>()
    updateJournalJson?.let { journalJson ->
        if (journalJson == "{}") return@let

        isEdit = true

        val diary = Json.decodeFromString<DiaryResponse>(journalJson)
        diaryId = diary.diaryId
        title = diary.title
        content = diary.content
        selectedImages = diary.images.map { Pair("prev_image.jpg", convertBase64ToByteArray(it.base64EncodedImage)) }
        previousImageIds = diary.images.map { it.imageId }
    }

    MollaTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("일기 작성") },
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
                                if (!isEdit) {
                                    journalViewModel.writeDiary(
                                        title = title,
                                        content = content,
                                        images = selectedImages,
                                        onSuccess = {
                                            navController.navigate(Screen.LoadAnalysis.name) {
                                                popUpTo(Screen.Main.name)
                                            }
                                        },
                                        onError = { Log.e("WriteJournalPage:Write", it) }
                                    )
                                } else {
                                    journalViewModel.updateDiary(
                                        diaryId = diaryId,
                                        title = title,
                                        content = content,
                                        updateImages = selectedImages,
                                        deleteImageIds = previousImageIds,
                                        onSuccess = {
                                            navController.navigate(Screen.LoadAnalysis.name) {
                                                popUpTo(Screen.Main.name)
                                            }
                                        },
                                        onError = { Log.e("WriteJournalPage:Update", it) }
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
                                .background(color = MaterialTheme.colorScheme.outlineVariant, shape = RoundedCornerShape(8.dp))
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
    WriteJournalPage(navController)
}