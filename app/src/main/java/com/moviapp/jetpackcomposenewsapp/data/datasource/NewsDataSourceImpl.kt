package com.moviapp.jetpackcomposenewsapp.data.datasource

import com.moviapp.jetpackcomposenewsapp.data.api.ApiService
import com.moviapp.jetpackcomposenewsapp.data.entity.Film
import retrofit2.Response
import javax.inject.Inject

class NewsDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : NewsDataSource {

    override suspend fun getFilms(): Response<List<Film>> {
        return apiService.getFilms()
    }
}