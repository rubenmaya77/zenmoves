package com.moviapp.jetpackcomposenewsapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import com.moviapp.jetpackcomposenewsapp.ui.screens.FavoritesScreen
import com.moviapp.jetpackcomposenewsapp.ui.screens.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationGraph() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Favorites
    )

    fun navigateToBottomRoute(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        topBar = {
            val title = when (currentRoute) {
                Routes.FAVORITES_SCREEN -> "Favoritos"
                else -> "Películas"
            }
            TopAppBar(title = { Text(title) })
        },
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = { navigateToBottomRoute(item.route) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME_SCREEN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.HOME_SCREEN) {
                HomeScreen(onNavigateToFavorites = {
                    navigateToBottomRoute(Routes.FAVORITES_SCREEN)
                })
            }

            composable(Routes.FAVORITES_SCREEN) {
                FavoritesScreen()
            }
        }
    }

}

private sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object Home : BottomNavItem(
        route = Routes.HOME_SCREEN,
        label = "Home",
        icon = Icons.Filled.Home
    )

    data object Favorites : BottomNavItem(
        route = Routes.FAVORITES_SCREEN,
        label = "Favoritos",
        icon = Icons.Filled.Favorite
    )
}