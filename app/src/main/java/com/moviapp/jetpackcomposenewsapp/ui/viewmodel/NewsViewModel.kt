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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    private val _films: MutableStateFlow<ResourceState<List<Film>>> =
        MutableStateFlow(ResourceState.Loading())
    val films: StateFlow<ResourceState<List<Film>>> = _films

    init {
        getFilms()
    }

    private fun getFilms() {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.getFilms()
                .collectLatest { filmsResponse ->
                    _films.value = filmsResponse

                }
        }
    }

    companion object {
        const val TAG = "NewsViewModel"
    }
}