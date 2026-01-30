package com.moviapp.jetpackcomposenewsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moviapp.jetpackcomposenewsapp.data.entity.Film
import com.moviapp.jetpackcomposenewsapp.ui.components.HeadingTextComponent
import com.moviapp.jetpackcomposenewsapp.ui.components.NormalTextComponent
import com.moviapp.jetpackcomposenewsapp.ui.viewmodel.NewsViewModel

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel = hiltViewModel()
) {
    val favorites by newsViewModel.favoriteFilms.collectAsState()

    if (favorites.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeadingTextComponent(value = "No tienes favoritos", isCenterAligned = true)
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            items(favorites) { film ->
                FavoriteFilmRow(film)
            }
        }
    }
}

@Composable
private fun FavoriteFilmRow(film: Film) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        HeadingTextComponent(value = film.title)
        Spacer(modifier = Modifier.padding(top = 6.dp))
        NormalTextComponent(value = film.description ?: "")
    }
}
