import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.puneetkaur.movies.R
import com.puneetkaur.movies.fragment.BaseMovieFragment
import com.puneetkaur.movies.fragment.DetailOverlayFragment
import com.puneetkaur.movies.fragment.SearchHandler
import com.puneetkaur.movies.network.model.api.model.Result
import kotlinx.coroutines.*

/**
 * Fragment responsible for displaying a list of movies, and showing movie details.
 *
 */
class MovieFragment : BaseMovieFragment() {

    private lateinit var searchView: TextInputEditText
    private val searchHandler by lazy { SearchHandler() }
    private var searchJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchView = requireView().findViewById(R.id.searchView)

        movieAdapter.setItemClickListener { selectedMovie ->
            showDetailOverlay(selectedMovie)
        }

        searchHandler.setupSearchView(searchView) { query ->
            Log.d(TAG, "setting up search view")
            updateSearchResults(query)
        }

        searchView.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.AXIS_X) {
                if (event.rawX >= (searchView.right - searchView.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    searchView.text?.clear() // Clear the search text
                    return@setOnTouchListener true
                }
            }
            false
        }

        observeViewModel()
        //fetchData()
    }

    /**
     * Updates the search results based on the provided query.
     *
     * Helper function cancels the current search job, initiates a new coroutine to handle the search,
     * introduces a delay(1000) to wait for user input, and fetches data from the ViewModel.
     * If the query is empty, it triggers fetching trending movies;
     * otherwise, it searches for movies based on the provided query.
     *
     * @param query The search query entered by the user.
     */
    private fun updateSearchResults(query: String) {
        searchJob?.cancel()
        searchJob = CoroutineScope(Dispatchers.Main).launch {
            delay(SEARCH_DELAY_MILLIS)
            if (query.isEmpty()) {
                Log.d(TAG, "fetching data from updateSearchResults")
                fetchData()
            } else {
                withContext(Dispatchers.IO) {
                    viewModel.searchMovies(query)
                }
            }
        }
    }

    /**
     * Observes the ViewModel to update UI based on movie data changes.
     *
     *  This function observes the [trendingMovies], [combinedResults], and [errorState] LiveData in the [viewModel]. When changes occur,
     * it checks if the response body is not null and updates the UI by submitting the list of movies
     * to the [movieAdapter].
     * Additionally, it triggers a crossfade animation to enhance the user experience.
     */
    override fun observeViewModel() {
        Log.d(TAG, "In observemodel")
        /*viewModel.trendingMovies.observe(viewLifecycleOwner, Observer { movies ->
            movies.body()?.results?.let {
                movieAdapter.submitList(it)
                crossfadeAnimation()
            }
        })*/

        viewModel.combinedResults.observe(viewLifecycleOwner, Observer { searchResults ->
            val results = searchResults.body()?.results

            if (results?.isEmpty() == true) {
                showToast("No results found")
            }
            searchResults.body()?.results?.let {
                movieAdapter.submitList(it)
                crossfadeAnimation()
            }
        })

        viewModel.errorState.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                showToast(error)
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState called")
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    /**
     * Initiates the fetching of trending movies.
     */
    override fun fetchData() {
        Log.d(TAG, "Fetching data")
        viewModel.fetchTrendingMovies("day")
    }

    /**
     * Initiates details overlay instantiation.
     */
    private fun showDetailOverlay(selectedMovie: Result) {
        val detailFragment = DetailOverlayFragment.newInstance(selectedMovie)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, detailFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchJob?.cancel()
    }

    companion object {
        const val TAG = "MovieFragment"
        const val DRAWABLE_RIGHT = 2
        private const val SEARCH_DELAY_MILLIS = 300L
    }
}
