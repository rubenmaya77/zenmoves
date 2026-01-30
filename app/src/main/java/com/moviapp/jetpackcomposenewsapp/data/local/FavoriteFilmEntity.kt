package com.moviapp.jetpackcomposenewsapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_films")
data class FavoriteFilmEntity(
    @PrimaryKey
    val filmId: String
)
