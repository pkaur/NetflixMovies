package com.puneetkaur.movies.fragment

import android.os.Bundle
import android.view.View
import com.puneetkaur.movies.R
import android.view.LayoutInflater
import android.view.ViewGroup
import com.puneetkaur.movies.network.model.api.model.Result
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.puneetkaur.movies.utils.Constants

/**
 * Fragment to display details of a selected movie in an overlay fragment.
 */
class DetailOverlayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail_overlay, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedMovie = arguments?.getParcelable<Result>(ARG_SELECTED_MOVIE)

        updateBackdropImage(selectedMovie?.backdrop_path)
        updateTitle(selectedMovie?.title)
        updateDescription(selectedMovie?.overview)
    }

    /**
     * Updates the backdrop image in the UI using Glide.
     *
     * @param backdropPath The path to the backdrop image.
     */
    private fun updateBackdropImage(backdropPath: String?) {
        val imageViewBackdrop: ImageView = requireView().findViewById(R.id.imageViewBackdrop)

        Glide.with(requireContext())
            .load(Constants.paster_image_path + backdropPath)
            .fitCenter()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(imageViewBackdrop)
    }

    /**
     * Updates the title in the UI.
     *
     * @param title The movie title.
     */
    private fun updateTitle(title: String?) {
        val textViewTitle: TextView = requireView().findViewById(R.id.textViewTitle)
        textViewTitle.text = title
    }

    /**
     * Updates the description in the UI.
     *
     * @param description The movie description.
     */
    private fun updateDescription(description: String?) {
        val textViewDescription: TextView = requireView().findViewById(R.id.textViewDescription)
        textViewDescription.text = description
    }

    companion object {
        private const val ARG_SELECTED_MOVIE = "selectedMovie"

        /**
         * Creates a new instance of [DetailOverlayFragment] with the selected movie details.
         *
         * @param selectedMovie The selected movie details.
         * @return A new instance of [DetailOverlayFragment].
         */
        fun newInstance(selectedMovie: Result): DetailOverlayFragment {
            val args = Bundle().apply {
                putParcelable(ARG_SELECTED_MOVIE, selectedMovie)
            }
            val fragment = DetailOverlayFragment()
            fragment.arguments = args
            return fragment
        }
    }
}





