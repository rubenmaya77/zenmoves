package com.moviapp.jetpackcomposenewsapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moviapp.jetpackcomposenewsapp.data.entity.Film
import com.moviapp.jetpackcomposenewsapp.ui.repository.NewsRepository
import com.moviapp.utilities.ResourceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _films: MutableStateFlow<ResourceState<List<Film>>> =
        MutableStateFlow(ResourceState.Loading())
    val films: StateFlow<ResourceState<List<Film>>> = _films

    val favoriteFilmIds: StateFlow<Set<String>> = newsRepository.favoriteFilmIds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptySet())

    val favoriteFilms: StateFlow<List<Film>> = combine(films, favoriteFilmIds) { filmsState, favoriteIds ->
        when (filmsState) {
            is ResourceState.Success -> filmsState.data.filter { favoriteIds.contains(it.id) }
            else -> emptyList()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        getFilms()
    }

    private fun getFilms() {
        viewModelScope.launch {
            newsRepository.getFilms()
                .collectLatest { filmsResponse ->
                    _films.value = filmsResponse

                }
        }
    }

    fun toggleFavorite(filmId: String) {
        viewModelScope.launch {
            newsRepository.toggleFavorite(filmId)
        }
    }

    companion object {
        const val TAG = "NewsViewModel"
    }
}