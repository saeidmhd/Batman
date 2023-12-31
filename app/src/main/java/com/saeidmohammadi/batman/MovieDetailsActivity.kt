package com.saeidmohammadi.batman

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailsActivity : AppCompatActivity() {

    private lateinit var movieDetailsView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        movieDetailsView = findViewById(R.id.movieDetailsView)

        val imdbID = intent.getStringExtra("imdbID")
        if (imdbID != null) {
            loadMovieDetails(imdbID)
        }
    }
    private fun loadMovieDetails(imdbID: String) {
        val movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val movieDetails = ApiClient.api.getMovieDetails(ApiClient.API_KEY, imdbID)
                withContext(Dispatchers.Main) {
                    movieViewModel.insertMovieDetails(movieDetails)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // نمایش پیام خطا به کاربر
                    Toast.makeText(this@MovieDetailsActivity, "Error loading movie details", Toast.LENGTH_SHORT).show()
                }
            }
        }
        movieViewModel.getMovieDetails(imdbID).observe(this) { movieDetails ->
            if (movieDetails != null) {
                // Update UI with movieDetails
                movieDetailsView.text = """
            Title: ${movieDetails.Title}
            Year: ${movieDetails.Year}
            Genre: ${movieDetails.Genre}
            Plot: ${movieDetails.Plot}
            Director: ${movieDetails.Director}
        """.trimIndent()
            }
        }

    }


}

