package com.devconnect.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.devconnect.ui.screens.CreatePostScreen
import com.devconnect.ui.screens.MainFeedScreen
import com.devconnect.ui.screens.PostDetailScreen

sealed class Screen(val route: String) {
    object MainFeed : Screen("main_feed")
    object PostDetail : Screen("post/{postId}") {
        fun createRoute(postId: String) = "post/$postId"
    }
    object CreatePost : Screen("create_post")
}

@Composable
fun DevConnectNavGraph(
    navController: NavHostController,
    authorAlias: String,
    startDestination: String = Screen.MainFeed.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.MainFeed.route) {
            MainFeedScreen(
                onPostClick = { postId ->
                    navController.navigate(Screen.PostDetail.createRoute(postId))
                },
                onCreatePost = {
                    navController.navigate(Screen.CreatePost.route)
                }
            )
        }

        composable(
            route = Screen.PostDetail.route,
            arguments = listOf(
                navArgument("postId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            PostDetailScreen(
                postId = backStackEntry.arguments?.getString("postId") ?: "",
                onNavigateUp = {
                    navController.navigateUp()
                }
            )
        }

        composable(Screen.CreatePost.route) {
            CreatePostScreen(
                onNavigateUp = {
                    navController.navigateUp()
                },
                onPostCreated = { postId ->
                    navController.navigate(Screen.PostDetail.createRoute(postId)) {
                        popUpTo(Screen.MainFeed.route)
                    }
                },
                authorAlias = authorAlias
            )
        }
    }
}
