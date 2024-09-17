package com.example.marvel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// 1. Définir l'adapter avec un type de ViewHolder adapté
class MovieAdapterListe(private var movieList: List<String>) :
    RecyclerView.Adapter<MovieAdapterListe.MovieViewHolder>() {

    // 2. Définir la classe interne MovieViewHolder qui hérite de RecyclerView.ViewHolder
    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val movieTitle: TextView = itemView.findViewById(android.R.id.text1)
    }

    // 3. Implémenter onCreateViewHolder correctement
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return MovieViewHolder(view)
    }

    // 4. Implémenter onBindViewHolder avec les bons paramètres
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.movieTitle.text = movieList[position]
    }

    // 5. Implémenter getItemCount pour retourner la taille de la liste
    override fun getItemCount(): Int {
        return movieList.size
    }

    // 6. Méthode pour mettre à jour la liste des films
    fun updateMovies(newMovies: List<String>) {
        movieList = newMovies
        notifyDataSetChanged()
    }
}
