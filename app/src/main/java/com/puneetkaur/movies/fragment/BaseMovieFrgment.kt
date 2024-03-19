package com.puneetkaur.movies.fragment

import MovieRepository
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.puneetkaur.movies.adapter.MovieAdapter
import com.puneetkaur.movies.databinding.FragmentMovieBinding
import com.puneetkaur.movies.network.model.api.MoviesApiService
import com.puneetkaur.movies.network.model.api.RetrofitHelper
import com.puneetkaur.movies.repository.MovieRepositoryImpl
import com.puneetkaur.movies.viewModel.MovieViewModel
import com.puneetkaur.movies.viewModel.ViewModelFactory

/**
 * Base class for fragments displaying movie-related content.
 * Provides common functionality such as ViewModel initialization and RecyclerView setup.
 */
abstract class BaseMovieFragment : Fragment() {

    private lateinit var binding: FragmentMovieBinding
    protected lateinit var viewModel: MovieViewModel
    protected lateinit var movieAdapter: MovieAdapter
    private lateinit var repository: MovieRepository
    private lateinit var service: MoviesApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        service = RetrofitHelper.api
        repository = MovieRepositoryImpl(service)
        viewModel = ViewModelProvider(this, ViewModelFactory<MovieViewModel>(repository,requireContext())).get(MovieViewModel::class.java)

        movieAdapter = MovieAdapter(emptyList())
        binding.recyclerViewMovies.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = movieAdapter
        }

        observeViewModel()
        fetchData()
    }

    protected fun crossfadeAnimation() {
        // this will set the duration for crossfade animation
        val duration = 500L

        binding.recyclerViewMovies.apply {
            alpha = 0f // Start with RecyclerView hidden
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(duration)
                .setListener(null)
        }

        binding.progressBar.apply {
            animate()
                .alpha(0f)
                .setDuration(duration)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        visibility = View.GONE
                    }
                })
        }
    }

    /**
     * Abstract function to be implemented in subclasses for observing changes in ViewModel data.
     */
    abstract fun observeViewModel()

    /**
     * Abstract function to be implemented in subclasses for fetching movie-related data.
     */
    abstract fun fetchData()
}
