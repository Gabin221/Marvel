package com.example.marvel.ui.update

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.marvel.MoviePreferences
import com.example.marvel.R
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UpdateFragment : Fragment() {

    private lateinit var movieTitleInput: EditText
    private lateinit var positionInput: EditText
    private lateinit var addButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_update, container, false)

        movieTitleInput = rootView.findViewById(R.id.et_movie_title)
        positionInput = rootView.findViewById(R.id.et_position)
        addButton = rootView.findViewById(R.id.btn_add_update_movie)

        addButton.setOnClickListener {
            val title = movieTitleInput.text.toString()
            val position = positionInput.text.toString().toIntOrNull()

            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter a movie title", Toast.LENGTH_SHORT).show()
            } else {
                addOrUpdateMovie(title, position)
            }
        }

        return rootView
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
