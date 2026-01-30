package com.moviapp.jetpackcomposenewsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import com.moviapp.jetpackcomposenewsapp.ui.components.EmptyStateComponent
import com.moviapp.jetpackcomposenewsapp.ui.components.Loader
import com.moviapp.jetpackcomposenewsapp.ui.components.NewsRowComponent
import com.moviapp.jetpackcomposenewsapp.ui.viewmodel.NewsViewModel
import com.moviapp.utilities.ResourceState

const val TAG = "HOME_SCREEN"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToFavorites: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    newsViewModel: NewsViewModel = hiltViewModel()
) {

    val filmsResponse by newsViewModel.films.collectAsState()
    val favoriteIds by newsViewModel.favoriteFilmIds.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = onNavigateToFavorites) {
                        Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (filmsResponse) {
                is ResourceState.Loading -> {
                    Log.d(TAG, "Inside_Loading")
                    Loader()
                }

                is ResourceState.Success -> {
                    val films = (filmsResponse as ResourceState.Success).data
                    Log.d(TAG, "Inside_Success films=${films.size}")

                    if (films.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(items = films, key = { it.id }) { film ->
                                val isFavorite = favoriteIds.contains(film.id)
                                NewsRowComponent(
                                    film = film,
                                    isFavorite = isFavorite,
                                    onToggleFavorite = { newsViewModel.toggleFavorite(film.id) },
                                    onArrowClick = { onNavigateToDetail(film.id) }
                                )
                            }
                        }
                    } else {
                        EmptyStateComponent()
                    }
                }

                is ResourceState.Error -> {
                    val response = (filmsResponse as ResourceState.Error)
                    Log.d(TAG, "Inside_Error: $response")
                }
            }
        }
    }

}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigateToFavorites = {}, onNavigateToDetail = {})
}