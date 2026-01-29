package com.moviapp.jetpackcomposenewsapp.data.entity

data class Film(
    val id: String,
    val title: String,
    val original_title: String? = null,
    val original_title_romanised: String? = null,
    val image: String? = null,
    val movie_banner: String? = null,
    val description: String? = null,
    val director: String? = null,
    val producer: String? = null,
    val release_date: String? = null,
    val running_time: String? = null,
    val rt_score: String? = null
)
