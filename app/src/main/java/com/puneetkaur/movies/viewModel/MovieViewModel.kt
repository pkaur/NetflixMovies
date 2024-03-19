package com.puneetkaur.movies.viewModel

import MovieRepository
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.puneetkaur.movies.network.model.api.model.Movies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

/**
 * ViewModel class responsible for managing movie-related data and interactions.
 *
 * @param movieRepository The repository providing access to movie data.
 */
class MovieViewModel(private val movieRepository: MovieRepository, private val context: Context) :
    ViewModel() {

    private val _trendingMovies = MutableLiveData<Response<Movies>>()
    val trendingMovies: LiveData<Response<Movies>> get() = _trendingMovies

    private val _searchResult = MutableLiveData<Response<Movies>>()

    private val _combinedResults = MediatorLiveData<Response<Movies>>()
    val combinedResults: LiveData<Response<Movies>> get() = _combinedResults

    private val _errorState = MutableLiveData<String>()
    val errorState: LiveData<String> get() = _errorState

    init {
        _combinedResults.addSource(_trendingMovies) { result ->
            _combinedResults.value = result
        }

        _combinedResults.addSource(_searchResult) { result ->
            _combinedResults.value = result
        }
    }

    /**
     * Fetch trending movies based on the specified time window.
     *
     * @param timeWindow The time window for fetching trending movies.
     */
    fun fetchTrendingMovies(timeWindow: String) {
        viewModelScope.launch {
            try {
                if (isNetworkConnected()) {
                    Log.d(TAG, "fetching trending movies")
                    val response =
                        withContext(Dispatchers.IO) { movieRepository.getTrendingMovies(timeWindow) }
                    if (response.isSuccessful) {
                        _trendingMovies.value = response
                    } else {
                        handleApiError(Exception("API error: ${response.code()}"))
                    }
                } else {
                    handleApiError(Exception("No internet connection"))
                }
            } catch (e: Exception) {
                handleApiError(e)
            }
        }
    }

    /**
     * Search movies based on the provided query.
     *
     * @param query The search query for movies.
     */
    fun searchMovies(query: String) {
        Log.d(TAG, "fetching search movies")
        viewModelScope.launch {
            try {
                if (isNetworkConnected()) {
                    val response =
                        withContext(Dispatchers.IO) { movieRepository.searchMovie(query) }
                    if (response.isSuccessful) {
                        _searchResult.value = response
                    } else {
                        handleApiError(Exception("API error: ${response.code()}"))
                    }
                } else {
                    handleApiError(Exception("No internet connection"))
                }
            } catch (e: Exception) {
                handleApiError(e)
            }
        }
    }

    /**
     * Handle API errors by logging and providing an error state.
     *
     * @param e The exception representing the API error.
     */
    private fun handleApiError(e: Exception) {

        val errorMessage = when (e) {
            is retrofit2.HttpException -> {
                "API error: ${e.code()} - ${e.message()}"
            }
            is java.lang.Exception -> {
                "Error Occurred: ${e.message}"
            }
            else -> {
                "Unexpected error: ${e.message}"
            }
        }
        _errorState.value = errorMessage
    }

    /**
     * Check if the device is connected to the internet.
     */
    /**
     * Check if the device is connected to the internet.
     */
    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val activeNetwork =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            } else {
                val networkInfo = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected == true
            }
        } catch (e: Exception) {
            false
        }
    }

    companion object{
        const val TAG = "MovieViewModel"
    }
}
