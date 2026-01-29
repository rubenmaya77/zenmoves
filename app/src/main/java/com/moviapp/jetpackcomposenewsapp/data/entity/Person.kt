package com.moviapp.jetpackcomposenewsapp.data.entity

data class Person(
    val id: String,
    val name: String,
    val gender: String? = null,
    val age: String? = null,
    val eye_color: String? = null,
    val hair_color: String? = null
)
