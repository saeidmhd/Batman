package com.saeidmohammadi.batman

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDetailsDao {
    @Query("SELECT * FROM movie_details WHERE imdbID = :imdbID")
    suspend fun getMovieDetails(imdbID: String): MovieDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(movieDetails: MovieDetails)
}