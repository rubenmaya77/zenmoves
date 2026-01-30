package com.moviapp.jetpackcomposenewsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.moviapp.jetpackcomposenewsapp.ui.viewmodel.NewsViewModel
import com.moviapp.utilities.ResourceState

@Composable
fun MovieDetailScreen(
    filmId: String?,
    newsViewModel: NewsViewModel = hiltViewModel()
) {
    val filmsState by newsViewModel.films.collectAsState()

    when (filmsState) {
        is ResourceState.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ResourceState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "No se pudo cargar la película")
            }
        }

        is ResourceState.Success -> {
            val films = (filmsState as ResourceState.Success).data
            val film = filmId?.let { id -> films.firstOrNull { it.id == id } }

            if (film == null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Película no encontrada")
                }
                return
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = film.movie_banner ?: film.image,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = film.title,
                        style = TextStyle(
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Director: ${film.director ?: "No disponible"}",
                        style = TextStyle(fontSize = 16.sp)
                    )

                    Text(
                        text = "Año: ${film.release_date ?: "No disponible"}",
                        style = TextStyle(fontSize = 16.sp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Divider(modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Descripción",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = film.description ?: "No hay descripción disponible",
                        style = TextStyle(
                            fontSize = 16.sp,
                            lineHeight = 22.sp
                        ),
                        textAlign = TextAlign.Start
                    )

                    film.producer?.let { producer ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Productor: $producer",
                            style = TextStyle(fontSize = 16.sp)
                        )
                    }

                    film.rt_score?.let { score ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Puntuación: $score%",
                            style = TextStyle(
                                fontSize = 16.sp,
                                color = (score.toIntOrNull() ?: 0).let { if (it >= 60) Color(0xFF2E7D32) else Color(0xFFC62828) }
                            )
                        )
                    }
                }
            }
        }
    }
}
