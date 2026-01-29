package com.moviapp.jetpackcomposenewsapp.data.datasource

import com.moviapp.jetpackcomposenewsapp.data.entity.Film
import retrofit2.Response

interface NewsDataSource {
  suspend fun getFilms(): Response<List<Film>>
}