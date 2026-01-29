package com.moviapp.jetpackcomposenewsapp.ui.repository

import com.moviapp.jetpackcomposenewsapp.data.datasource.NewsDataSource
import com.moviapp.jetpackcomposenewsapp.data.entity.Film
import com.moviapp.utilities.ResourceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsDataSource: NewsDataSource
) {

    suspend fun getFilms(): Flow<ResourceState<List<Film>>> {
        return flow {

            emit(ResourceState.Loading())

            val response = newsDataSource.getFilms()

            if (response.isSuccessful && response.body() != null) {
                emit(ResourceState.Success(response.body()!!))
            } else {
                emit(ResourceState.Error("Error Fetching Films"))
            }
        }.catch {
            emit(ResourceState.Error(it.localizedMessage ?: "Some Error in flow"))
        }
    }
}