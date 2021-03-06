package labs.mamangkompii.mymoviesbook.presenter.movielist

import androidx.paging.DataSource
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import labs.mamangkompii.mymoviesbook.usecase.model.MovieListCategory
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import labs.mamangkompii.mymoviesbook.usecase.movielist.GetMovieListUseCase

class MoviePagedListDataSourceFactory(
    private val getMovieListUseCase: GetMovieListUseCase,
    private val compositeDisposable: CompositeDisposable,
    val loadStateSubject: PublishSubject<LoadState>,
    private val retryScheduler: Scheduler = Schedulers.computation()
) : DataSource.Factory<Int, MovieSummary>() {

    private var cachedDataSource: MoviePagedListDataSource? = null
    private var movieListCategory: MovieListCategory = MovieListCategory.Popular

    override fun create(): DataSource<Int, MovieSummary> {
        compositeDisposable.clear()

        cachedDataSource = MoviePagedListDataSource(
            movieListCategory,
            getMovieListUseCase,
            compositeDisposable,
            loadStateSubject,
            retryScheduler
        )

        return cachedDataSource as MoviePagedListDataSource
    }

    fun getExistingSource(): MoviePagedListDataSource {
        return cachedDataSource ?: create() as MoviePagedListDataSource
    }

    fun dispose() {
        compositeDisposable.dispose()
    }

    fun changeCategory(movieListCategory: MovieListCategory) {
        if (this.movieListCategory != movieListCategory) {
            this.movieListCategory = movieListCategory
            cachedDataSource?.invalidate()
        }
    }
}