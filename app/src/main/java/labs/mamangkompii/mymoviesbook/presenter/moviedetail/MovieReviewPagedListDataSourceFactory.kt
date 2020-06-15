package labs.mamangkompii.mymoviesbook.presenter.moviedetail

import androidx.paging.DataSource
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import labs.mamangkompii.mymoviesbook.presenter.movielist.LoadState
import labs.mamangkompii.mymoviesbook.usecase.model.MovieReviewItem
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.GetMovieReviewsUseCase

class MovieReviewPagedListDataSourceFactory(
    private val movieId: String,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val compositeDisposable: CompositeDisposable,
    val loadStateSubject: PublishSubject<LoadState>,
    private val retryScheduler: Scheduler = Schedulers.computation()
) : DataSource.Factory<Int, MovieReviewItem>() {

    private var cachedDataSource: MovieReviewPagedListDataSource? = null

    override fun create(): DataSource<Int, MovieReviewItem> {
        cachedDataSource = MovieReviewPagedListDataSource(
            movieId,
            getMovieReviewsUseCase,
            compositeDisposable,
            loadStateSubject,
            retryScheduler
        )

        return cachedDataSource as MovieReviewPagedListDataSource
    }

    fun getExistingSource(): MovieReviewPagedListDataSource {
        return cachedDataSource ?: create() as MovieReviewPagedListDataSource
    }

    fun dispose() {
        compositeDisposable.dispose()
    }
}