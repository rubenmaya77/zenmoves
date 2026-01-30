package com.moviapp.jetpackcomposenewsapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Query("SELECT filmId FROM favorite_films")
    fun getFavoriteFilmIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_films WHERE filmId = :filmId)")
    suspend fun isFavorite(filmId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFavorite(entity: FavoriteFilmEntity)

    @Query("DELETE FROM favorite_films WHERE filmId = :filmId")
    suspend fun removeFavorite(filmId: String)
}
