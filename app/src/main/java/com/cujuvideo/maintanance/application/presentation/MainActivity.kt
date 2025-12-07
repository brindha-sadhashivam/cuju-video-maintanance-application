package com.cujuvideo.maintanance.application.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cujuvideo.maintanance.application.presentation.Util.FETCH_VIDEO_VIEW_ROUTE_KEY
import com.cujuvideo.maintanance.application.presentation.Util.PERMISSION_REQUEST_SCREEN_ROUTE_KEY
import com.cujuvideo.maintanance.application.presentation.Util.RECORD_VIDEO_SCREEN_ROUTE_KEY
import com.cujuvideo.maintanance.application.presentation.Util.VIDEO_ID_ARG
import com.cujuvideo.maintanance.application.presentation.Util.VIDEO_PLAYER_SCREEN_ROUTE_KEY
import com.cujuvideo.maintanance.application.presentation.viewmodel.VideosViewModel
import com.cujuvideo.maintanance.application.presentation.views.PermissionRequestScreen
import com.cujuvideo.maintanance.application.presentation.views.PlayVideoView
import com.cujuvideo.maintanance.application.presentation.views.RecordVideoScreenView
import com.cujuvideo.maintanance.application.presentation.views.VideosListView
import org.koin.androidx.compose.koinViewModel

/**
 * Main Activity :  Responsible for Navigation and loading the landing screen
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MyAppNavigationFlow()
        }
    }
}

/**
 * MyAppNavigationFlow
 *
 * Manages the application's navigation graph using Jetpack Compose Navigation,
 * handling routing and required data passing between screens.
 */
@Composable
fun MyAppNavigationFlow() {
    val navigationController = rememberNavController()
    NavHost(navController = navigationController, startDestination = FETCH_VIDEO_VIEW_ROUTE_KEY) {
        composable(FETCH_VIDEO_VIEW_ROUTE_KEY) {
            VideosListView(navigationController)
        }
        composable(PERMISSION_REQUEST_SCREEN_ROUTE_KEY) {
            PermissionRequestScreen(navigationController)
        }
        composable(RECORD_VIDEO_SCREEN_ROUTE_KEY) {
            RecordVideoScreenView(navigationController)
        }
        composable(
            VIDEO_PLAYER_SCREEN_ROUTE_KEY, arguments = listOf(
                androidx.navigation.navArgument(VIDEO_ID_ARG) {
                    type = androidx.navigation.NavType.IntType
                    defaultValue = 0
                }
            )) { backStackEntry ->
            val videosViewModel: VideosViewModel = koinViewModel(
                viewModelStoreOwner = backStackEntry
            )
            PlayVideoView(videosViewModel, navigationController)
        }
    }
}