package labs.mamangkompii.mymoviesbook.presenter.moviedetail

import androidx.paging.PageKeyedDataSource
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import labs.mamangkompii.mymoviesbook.presenter.movielist.LoadState
import labs.mamangkompii.mymoviesbook.usecase.model.MovieReviewItem
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.GetMovieReviewsUseCase

class MovieReviewPagedListDataSource(
    private val movieId: String,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val compositeDisposable: CompositeDisposable,
    private val loadStateSubject: PublishSubject<LoadState>,
    private val retryScheduler: Scheduler
) : PageKeyedDataSource<Int, MovieReviewItem>() {

    private var retry: (() -> Any)? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieReviewItem>
    ) {
        compositeDisposable.add(
            getMovieReviewsUseCase.getMovieReviews(movieId, 1)
                .doOnSubscribe { loadStateSubject.onNext(LoadState.LoadingInitialData) }
                .subscribe(
                    { collection ->
                        callback.onResult(collection, null, 2)
                        if (collection.isNotEmpty()) {
                            loadStateSubject.onNext(LoadState.InitialLoadFinished)
                        } else {
                            loadStateSubject.onNext(LoadState.NoLoadMoreRemaining)
                        }
                        retry = null
                    },
                    {
                        loadStateSubject.onNext(LoadState.Error)
                        retry = {
                            loadInitial(params, callback)
                        }
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieReviewItem>) {
        compositeDisposable.add(
            getMovieReviewsUseCase.getMovieReviews(movieId, params.key)
                .doOnSubscribe { loadStateSubject.onNext(LoadState.LoadingMoreData) }
                .subscribe(
                    { collection ->
                        callback.onResult(collection, params.key + 1)
                        if (collection.isNotEmpty()) {
                            loadStateSubject.onNext(LoadState.LoadMoreFinished)
                        } else {
                            loadStateSubject.onNext(LoadState.NoLoadMoreRemaining)
                        }
                        retry = null
                    },
                    {
                        loadStateSubject.onNext(LoadState.Error)
                        retry = {
                            loadAfter(params, callback)
                        }
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieReviewItem>) {
    }

    fun retryAllRequest() {
        val prefRetry = retry
        retry = null
        retryScheduler.scheduleDirect {
            prefRetry?.invoke()
        }
    }
}