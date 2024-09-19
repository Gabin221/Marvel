package com.example.marvel.ui.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.marvel.MoviePreferences
import com.example.marvel.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateFragment : Fragment() {

    private lateinit var movieTitleInput: EditText
    private lateinit var positionInput: EditText
    private lateinit var addButton: Button
    private lateinit var movieSpinner: Spinner
    private lateinit var deleteButton: Button

    private val listeFilms = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_update, container, false)

        movieTitleInput = rootView.findViewById(R.id.et_movie_title)
        positionInput = rootView.findViewById(R.id.et_position)
        addButton = rootView.findViewById(R.id.btn_add_update_movie)
        movieSpinner = rootView.findViewById(R.id.movieSpinner)
        deleteButton = rootView.findViewById(R.id.deleteButton)

        addButton.setOnClickListener {
            val title = movieTitleInput.text.toString()
            val positionStr = positionInput.text.toString()

            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a movie title", Toast.LENGTH_SHORT).show()
            } else if (positionStr.isNotEmpty()) {
                val position = positionStr.toIntOrNull()
                if (position != null) {
                    addOrUpdateMovie(title, position)
                } else {
                    Toast.makeText(requireContext(), "Invalid position", Toast.LENGTH_SHORT).show()
                }
            } else {
                addOrUpdateMovie(title, null)
            }
        }

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listeFilms)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        movieSpinner.adapter = spinnerAdapter

        deleteButton.setOnClickListener {
            val position = movieSpinner.selectedItemPosition
            if (position != Spinner.INVALID_POSITION && position > 0) {
                supprimerFilm(position)
            }
        }

        obtainMovies()

        return rootView
    }

    private fun supprimerFilm(position: Int) {
        lifecycleScope.launch {
            val movies = MoviePreferences.getMovies(requireContext()).first().toMutableList()
            if (position in movies.indices) {
                movies.removeAt(position)
                MoviePreferences.saveMovies(requireContext(), movies)
                withContext(Dispatchers.Main) {
                    listeFilms.removeAt(position)
                    (movieSpinner.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                    Toast.makeText(requireContext(), "Movie deleted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtainMovies() {
        lifecycleScope.launch {
            val movies = MoviePreferences.getMovies(requireContext()).first()
            withContext(Dispatchers.Main) {
                listeFilms.clear()
                listeFilms.add("--- Choisir un film ---")
                listeFilms.addAll(movies)
                (movieSpinner.adapter as ArrayAdapter<*>).notifyDataSetChanged()
            }
        }
    }

    private fun addOrUpdateMovie(title: String, position: Int?) {
        lifecycleScope.launch {
            val movies = MoviePreferences.getMovies(requireContext()).first().toMutableList()

            if (position == null || position < 0 || position >= movies.size) {
                // Add movie at the end
                movies.add(title)
            } else {
                // Update the movie at the given position
                //movies[position] = title
                movies.add(position, title)
            }

            MoviePreferences.saveMovies(requireContext(), movies)
            Toast.makeText(requireContext(), "Movie updated", Toast.LENGTH_SHORT).show()
        }
    }
}
