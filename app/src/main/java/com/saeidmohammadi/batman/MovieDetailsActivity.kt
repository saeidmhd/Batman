package com.saeidmohammadi.batman

import android.content.Context
import android.net.ConnectivityManager
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
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        movieDetailsView = findViewById(R.id.movieDetailsView)

        val imdbID = intent.getStringExtra("imdbID")
        if (imdbID != null) {
            movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
            loadMovieDetails(imdbID)
        }
    }


    private fun loadMovieDetails(imdbID: String) {
        if (isNetworkAvailable(this)) {
            // If network is available, load movie details from API
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val movieDetails = ApiClient.api.getMovieDetails(ApiClient.API_KEY, imdbID)
                    withContext(Dispatchers.Main) {
                        movieViewModel.insertMovieDetails(movieDetails)
                        updateUI(movieDetails)
                    }
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Error loading movie details", e)
                    Toast.makeText(this@MovieDetailsActivity, "Error loading movie details", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // If network is unavailable, observe movie details from the database
            movieViewModel.getMovieDetails(imdbID).observe(this) { movieDetails ->
                updateUI(movieDetails)
            }
        }
    }


    private fun updateUI(movieDetails: MovieDetails?) {
        if (movieDetails != null) {
            movieDetailsView.text = """
            Title: ${movieDetails.Title}
            Year: ${movieDetails.Year}
            Genre: ${movieDetails.Genre}
            Plot: ${movieDetails.Plot}
            Director: ${movieDetails.Director}
        """.trimIndent()
        }
    }

    private fun isNetworkAvailable(context: MovieDetailsActivity): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}
