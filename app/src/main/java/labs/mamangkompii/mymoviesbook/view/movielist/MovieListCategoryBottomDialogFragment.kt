package labs.mamangkompii.mymoviesbook.view.movielist

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import labs.mamangkompii.mymoviesbook.databinding.MovieListCategoryPickerBinding
import labs.mamangkompii.mymoviesbook.usecase.model.MovieListCategory

class MovieListCategoryBottomDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "MovieListCategoryBottomDialogFragment"
    }

    private var interactionListener: InteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vBinding = MovieListCategoryPickerBinding.inflate(inflater)

        vBinding.popularMenu.setOnClickListener {
            onCategoryPicked(MovieListCategory.Popular)
        }
        vBinding.topRatedMenu.setOnClickListener {
            onCategoryPicked(MovieListCategory.TopRated)
        }
        vBinding.nowPlayingMenu.setOnClickListener {
            onCategoryPicked(MovieListCategory.NowPlaying)
        }
        vBinding.favoriteMenu.setOnClickListener {
            onCategoryPicked(MovieListCategory.Favorite)
        }

        return vBinding.root
    }

    private fun onCategoryPicked(movieListCategory: MovieListCategory) {
        interactionListener?.onPickMovieListCategory(movieListCategory)
        dismiss()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (activity is InteractionListener) {
            interactionListener = activity as InteractionListener
        }
    }

    override fun onDetach() {
        super.onDetach()

        interactionListener = null
    }

    interface InteractionListener {
        fun onPickMovieListCategory(movieListCategory: MovieListCategory)
    }
}

