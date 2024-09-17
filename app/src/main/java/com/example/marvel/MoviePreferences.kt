package com.example.marvel

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("movie_preferences")

object MoviePreferences {
    private val MOVIE_LIST_KEY = stringPreferencesKey("movie_list_key")

    // Function to get the stored movie list as a flow
    fun getMovies(context: Context): Flow<List<String>> {
        return context.dataStore.data.map { preferences ->
            val movieList = preferences[MOVIE_LIST_KEY]
            movieList?.split(",")?.toList() ?: emptyList()
        }
    }

    // Function to save/update the movie list
    suspend fun saveMovies(context: Context, movies: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[MOVIE_LIST_KEY] = movies.joinToString(",")
        }
    }
}
