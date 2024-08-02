package com.example.molla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.molla.ui.theme.MollaTheme

class CounselActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { CounselActivityContent() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounselActivityContent() {
    MollaTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("상담") },
                    navigationIcon = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            Text(
                modifier = Modifier.padding(innerPadding),
                text = "Counsel Activity"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CounselActivityPreview() {
    CounselActivityContent()
}