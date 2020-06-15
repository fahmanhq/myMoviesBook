package labs.mamangkompii.mymoviesbook.view.moviedetail

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
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
import labs.mamangkompii.mymoviesbook.presenter.moviedetail.MovieReviewPresenter
import labs.mamangkompii.mymoviesbook.usecase.model.MovieReviewItem
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import org.joda.time.format.DateTimeFormat
import javax.inject.Inject

class MovieDetailActivity : AppCompatActivity(), MovieReviewsView {

    companion object {
        private const val EXTRA_KEY_MOVIE_ID = "movieId"
        private const val EXTRA_KEY_MOVIE_SUMMARY = "movieSummary"

        @JvmStatic
        fun start(
            context: Context,
            movieId: String,
            movieSummary: MovieSummary
        ) {
            val starter = Intent(context, MovieDetailActivity::class.java)
                .putExtra(EXTRA_KEY_MOVIE_ID, movieId)
                .putExtra(EXTRA_KEY_MOVIE_SUMMARY, movieSummary)
            context.startActivity(starter)
        }
    }

    private var posterHeight: Int = 0

    private lateinit var vBinding: ActivityMovieDetailBinding
    private val dateTimeFormatter = DateTimeFormat.forPattern("dd MMM YYYY")
    private lateinit var movieReviewItemAdapter: MovieReviewItemAdapter

    private lateinit var sheetBehavior: BottomSheetBehavior<View>

    @Inject
    lateinit var movieReviewPresenter: MovieReviewPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        val movieId = intent.extras!!.getString(EXTRA_KEY_MOVIE_ID)!!
        (application as MyApplication).appComponent.movieDetailsComponent()
            .create(this, movieId)
            .inject(this)
        super.onCreate(savedInstanceState)

        vBinding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(vBinding.root)

        initResources(resources)
        renderTemporarySummary()
        initUI()
        initActionListener()
    }

    private fun initResources(resources: Resources) {
        posterHeight = resources.getDimensionPixelSize(R.dimen.movie_poster_thumbnail_dimen_height)
    }

    private fun renderTemporarySummary() {
        val movieSummary = intent.extras?.getSerializable(EXTRA_KEY_MOVIE_SUMMARY) as MovieSummary?
        if (movieSummary != null) {
            renderMovieSummary(movieSummary)
        }
    }

    private fun renderMovieSummary(movieSummary: MovieSummary) {
        vBinding.movieTitleLabel.text = movieSummary.title
        vBinding.movieReleaseDateLabel.text =
            if (movieSummary.releaseDate != null) {
                dateTimeFormatter.print(movieSummary.releaseDate)
            } else {
                ""
            }
        vBinding.movieOverviewLabel.text = movieSummary.overview ?: ""
        renderPosterImage(movieSummary.posterPath)
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

    override fun onBackPressed() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            super.onBackPressed()
        }
    }
}