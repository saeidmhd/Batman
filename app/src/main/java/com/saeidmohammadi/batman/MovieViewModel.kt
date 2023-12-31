package com.saeidmohammadi.batman

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository
    lateinit var allMovies: LiveData<List<Movie>>
    init {
        val movieDao = AppDatabase.getInstance(application).movieDao()
        val movieDetailsDao = AppDatabase.getInstance(application).movieDetailsDao()
        repository = MovieRepository(movieDao, movieDetailsDao)

        // Use viewModelScope.launch to call getAllMovies within a coroutine
        viewModelScope.launch {
            allMovies = repository.getAllMovies().asLiveData()
        }
    }

    fun insertMovies(movies: List<Movie>) = viewModelScope.launch {
        repository.insertMovies(movies)
    }

    fun getMovieDetails(imdbID: String): LiveData<MovieDetails?> {
        return repository.getMovieDetails(imdbID).asLiveData()
    }

    fun insertMovieDetails(movieDetails: MovieDetails) = viewModelScope.launch {
        repository.insertMovieDetails(movieDetails)
    }
}

