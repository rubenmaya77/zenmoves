package com.moviapp.jetpackcomposenewsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import coil.compose.AsyncImage
import com.moviapp.jetpackcomposenewsapp.R
import com.moviapp.jetpackcomposenewsapp.data.entity.Film
import com.moviapp.jetpackcomposenewsapp.ui.components.EmptyStateComponent
import com.moviapp.jetpackcomposenewsapp.ui.components.Loader
import com.moviapp.jetpackcomposenewsapp.ui.viewmodel.NewsViewModel
import com.moviapp.utilities.ResourceState

const val TAG = "HOME_SCREEN"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToFavorites: () -> Unit,
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
                                FilmCardRowItem(
                                    film = film,
                                    isFavorite = isFavorite,
                                    onToggleFavorite = { newsViewModel.toggleFavorite(film.id) }
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

@Composable
private fun FilmCardRowItem(
    film: Film,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(width = 110.dp, height = 140.dp)
                    .clip(RoundedCornerShape(12.dp)),
                model = film.image ?: film.movie_banner,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.placeholder),
                error = painterResource(id = R.drawable.placeholder)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    androidx.compose.material3.Text(
                        text = film.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = onToggleFavorite, modifier = Modifier.size(40.dp)) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                val description = film.description.orEmpty().trim()
                val shortDescription = if (description.length > 130) {
                    description.take(127) + "..."
                } else {
                    description
                }

                androidx.compose.material3.Text(
                    text = shortDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(onNavigateToFavorites = {})
}