package com.example.molla

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
        composable(Screen.Main.name) {
            MainPage(navController)
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