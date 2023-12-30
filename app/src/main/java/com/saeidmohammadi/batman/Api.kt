package com.saeidmohammadi.batman

import retrofit2.http.GET
import retrofit2.http.Query

interface Api {
    @GET("/")
    suspend fun getMovies(@Query("apikey") apiKey: String, @Query("s") searchQuery: String): MovieResponse

    @GET("/")
    suspend fun getMovieDetails(@Query("apikey") apiKey: String, @Query("i") imdbID: String): MovieDetails
}