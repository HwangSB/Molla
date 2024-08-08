package com.example.molla

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.molla.ui.theme.MollaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MainActivityContent() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityContent() {
    var selectedIndex by remember { mutableIntStateOf(0) }
    var isDashboardOpened by remember { mutableStateOf(true) }

    val selectedIconButtonColor = IconButtonDefaults.iconButtonColors(
        containerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
    )
    val appBarTitles = listOf("내 일기", "게시판")

    MollaTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(appBarTitles[selectedIndex]) },
                    actions = {
                        when (selectedIndex) {
                            0 -> IconButton(onClick = { isDashboardOpened = !isDashboardOpened }) {
                                Icon(painterResource(R.drawable.insert_chart_24px), null)
                            }
                            1 -> IconButton(onClick = { /*TODO: Show Search */ }) {
                                Icon(painterResource(R.drawable.groups_24px), null)
                            }
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
                        val icons = listOf(
                            R.drawable.today_24px,
                            R.drawable.groups_24px,
                            R.drawable.chat_bubble_24px
                        )
                        icons.forEachIndexed { index, iconResource ->
                            IconButton(
                                colors = if (selectedIndex == index) selectedIconButtonColor else IconButtonDefaults.iconButtonColors(),
                                onClick = { selectedIndex = index }
                            ) {
                                Icon(painterResource(iconResource), null)
                            }
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                            onClick = { /*TODO: Show write page */ }
                        ) {
                            Icon(Icons.Default.Add, null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            when (selectedIndex) {
                0 -> JournalPage(modifier = Modifier.padding(innerPadding), isDashboardOpened)
                1 -> ForumPage(modifier = Modifier.padding(innerPadding))
                2 -> TODO("Navigate to Counsel Activity")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    MainActivityContent()
}