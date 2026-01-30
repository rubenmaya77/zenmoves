package com.moviapp.jetpackcomposenewsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moviapp.jetpackcomposenewsapp.ui.screens.FavoritesScreen
import com.moviapp.jetpackcomposenewsapp.ui.screens.HomeScreen

@Composable
fun AppNavigationGraph() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.HOME_SCREEN) {
        composable(Routes.HOME_SCREEN) {
            HomeScreen(
                onNavigateToFavorites = { navController.navigate(Routes.FAVORITES_SCREEN) }
            )
        }

        composable(Routes.FAVORITES_SCREEN) {
            FavoritesScreen(onNavigateBack = { navController.popBackStack() })
        }
    }

}