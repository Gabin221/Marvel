package com.example.marvel.ui.avenir

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.Movie
import com.example.marvel.R
import com.example.marvel.databinding.FragmentAVenirBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.marvel.MovieAdapter
import com.example.marvel.mcuApiService

class AVenirFragment : Fragment() {

    private var _binding: FragmentAVenirBinding? = null

    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private val movies = mutableListOf<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAVenirBinding.inflate(inflater, container, false)
        val root: View = binding.root

        recyclerView = root.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = MovieAdapter(movies)

        fetchMovies()

        return root
    }

    private fun fetchMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            var date: String? = null
            do {
                val movie = mcuApiService.getNextMovie(date)
                date = movie.release_date
                withContext(Dispatchers.Main) {
                    movies.add(movie)
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            } while (movie.following_production != null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}