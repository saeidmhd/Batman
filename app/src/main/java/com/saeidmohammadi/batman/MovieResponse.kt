package com.saeidmohammadi.batman

data class MovieResponse(
    val Search: List<Movie>,
    val totalResults: String,
    val Response: String
)
