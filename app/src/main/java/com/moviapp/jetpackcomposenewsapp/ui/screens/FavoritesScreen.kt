package com.moviapp.jetpackcomposenewsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moviapp.jetpackcomposenewsapp.ui.components.EmptyStateComponent
import com.moviapp.jetpackcomposenewsapp.ui.components.NewsRowComponent
import com.moviapp.jetpackcomposenewsapp.ui.viewmodel.NewsViewModel

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel = hiltViewModel()
) {
    val favorites by newsViewModel.favoriteFilms.collectAsState()

    if (favorites.isEmpty()) {
        EmptyStateComponent()
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(items = favorites, key = { it.id }) { film ->
                NewsRowComponent(
                    film = film,
                    isFavorite = true,
                    onToggleFavorite = { newsViewModel.toggleFavorite(film.id) }
                )
            }
        }
    }
}
