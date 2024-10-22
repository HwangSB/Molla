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
import com.example.molla.analysis.AnalysisPage
import com.example.molla.analysis.LoadAnalysisPage
import com.example.molla.counsel.CounselPage
import com.example.molla.forum.DetailedFeedPage
import com.example.molla.forum.WriteFeedPage
import com.example.molla.journal.WriteJournalPage
import com.example.molla.sign.SignInPage
import com.example.molla.sign.SignUpPage

enum class Screen {
    SignIn,
    SignUp,
    Main,
    WriteJournal,
    WriteFeed,
    DetailedFeed,
    LoadAnalysis,
    Analysis,
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
            arguments = listOf(
                navArgument("actionIndex") {
                    defaultValue = 0
                    type = NavType.IntType
                }
            )
        ) { navBackStackEntry ->
            val actionIndex = navBackStackEntry.arguments?.getInt("actionIndex")
            actionIndex?.let {
                MainPage(navController, it)
            }
        }
        composable(
            "${Screen.WriteJournal.name}?updateJournalJson={updateJournalJson}",
            arguments = listOf(
                navArgument("updateJournalJson") {
                    defaultValue = "{}"
                    type = NavType.StringType
                }
            ),
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down) }
        ) { navBackStackEntry ->
            val updateJournalJson = navBackStackEntry.arguments?.getString("updateJournalJson")
            updateJournalJson?.let {
                WriteJournalPage(navController, it)
            }
        }
        composable(
            Screen.WriteFeed.name,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down) }
        ) {
            WriteFeedPage(navController)
        }
        composable(
            "${Screen.DetailedFeed.name}?detailedFeedJson={detailedFeedJson}",
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) }
        ) { navBackStackEntry ->
            val detailFeedJson = navBackStackEntry.arguments?.getString("detailedFeedJson")
            detailFeedJson?.let {
                DetailedFeedPage(navController, it)
            }

        }
        composable(
            Screen.LoadAnalysis.name,
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            LoadAnalysisPage(navController)
        }
        composable(
            "${Screen.Analysis.name}?analysisResult={analysisResult}",
            arguments = listOf(
                navArgument("analysisResult") {
                    defaultValue = 5
                    type = NavType.IntType
                }
            ),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { navBackStackEntry ->
            val analysisResult = navBackStackEntry.arguments?.getInt("analysisResult")
            println(analysisResult)
            analysisResult?.let {
                AnalysisPage(navController, it)
            }
        }
        composable(
            Screen.Counsel.name,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right) }
        ) {
            CounselPage(navController)
        }
    }
}