package com.saeidmohammadi.batman

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDetailsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(movieDetails: MovieDetails)

    @Query("SELECT * FROM movie_details WHERE imdbID = :imdbID")
    fun getMovieDetails(imdbID: String): Flow<MovieDetails?>
}
