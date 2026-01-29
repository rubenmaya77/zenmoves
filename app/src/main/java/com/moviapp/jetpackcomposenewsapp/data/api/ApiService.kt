package com.moviapp.jetpackcomposenewsapp.data.api

import com.moviapp.jetpackcomposenewsapp.data.entity.Film
import com.moviapp.jetpackcomposenewsapp.data.entity.Person
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    // Ghibli API endpoints
    @GET("films")
    suspend fun getFilms(): Response<List<Film>>

    @GET("people")
    suspend fun getPeople(): Response<List<Person>>
}
