package com.moviapp.jetpackcomposenewsapp.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.moviapp.jetpackcomposenewsapp.ui.components.EmptyStateComponent
import com.moviapp.jetpackcomposenewsapp.ui.components.Loader
import com.moviapp.jetpackcomposenewsapp.ui.components.NewsRowComponent
import com.moviapp.jetpackcomposenewsapp.ui.viewmodel.NewsViewModel
import com.moviapp.utilities.ResourceState

const val TAG = "HOME_SCREEN"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(newsViewModel: NewsViewModel = hiltViewModel()) {

    val filmsResponse by newsViewModel.films.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        when (filmsResponse) {
            is ResourceState.Success -> {
                val films = (filmsResponse as ResourceState.Success).data
                films.size
            }

            else -> 0
        }
    }

    VerticalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize(),
        pageSize = PageSize.Fill,
        pageSpacing = 10.dp
    ) { page: Int ->
        when (filmsResponse) {
            is ResourceState.Loading -> {
                Log.d(TAG, "Inside_Loading")
                Loader()
            }

            is ResourceState.Success -> {
                val films = (filmsResponse as ResourceState.Success).data
                Log.d(TAG, "Inside_Success films=${films.size}")

                if (films.isNotEmpty()) {
                    NewsRowComponent(page, films[page])
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

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}