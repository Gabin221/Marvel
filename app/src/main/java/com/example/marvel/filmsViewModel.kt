package com.example.marvel

data class Movie(
    val days_until: Int,
    val id: Int,
    val overview: String,
    val poster_url: String,
    val release_date: String,
    val title: String,
    val type: String,
    val following_production: Movie?
)
