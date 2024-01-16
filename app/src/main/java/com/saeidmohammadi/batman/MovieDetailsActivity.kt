package com.saeidmohammadi.batman

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovieDetailsActivity : ComponentActivity() {

    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imdbID = intent.getStringExtra("imdbID")
        if (imdbID != null) {
            movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)
            setContent {
                MovieDetailsScreen(imdbID)
            }
        }
    }

    @Composable
    fun MovieDetailsScreen(imdbID: String) {
        val movieDetails = remember { mutableStateOf<MovieDetails?>(null) }
        Column(modifier = Modifier.padding(16.dp)) {
            if (movieDetails.value == null) {
                Text(text = "Loading...")
                loadMovieDetails(imdbID, movieDetails)
            } else {
                MovieDetailsContent(movieDetails.value)
            }
        }
    }
    private fun loadMovieDetails(imdbID: String, movieDetails: MutableState<MovieDetails?>) {
        if (isNetworkAvailable(this)) {
            // If network is available, load movie details from API
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val details = ApiClient.api.getMovieDetails(ApiClient.API_KEY, imdbID)
                    withContext(Dispatchers.Main) {
                        movieViewModel.insertMovieDetails(details)
                        movieDetails.value = details
                    }
                } catch (e: Exception) {
                    Log.e("API_ERROR", "Error loading movie details", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MovieDetailsActivity, "Error loading movie details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            // If network is unavailable, observe movie details from the database
            movieViewModel.getMovieDetails(imdbID).observe(this@MovieDetailsActivity) { details ->
                movieDetails.value = details
            }
        }
    }

    private fun isNetworkAvailable(context: MovieDetailsActivity): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    @Composable
    fun MovieDetailsContent(movieDetails: MovieDetails?) {
        if (movieDetails != null) {
            Column {
                Text(text = "Title: ${movieDetails.Title}")
                Text(text = "Year: ${movieDetails.Year}")
                Text(text = "Genre: ${movieDetails.Genre}")
                Text(text = "Plot: ${movieDetails.Plot}")
                Text(text = "Director: ${movieDetails.Director}")
            }
        }
    }
}
