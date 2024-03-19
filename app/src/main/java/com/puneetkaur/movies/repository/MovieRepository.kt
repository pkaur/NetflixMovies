import com.puneetkaur.movies.network.model.api.model.Movies
import retrofit2.Response

/**
 * Repository interface for handling movie-related data operations.
 */
interface MovieRepository {

    /**
     * Fetches trending movies based on the specified time window.
     *
     * @param timeWindow The time window for fetching trending movies.
     * @return A [Response] containing the list of trending movies.
     */
    suspend fun getTrendingMovies(timeWindow: String): Response<Movies>

    /**
     * Searches for movies based on the provided query.
     *
     * @param query The search query for movies.
     * @return A [Response] containing the list of movies matching the search query.
     */
    suspend fun searchMovie(query: String): Response<Movies>
}