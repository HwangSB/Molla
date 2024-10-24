package com.example.molla

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.molla.config.MainAction
import com.example.molla.config.Screen
import com.example.molla.forum.ForumPage
import com.example.molla.journal.JournalPage
import com.example.molla.ui.theme.MollaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavController, selectedAction: MainAction, onActionChange: (MainAction) -> Unit) {
    var isDashboardOpened by remember { mutableStateOf(true) }

    val selectedIconButtonColor = IconButtonDefaults.iconButtonColors(
        containerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f)
    )
    val appBarTitles = listOf("내 일기", "게시판")

    MollaTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(appBarTitles[selectedAction.ordinal]) },
                    actions = {
                        when (selectedAction) {
                            MainAction.Journal -> IconButton(onClick = { isDashboardOpened = !isDashboardOpened }) {
                                Icon(painterResource(R.drawable.insert_chart_24px), null)
                            }
                            MainAction.Forum -> IconButton(onClick = { /*TODO: Show Search */ }) {
                                Icon(painterResource(R.drawable.groups_24px), null)
                            }
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
                        IconButton(
                            colors = if (selectedAction == MainAction.Journal) selectedIconButtonColor else IconButtonDefaults.iconButtonColors(),
                            onClick = { onActionChange(MainAction.Journal) }
                        ) {
                            Icon(painterResource(R.drawable.today_24px), null)
                        }
                        IconButton(
                            colors = if (selectedAction == MainAction.Forum) selectedIconButtonColor else IconButtonDefaults.iconButtonColors(),
                            onClick = { onActionChange(MainAction.Forum) }
                        ) {
                            Icon(painterResource(R.drawable.groups_24px), null)
                        }
                        IconButton(
                            onClick = { navController.navigate(Screen.Counsel.name) }
                        ) {
                            Icon(painterResource(R.drawable.chat_bubble_24px), null)
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                            onClick = {
                                when (selectedAction) {
                                    MainAction.Journal -> navController.navigate(Screen.WriteJournal.name)
                                    MainAction.Forum -> navController.navigate(Screen.WriteFeed.name)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Add, null)
                        }
                    }
                )
            }
        ) { innerPadding ->
            when (selectedAction) {
                MainAction.Journal -> JournalPage(navController, modifier = Modifier.padding(innerPadding), isDashboardOpened)
                MainAction.Forum -> ForumPage(navController, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    val navController = rememberNavController()
    MainPage(navController, MainAction.Journal) {}
}