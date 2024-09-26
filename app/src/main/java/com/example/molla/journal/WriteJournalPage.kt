package com.example.molla.journal

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.molla.R
import com.example.molla.api.config.ApiClient
import com.example.molla.api.dto.request.DiaryCreateRequest
import com.example.molla.api.dto.response.DiaryCreateResponse
import com.example.molla.common.TitleAndContentInput
import com.example.molla.ui.theme.MollaTheme
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteJournalPage(navController: NavController) {
//    var selectedImages by remember { mutableStateOf(listOf<Uri>()) }
//    val imagePickerLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let { selectedImages = selectedImages + it }
//    }

    var selectedImages by remember { mutableStateOf(listOf<Int>()) }
    val placeholderImageRes = R.drawable.tree

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

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
                                val diaryCreateJsonString = Gson().toJson(
                                    DiaryCreateRequest(
                                        title = title,
                                        content = content,
                                        userId = "2352",
                                    )
                                )
                                val diaryCreateRequestBody = diaryCreateJsonString.toRequestBody("application/json".toMediaTypeOrNull())

                                val call = ApiClient.apiService.saveDiary(diaryCreateRequestBody)
                                call.enqueue(object : Callback<DiaryCreateResponse> {
                                    override fun onResponse(
                                        call: Call<DiaryCreateResponse>,
                                        response: Response<DiaryCreateResponse>
                                    ) {
                                        Log.d("DiaryCreateResponse", response.body().toString())
                                    }

                                    override fun onFailure(
                                        call: Call<DiaryCreateResponse>,
                                        t: Throwable
                                    ) {
                                        Log.e("DiaryCreateResponse", "Request: ${call.request().toString()}")

                                        // t.message만으로 부족하므로 스택 트레이스를 출력
                                        Log.e("DiaryCreateResponse", "Error message: ${t.message}")
                                        t.printStackTrace() // 스택 트레이스를 로그로 출력

                                        // 스택 트레이스를 Logcat에 명시적으로 출력
                                        Log.e("DiaryCreateResponse", Log.getStackTraceString(t)) // 스택 트레이스를 문자열로 변환해 출력

//                                        Log.d("DiaryCreateResponse", call.request().toString())
//                                        Log.e("DiaryCreateResponse", t.message.toString())
                                    }
                                })
//                                navController.navigate(Screen.LoadAnalysis.name) {
//                                    popUpTo(Screen.Main.name)
//                                }
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
                                //.clickable { imagePickerLauncher.launch("image/*") },
                                .clickable { selectedImages = selectedImages + placeholderImageRes },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
                        }
                    }
                    items(selectedImages.size) {
//                        index -> Image(
//                            //painter = rememberAsyncImagePainter(selectedImages[index]),
//                            painter = painterResource(id = selectedImages[index]),
//                            contentDescription = null,
//                            modifier = Modifier
//                                .size(100.dp)
//                                .background(Color.Gray, shape = RoundedCornerShape(8.dp))
//                                .aspectRatio(1f)
//                        )
                        index -> Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color.Transparent, shape = RoundedCornerShape(8.dp))
                            .clickable {
                                selectedImages = selectedImages.toMutableList().apply {
                                    removeAt(index)
                                }
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = selectedImages[index]),
                                contentDescription = null,
                                modifier = Modifier.size(100.dp)
                            )
                        }
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

@Preview(showBackground = true)
@Composable
fun WriteJournalPagePreview() {
    val navController = rememberNavController()
    WriteJournalPage(navController)
}