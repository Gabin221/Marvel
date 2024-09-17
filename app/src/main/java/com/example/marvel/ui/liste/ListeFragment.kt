package com.example.marvel.ui.liste

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.MovieAdapterListe
import com.example.marvel.MoviePreferences
import com.example.marvel.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ListeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyMessage: TextView
    private lateinit var movieAdapter: MovieAdapterListe

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_liste, container, false)

        recyclerView = rootView.findViewById(R.id.rv_movies)
        emptyMessage = rootView.findViewById(R.id.tv_empty_message)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        movieAdapter = MovieAdapterListe(emptyList())
        recyclerView.adapter = movieAdapter

        observeMovies()

        return rootView
    }

    private fun observeMovies() {
        // Use lifecycleScope to observe the DataStore flow
        lifecycleScope.launch {
            MoviePreferences.getMovies(requireContext()).collect { movies ->
                if (movies.isEmpty()) {
                    emptyMessage.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    emptyMessage.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                    movieAdapter.updateMovies(movies)
                }
            }
        }
    }
}
