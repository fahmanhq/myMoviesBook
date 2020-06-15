package labs.mamangkompii.mymoviesbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import labs.mamangkompii.mymoviesbook.databinding.ActivityMainBinding
import labs.mamangkompii.mymoviesbook.presenter.movielist.MovieListPresenter
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import labs.mamangkompii.mymoviesbook.view.movielist.MovieListItemAdapter
import labs.mamangkompii.mymoviesbook.view.movielist.MovieListView
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MovieListView {

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
    }

    private fun setupMovieListRecyclerView() {
        movieListAdapter = MovieListItemAdapter()
        vBinding.movieListRV.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = movieListAdapter
        }

        movieListPresenter.setupMovieListDataSource()
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
}