package com.saeidmohammadi.batman

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        movieAdapter = MovieAdapter { movie -> showMovieDetails(movie) }
        recyclerView.adapter = movieAdapter

        movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        if (isNetworkAvailable(this)) {
            loadMoviesFromApi()
        } else {
            loadMoviesFromDatabase()
        }
    }

    private fun loadMoviesFromApi() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val movieResponse = ApiClient.api.getMovies(ApiClient.API_KEY, "batman")
                val movies = movieResponse.Search
                withContext(Dispatchers.Main) {
                    movieViewModel.insertMovies(movies)
                    movieAdapter.setData(movies)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("API_ERROR", "Error loading movies from API", e)
                    Toast.makeText(this@MainActivity, "Error loading movies from API", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadMoviesFromDatabase() {
        // Observe allMovies LiveData
        movieViewModel.allMovies?.observe(this) { movies ->
            // Update the adapter data when allMovies changes
            movieAdapter.setData(movies)
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("imdbID", movie.imdbID)
        startActivity(intent)
    }
}
