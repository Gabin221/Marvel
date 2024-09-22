package com.example.marvel.ui.liste

import android.content.Context
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.MovieAdapterListe
import com.example.marvel.MoviePreferences
import com.example.marvel.R
import kotlinx.coroutines.launch

class ListeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyMessage: TextView
    private lateinit var movieAdapter: MovieAdapterListe
    private lateinit var clearButton: Button
    private lateinit var selectedMovies: SparseBooleanArray

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_liste, container, false)

        recyclerView = rootView.findViewById(R.id.rv_movies)
        emptyMessage = rootView.findViewById(R.id.tv_empty_message)
        clearButton = rootView.findViewById(R.id.btn_clear)

        selectedMovies = loadSelections()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        movieAdapter = MovieAdapterListe(emptyList(), selectedMovies)
        recyclerView.adapter = movieAdapter

        observeMovies()

        clearButton.setOnClickListener {
            movieAdapter.clearSelections()
            saveSelections()
        }

        return rootView
    }

    private fun observeMovies() {
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

    private fun loadSelections(): SparseBooleanArray {
        val sharedPref = requireContext().getSharedPreferences("movieSelections", Context.MODE_PRIVATE)
        val selectedMovies = SparseBooleanArray()
        val size = sharedPref.getInt("size", 0)

        for (i in 0 until size) {
            selectedMovies.put(sharedPref.getInt("selected_$i", -1), true)
        }

        return selectedMovies
    }

    private fun saveSelections() {
        val sharedPref = requireContext().getSharedPreferences("movieSelections", Context.MODE_PRIVATE).edit()
        sharedPref.putInt("size", selectedMovies.size())

        for (i in 0 until selectedMovies.size()) {
            val key = selectedMovies.keyAt(i)
            sharedPref.putInt("selected_$i", key)
        }

        sharedPref.apply()
    }

    override fun onPause() {
        super.onPause()
        saveSelections()
    }
}
