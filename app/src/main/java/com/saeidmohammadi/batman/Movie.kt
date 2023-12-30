package com.saeidmohammadi.batman

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    val imdbID: String,
    val Title: String,
    val Year: String,
    val Type: String,
    val Poster: String
)