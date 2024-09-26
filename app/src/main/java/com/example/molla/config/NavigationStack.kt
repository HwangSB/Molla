package com.example.molla.config

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.molla.MainPage
import com.example.molla.analysis.LoadAnalysisPage
import com.example.molla.counsel.CounselPageContent
import com.example.molla.forum.Feed
import com.example.molla.forum.PostDetailActivityContent
import com.example.molla.forum.WriteFeedPage
import com.example.molla.journal.WriteJournalPage
import com.example.molla.sign.SignInPage
import com.example.molla.sign.SignUpPage
import kotlinx.serialization.json.Json

enum class Screen {
    SignIn,
    SignUp,
    Main,
    WriteJournal,
    WriteFeed,
    DetailedFeed,
    LoadAnalysis,
    Counsel,
}

@Composable
fun NavigationStack() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.SignIn.name) {
        composable(Screen.SignIn.name) {
            SignInPage(navController)
        }
        composable(Screen.SignUp.name) {
            SignUpPage(navController)
        }
        composable(
            "${Screen.Main.name}?actionIndex={actionIndex}",
            arguments = listOf(navArgument("actionIndex") {
                defaultValue = 0
                type = NavType.IntType
            })
        ) { navBackStackEntry ->
            val actionIndex = navBackStackEntry.arguments?.getInt("actionIndex")
            actionIndex?.let {
                MainPage(navController, it)
            }
        }
        composable(
            Screen.WriteJournal.name,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down) }
        ) {
            WriteJournalPage(navController)
        }
        composable(
            Screen.WriteFeed.name,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down) }
        ) {
            WriteFeedPage(navController)
        }
        composable(
            "${Screen.DetailedFeed.name}/{feedJson}",
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) }
        ) { backStackEntry ->
            val feedJson = backStackEntry.arguments?.getString("feedJson")
            val feed = feedJson?.let { Json.decodeFromString<Feed>(it) }
            PostDetailActivityContent(navController, feed!!)
        }
        composable(
            Screen.LoadAnalysis.name,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            LoadAnalysisPage(navController)
        }
        composable(
            Screen.Counsel.name,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) }
        ) {
            CounselPageContent(navController)
        }
    }
}