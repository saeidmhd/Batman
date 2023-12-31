package com.saeidmohammadi.batman

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData


class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: MovieRepository
    private var _allMovies: LiveData<List<Movie>> = MutableLiveData(emptyList())


    val allMovies: LiveData<List<Movie>>?
        get() = _allMovies


    init {
        val movieDao = AppDatabase.getInstance(application).movieDao()
        val movieDetailsDao = AppDatabase.getInstance(application).movieDetailsDao()
        repository = MovieRepository(movieDao, movieDetailsDao)
        _allMovies = repository.getAllMovies().asLiveData()
    }

    // Other functions in your ViewModel...

    // Note: You can remove this function if not needed
    fun insertMovies(movies: List<Movie>) = viewModelScope.launch {
        repository.insertMovies(movies)
    }

    // Note: You can remove this function if not needed
    suspend fun getMovieDetails(imdbID: String): MovieDetails? {
        return repository.getMovieDetails(imdbID)
    }

    // Note: You can remove this function if not needed
    fun insertMovieDetails(movieDetails: MovieDetails) = viewModelScope.launch {
        repository.insertMovieDetails(movieDetails)
    }
}

