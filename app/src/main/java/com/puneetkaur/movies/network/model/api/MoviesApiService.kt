package com.puneetkaur.movies.network.model.api

import com.puneetkaur.movies.network.model.api.model.Movies
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface for making API calls related to movies.
 */
interface MoviesApiService {

    /**
     * Gets a list of trending movies based on the specified time window.
     *
     * @param timeWindow The time window for trending movies (e.g., "day", "week").
     * @param apiKey The API key for authentication.
     * @return A [Response] containing the list of trending movies.
     */
    @GET("trending/movie/{time_window}")
    suspend fun getAllTrendingMovies(
        @Path("time_window") timeWindow: String,
        @Query("api_key") apiKey: String
    ): Response<Movies>

    /**
     * Searches for movies based on the specified query.
     *
     * @param query The search query.
     * @param apiKey The API key for authentication.
     * @return A [Response] containing the search results.
     */
    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("api_key") apiKey: String
    ): Response<Movies>
}