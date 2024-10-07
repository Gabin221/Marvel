package com.example.marvel

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.databinding.ItemMovieBinding
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.nl.translate.Translation
import com.squareup.picasso.Picasso

class MovieAdapter(private val movies: MutableList<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        private val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.FRENCH)
            .build()

        private val englishFrenchTranslator: Translator = Translation.getClient(options)

        fun bind(movie: Movie) {
            if (!date(movie.release_date).equals("")) {
                binding.title.text = movie.title
                binding.decompte.text = "${movie.days_until} jours restants"
                Picasso.get().load(movie.poster_url).into(binding.poster)

                // Télécharger le modèle de traduction si nécessaire
                englishFrenchTranslator.downloadModelIfNeeded()
                    .addOnSuccessListener {
                        // Traduire le texte une fois le modèle téléchargé
                        translate(movie.overview)
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Translation", "Model download failed: ${exception.message}")
                        // Afficher le texte original en cas d'erreur
                        binding.overview.text = movie.overview
                    }

                binding.releaseDate.text = date(movie.release_date)
            }
        }

        private fun translate(text: String) {
            englishFrenchTranslator.translate(text)
                .addOnSuccessListener { translatedText ->
                    binding.overview.text = translatedText
                }
                .addOnFailureListener { exception ->
                    Log.e("Translation", "Error translating text: ${exception.message}")
                    // Afficher le texte original en cas d'erreur
                    binding.overview.text = text
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

        fun closeTranslator() {
            // Fermer et libérer les ressources du traducteur quand il n'est plus nécessaire
            englishFrenchTranslator.close()
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

    override fun onViewRecycled(holder: MovieViewHolder) {
        super.onViewRecycled(holder)
        // Assurez-vous de libérer les ressources du traducteur quand le ViewHolder est recyclé
        holder.closeTranslator()
    }
}
