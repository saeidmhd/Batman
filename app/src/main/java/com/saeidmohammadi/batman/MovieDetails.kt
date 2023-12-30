package com.saeidmohammadi.batman

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_details")
data class MovieDetails(
    @PrimaryKey
    val imdbID: String,
    val Title: String,
    val Year: String,
    val Genre: String,
    val Plot: String,
    val Director: String,
    val Poster: String
)