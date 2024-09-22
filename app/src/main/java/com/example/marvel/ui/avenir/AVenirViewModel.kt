package com.example.marvel.ui.avenir

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.marvel.Movie
import com.example.marvel.mcuApiService

class AVenirViewModel : ViewModel() {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> get() = _movies

    init {
        fetchMovies()
    }

    private fun fetchMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            val movieList = mutableListOf<Movie>()
            var date: String? = null
            var movie = mcuApiService.getNextMovie(date)
            while (movie.following_production != null) {
                date = movie.release_date
                movieList.add(movie)
                movie = mcuApiService.getNextMovie(date)
            }
            withContext(Dispatchers.Main) {
                _movies.value = movieList
            }
        }
    }
}
