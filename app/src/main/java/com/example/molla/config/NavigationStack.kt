package com.example.molla.config

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.molla.MainPage
import com.example.molla.analysis.AnalysisPage
import com.example.molla.api.dto.response.DiaryResponse
import com.example.molla.counsel.CounselPage
import com.example.molla.forum.DetailedFeedPage
import com.example.molla.forum.WriteFeedPage
import com.example.molla.journal.WriteJournalPage
import com.example.molla.sign.SignInPage
import com.example.molla.sign.SignUpPage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Screen {
    SignIn,
    SignUp,
    Main,
    WriteJournal,
    WriteFeed,
    DetailedFeed,
//    LoadAnalysis,
    Analysis,
    Counsel,
}

enum class MainAction {
    Journal,
    Forum,
}

class NavigationViewModel : ViewModel() {
    private val _selectedAction = MutableStateFlow(MainAction.Journal)
    val selectedAction = _selectedAction.asStateFlow()

    fun selectAction(action: MainAction) {
        _selectedAction.value = action
    }
}

@Composable
fun NavigationStack(
    viewModel: NavigationViewModel = NavigationViewModel()
) {
    val navController = rememberNavController()
    val selectedAction by viewModel.selectedAction.collectAsState()

    NavHost(navController = navController, startDestination = Screen.SignIn.name) {
        composable(Screen.SignIn.name) {
            SignInPage(navController)
        }
        composable(Screen.SignUp.name) {
            SignUpPage(navController)
        }
        composable(Screen.Main.name) {
            MainPage(
                navController,
                selectedAction = selectedAction,
                onActionChange = { viewModel.selectAction(it) }
            )
        }
        composable(
            Screen.WriteJournal.name,
            enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Up) },
            exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Down) }
        ) {
            val diary = navController.previousBackStackEntry?.savedStateHandle?.get<DiaryResponse>("diary")
            WriteJournalPage(navController, diary)
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
//        composable(
//            Screen.LoadAnalysis.name,
//            enterTransition = { fadeIn() },
//            exitTransition = { fadeOut() }
//        ) {
//            LoadAnalysisPage(navController)
//        }
        composable(
            "${Screen.Analysis.name}?analysisResult={analysisResult}",
            arguments = listOf(
                navArgument("analysisResult") {
                    defaultValue = ""
                    type = NavType.StringType
                }
            ),
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) { navBackStackEntry ->
            val analysisResult = navBackStackEntry.arguments?.getString("analysisResult")
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