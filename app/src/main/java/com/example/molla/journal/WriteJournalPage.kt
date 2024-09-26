package com.example.molla.journal

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
import com.example.molla.common.TitleAndContentInput
import com.example.molla.ui.theme.MollaTheme

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