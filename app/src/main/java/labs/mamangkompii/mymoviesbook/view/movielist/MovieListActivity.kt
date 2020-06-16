package labs.mamangkompii.mymoviesbook.view.movielist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import labs.mamangkompii.mymoviesbook.MyApplication
import labs.mamangkompii.mymoviesbook.R
import labs.mamangkompii.mymoviesbook.databinding.ActivityMainBinding
import labs.mamangkompii.mymoviesbook.presenter.movielist.MovieListPresenter
import labs.mamangkompii.mymoviesbook.usecase.model.MovieListCategory
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import labs.mamangkompii.mymoviesbook.view.moviedetail.MovieDetailActivity
import javax.inject.Inject

class MovieListActivity : AppCompatActivity(), MovieListView, MovieListCategoryBottomDialogFragment.InteractionListener {

    private lateinit var vBinding: ActivityMainBinding
    private lateinit var movieListAdapter: MovieListItemAdapter

    private var retrySnackbar: Snackbar? = null

    @Inject
    lateinit var movieListPresenter: MovieListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.movieListComponent()
            .create(this)
            .inject(this)

        super.onCreate(savedInstanceState)

        vBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vBinding.root)

        setupMovieListRecyclerView()
        initClickListener()
    }

    private fun setupMovieListRecyclerView() {
        movieListAdapter = MovieListItemAdapter(
            object : ItemClickListener {
                override fun onClickMovie(item: MovieSummary?) {
                    item?.let { startMovieDetailActivity(it) }
                }
            }
        )
        vBinding.movieListRV.apply {
            layoutManager = LinearLayoutManager(this@MovieListActivity)
            adapter = movieListAdapter
        }

        movieListPresenter.setupMovieListDataSource()
    }

    private fun startMovieDetailActivity(movieSummary: MovieSummary) {
        MovieDetailActivity.start(this, movieSummary.id, movieSummary)
    }

    private fun initClickListener() {
        vBinding.movieListCategoryPickerBtn.setOnClickListener {
            MovieListCategoryBottomDialogFragment().show(supportFragmentManager, MovieListCategoryBottomDialogFragment.TAG)
        }
    }

    override fun updateMovieList(pagedList: PagedList<MovieSummary>) {
        movieListAdapter.submitList(pagedList)
    }

    override fun showErrorGetDataIndicator() {
        retrySnackbar = Snackbar.make(
            vBinding.root,
            R.string.movie_list_error_requesting_data,
            Snackbar.LENGTH_INDEFINITE
        )
        retrySnackbar!!.setAction(R.string.generic_retry) { movieListPresenter.retryFetchData() }
        retrySnackbar!!.show()
    }

    override fun onPickMovieListCategory(movieListCategory: MovieListCategory) {
        movieListPresenter.changeMovieListCategory(movieListCategory)
    }
}