package com.example.marvel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MovieAdapter(movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    // Filtrer les films avant de les utiliser dans l'adaptateur
    private val upcomingMovies = movies

    class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.title.text = movie.title
            binding.decompte.text = "${movie.days_until} jours restants"
            Picasso.get().load(movie.poster_url).into(binding.poster)
            binding.overview.text = movie.overview
            binding.releaseDate.text = date(movie.release_date)
        }

        private fun date(releaseDate: String?): String {
            if (releaseDate != null) {
                val liste = releaseDate.split("-")
                return "${liste[2]}-${liste[1]}-${liste[0]}"
            } else {
                return ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(upcomingMovies[position])
    }

    override fun getItemCount() = upcomingMovies.size
}
