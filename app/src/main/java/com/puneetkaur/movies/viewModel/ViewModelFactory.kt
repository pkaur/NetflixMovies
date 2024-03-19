package com.puneetkaur.movies.viewModel

import MovieRepository
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory<T : ViewModel>(private val repository: MovieRepository, private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            // If the requested ViewModel is MovieViewModel, create an instance
            return MovieViewModel(repository, context) as T
        }
        // Handle other ViewModel types if needed
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
