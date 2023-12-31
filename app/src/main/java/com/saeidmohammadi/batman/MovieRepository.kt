package com.saeidmohammadi.batman

import kotlinx.coroutines.flow.Flow

class MovieRepository(private val movieDao: MovieDao, private val movieDetailsDao: MovieDetailsDao) {
     fun getAllMovies(): Flow<List<Movie>> {
        return movieDao.getAllMovies()
    }

    suspend fun insertMovies(movies: List<Movie>) {
        movieDao.insertMovies(movies)
    }

    suspend fun getMovieDetails(imdbID: String): MovieDetails? {
        return movieDetailsDao.getMovieDetails(imdbID)
    }

    suspend fun insertMovieDetails(movieDetails: MovieDetails) {
        movieDetailsDao.insertMovieDetails(movieDetails)
    }
}
