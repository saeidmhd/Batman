package com.saeidmohammadi.batman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        movieAdapter = MovieAdapter { movie -> showMovieDetails(movie) }
        recyclerView.adapter = movieAdapter

        // در اینجا می‌توانید بر اساس اتصال به اینترنت یا استفاده از داده‌های ذخیره شده تصمیم بگیرید
        if (isNetworkAvailable()) {
            loadMovies()
        }
    }

    private fun loadMovies() {
        val movieViewModel = ViewModelProvider(this).get(MovieViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val isConnectedToNetwork = isNetworkAvailable()

                // Load data from network if available
                if (isConnectedToNetwork) {
                    val movieResponse = ApiClient.api.getMovies(ApiClient.API_KEY, "batman")
                    val movies = movieResponse.Search

                    // Update UI on the main thread
                    withContext(Dispatchers.Main) {
                        movieViewModel.insertMovies(movies)
                    }

                }

                // Load data from the ViewModel's LiveData
                val moviesFromViewModel = movieViewModel.allMovies.value

                // Update UI on the main thread
                withContext(Dispatchers.Main) {
                    if (moviesFromViewModel != null) {
                        movieAdapter.setData(moviesFromViewModel)
                    }
                }
            } catch (e: Exception) {
                // Handle the exception
                withContext(Dispatchers.Main) {
                    Log.e("API_ERROR", "Error loading movies", e)
                    // Display an error message to the user
                    Toast.makeText(this@MainActivity, "Error loading movies", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun isNetworkAvailable(): Boolean {
        // Implement your network availability check logic here
        // For example, you can use ConnectivityManager to check network status
        // Return true if network is available, false otherwise
        return true
    }


    private fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, MovieDetailsActivity::class.java)
        intent.putExtra("imdbID", movie.imdbID)
        startActivity(intent)
    }
}
