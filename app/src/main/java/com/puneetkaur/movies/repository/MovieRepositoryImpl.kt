package com.puneetkaur.movies.repository

import MovieRepository
import com.puneetkaur.movies.network.model.api.MoviesApiService
import com.puneetkaur.movies.network.model.api.model.Movies
import com.puneetkaur.movies.utils.Constants
import retrofit2.Response

/**
 * Implementation of [MovieRepository] for handling movie-related data operations.
 *
 * @param moviesApiService The [MoviesApiService] used to interact with the Movies API.
 */
class MovieRepositoryImpl(private val moviesApiService: MoviesApiService): MovieRepository{

    override suspend fun getTrendingMovies(timeWindow: String): Response<Movies> {
        return moviesApiService.getAllTrendingMovies(timeWindow, Constants.api_key)
    }

    override suspend fun searchMovie(query: String): Response<Movies> {
        return moviesApiService.searchMovies(query, Constants.api_key)
    }
}