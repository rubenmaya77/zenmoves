package com.moviapp.jetpackcomposenewsapp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import androidx.navigation.navArgument
import androidx.compose.material.icons.filled.ArrowBack
import com.moviapp.jetpackcomposenewsapp.ui.screens.FavoritesScreen
import com.moviapp.jetpackcomposenewsapp.ui.screens.HomeScreen
import com.moviapp.jetpackcomposenewsapp.ui.screens.MovieDetailScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigationGraph() {

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val isDetailRoute = currentRoute?.startsWith(Routes.MOVIE_DETAIL) == true

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
            if (isDetailRoute) {
                TopAppBar(
                    title = { Text("Detalles") },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                )
            } else {
                val title = when (currentRoute) {
                    Routes.FAVORITES_SCREEN -> "Favoritos"
                    else -> "Películas"
                }
                TopAppBar(title = { Text(title) })
            }
        },
        bottomBar = {
            if (!isDetailRoute) {
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
                }, onNavigateToDetail = { filmId ->
                    navController.navigate("${Routes.MOVIE_DETAIL}/$filmId")
                })
            }

            composable(Routes.FAVORITES_SCREEN) {
                FavoritesScreen(onNavigateToDetail = { filmId ->
                    navController.navigate("${Routes.MOVIE_DETAIL}/$filmId")
                })
            }

            composable(
                route = "${Routes.MOVIE_DETAIL}/{filmId}",
                arguments = listOf(navArgument("filmId") { type = NavType.StringType })
            ) { backStackEntry ->
                val filmId = backStackEntry.arguments?.getString("filmId")
                MovieDetailScreen(filmId = filmId)
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