package labs.mamangkompii.mymoviesbook.view.moviedetail

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.squareup.picasso.Picasso
import labs.mamangkompii.mymoviesbook.MyApplication
import labs.mamangkompii.mymoviesbook.R
import labs.mamangkompii.mymoviesbook.databinding.ActivityMovieDetailBinding
import labs.mamangkompii.mymoviesbook.presenter.moviedetail.MovieDetailPresenter
import labs.mamangkompii.mymoviesbook.presenter.moviedetail.MovieReviewPresenter
import labs.mamangkompii.mymoviesbook.usecase.model.MovieDetail
import labs.mamangkompii.mymoviesbook.usecase.model.MovieReviewItem
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import org.joda.time.format.DateTimeFormat
import javax.inject.Inject

class MovieDetailActivity : AppCompatActivity(), MovieDetailView, MovieReviewsView {

    companion object {
        private const val EXTRA_KEY_MOVIE_ID = "movieId"
        private const val EXTRA_KEY_MOVIE_SUMMARY = "movieSummary"

        @JvmStatic
        fun start(
            context: Context,
            movieId: Int,
            movieSummary: MovieSummary
        ) {
            val starter = Intent(context, MovieDetailActivity::class.java)
                .putExtra(EXTRA_KEY_MOVIE_ID, movieId)
                .putExtra(EXTRA_KEY_MOVIE_SUMMARY, movieSummary)
            context.startActivity(starter)
        }
    }

    private var movieId: Int = 0
    private var posterHeight: Int = 0

    private lateinit var vBinding: ActivityMovieDetailBinding
    private val dateTimeFormatter = DateTimeFormat.forPattern("dd MMM YYYY")
    private lateinit var movieReviewItemAdapter: MovieReviewItemAdapter

    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    @Inject
    lateinit var movieDetailPresenter: MovieDetailPresenter

    @Inject
    lateinit var movieReviewPresenter: MovieReviewPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        movieId = intent.extras!!.getInt(EXTRA_KEY_MOVIE_ID)
        (application as MyApplication).appComponent.movieDetailsComponent()
            .create(this, this, movieId)
            .inject(this)
        super.onCreate(savedInstanceState)

        vBinding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(vBinding.root)

        initResources(resources)
        initUI()
        initActionListener()

        movieDetailPresenter.requestDetail(movieId)
    }

    private fun initResources(resources: Resources) {
        posterHeight = resources.getDimensionPixelSize(R.dimen.movie_poster_thumbnail_dimen_height)
    }

    private fun initUI() {
        sheetBehavior = BottomSheetBehavior.from(vBinding.reviewBottomSheet.root)
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        movieReviewItemAdapter = MovieReviewItemAdapter()
        vBinding.reviewBottomSheet.run {
            movieReviewListRV.apply {
                layoutManager = LinearLayoutManager(this@MovieDetailActivity)
                adapter = movieReviewItemAdapter
            }
        }
    }

    private fun initActionListener() {
        vBinding.movieReviewBtn.setOnClickListener {
            movieReviewPresenter.requestReview()
            if (sheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        vBinding.reviewBottomSheet.retryLoadReviewsButton.setOnClickListener {
            movieReviewPresenter.retryRequestReview()
            vBinding.reviewBottomSheet.retryLoadReviewsButton.visibility = View.GONE
        }

        vBinding.favoriteButton.setOnClickListener {
            movieDetailPresenter.onFavoriteButtonClick()       
        }
    }

    override fun showEmptyPlaceholderIfNeeded() {
        if (movieReviewItemAdapter.itemCount == 0) {
            vBinding.reviewBottomSheet.noReviewPlaceholder.visibility = View.VISIBLE
        }
    }

    override fun hideGetReviewLoadingProgress() {
        vBinding.reviewBottomSheet.progressIndicator.visibility = View.GONE
    }

    override fun showGetReviewLoadingProgress() {
        vBinding.reviewBottomSheet.progressIndicator.visibility = View.VISIBLE
        vBinding.reviewBottomSheet.retryLoadReviewsButton.visibility = View.GONE
    }

    override fun showReviews(pagedList: PagedList<MovieReviewItem>) {
        movieReviewItemAdapter.submitList(pagedList)
    }

    override fun showErrorGettingReview() {
        vBinding.reviewBottomSheet.retryLoadReviewsButton.visibility = View.VISIBLE
        Toast.makeText(this, "Error Getting Review", Toast.LENGTH_SHORT).show()
    }

    override fun showLoadingMovieDetail() {
        // todo : show loading
        Log.d("HQ", "showLoadingMovieDetail")
    }

    override fun showMovieDetail(movieDetail: MovieDetail) {
        vBinding.movieTitleLabel.text = movieDetail.title
        vBinding.movieReleaseDateLabel.text =
            if (movieDetail.releaseDate != null) {
                dateTimeFormatter.print(movieDetail.releaseDate)
            } else {
                ""
            }
        vBinding.movieOverviewLabel.text = movieDetail.overview ?: ""
        renderPosterImage(movieDetail.posterPath)
    }

    private fun renderPosterImage(posterImageUrl: String?) {
        Picasso.get()
            .load(posterImageUrl)
            .resize(
                findViewById<View>(android.R.id.content).width,
                posterHeight
            )
            .centerCrop()
            .into(vBinding.moviePosterIV)
    }

    override fun showErrorGettingMovieDetail() {
        // todo : show error page & retry button
        Log.d("HQ", "showErrorGettingMovieDetail")
    }

    override fun disableFavoriteButton() {
        vBinding.favoriteButton.isEnabled = false
    }

    override fun enableFavoriteButton() {
        vBinding.favoriteButton.isEnabled = true
    }

    override fun toggleFavoriteState(isFavorited: Boolean) {
        toggleFavoriteButton(isFavorited)
    }

    private fun toggleFavoriteButton(isFavorited: Boolean) {
        vBinding.favoriteButton.setImageResource(
            if (isFavorited) R.drawable.ic_favorite else R.drawable.ic_unfavorite
        )
        vBinding.favoriteButton.tag = isFavorited
    }

    override fun showErrorAddMovieAsFavorite() {
        Toast.makeText(
            this,
            "Can't add this movie as favorite, please try again",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showErrorRemoveMovieFromFavorite() {
        Toast.makeText(
            this,
            "Can't remove this movie from favorite, please try again",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun showAllWidget() {
        vBinding.run {
            favoriteButton.visibility = View.VISIBLE
            movieReviewBtn.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        movieDetailPresenter.onDestroy()
        movieReviewPresenter.onDestroy()
    }
}