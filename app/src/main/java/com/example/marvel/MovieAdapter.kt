package com.example.marvel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MovieAdapter(private val movies: MutableList<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            if (!date(movie.release_date).equals("")) {
                binding.title.text = movie.title
                binding.decompte.text = "${movie.days_until} jours restants"
                Picasso.get().load(movie.poster_url).into(binding.poster)
                binding.releaseDate.text = date(movie.release_date)
                binding.overview.text = movie.overview
            }
        }

        private fun date(releaseDate: String?): String {
            return if (releaseDate != null) {
                val liste = releaseDate.split("-")
                "${liste[2]}-${liste[1]}-${liste[0]}"
            } else {
                ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount() = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }
}
