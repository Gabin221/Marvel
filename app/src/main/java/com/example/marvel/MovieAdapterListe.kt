package com.example.marvel

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapterListe(
    private var movieList: List<String>,
    private val selectedMovies: SparseBooleanArray
) : RecyclerView.Adapter<MovieAdapterListe.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTitle: TextView = itemView.findViewById(R.id.tv_movie_title)
        val movieCheckbox: CheckBox = itemView.findViewById(R.id.checkbox_movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_list_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.movieTitle.text = position.toString() + " - " + movieList[position]

        holder.movieCheckbox.isChecked = selectedMovies.get(position, false)

        holder.movieCheckbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedMovies.put(position, true)
            } else {
                selectedMovies.delete(position)
            }
        }

        holder.itemView.setOnClickListener {
            val currentChecked = holder.movieCheckbox.isChecked
            holder.movieCheckbox.isChecked = !currentChecked
        }
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    fun updateMovies(newMovies: List<String>) {
        movieList = newMovies
        notifyDataSetChanged()
    }

    fun clearSelections() {
        selectedMovies.clear()
        notifyDataSetChanged()
    }
}
