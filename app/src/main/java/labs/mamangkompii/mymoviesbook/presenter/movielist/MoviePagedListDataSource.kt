package labs.mamangkompii.mymoviesbook.presenter.movielist

import androidx.paging.PageKeyedDataSource
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import labs.mamangkompii.mymoviesbook.usecase.movielist.GetMovieListUseCase

class MoviePagedListDataSource(
    private val getMovieListUseCase: GetMovieListUseCase,
    private val compositeDisposable: CompositeDisposable,
    private val loadStateSubject: PublishSubject<LoadState>,
    private val retryScheduler: Scheduler
) : PageKeyedDataSource<Int, MovieSummary>() {

    private var retry: (() -> Any)? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, MovieSummary>
    ) {
        compositeDisposable.add(
            getMovieListUseCase.getPopularMovies(1)
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
                        loadStateSubject.onNext(LoadState.Error(LoadState.LoadingInitialData, it))
                        retry = {
                            loadInitial(params, callback)
                        }
                    }
                )
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieSummary>) {
        compositeDisposable.add(
            getMovieListUseCase.getPopularMovies(params.key)
                .doOnSubscribe { loadStateSubject.onNext(LoadState.LoadingInitialData) }
                .subscribe(
                    { collection ->
                        callback.onResult(collection, params.key + 1)
                        if (collection.isNotEmpty()) {
                            loadStateSubject.onNext(LoadState.InitialLoadFinished)
                        } else {
                            loadStateSubject.onNext(LoadState.NoLoadMoreRemaining)
                        }
                        retry = null
                    },
                    {
                        loadStateSubject.onNext(LoadState.Error(LoadState.LoadingInitialData, it))
                        retry = {
                            loadAfter(params, callback)
                        }
                    }
                )
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieSummary>) {
    }

    fun retryAllRequest() {
        val prefRetry = retry
        retry = null
        retryScheduler.scheduleDirect {
            prefRetry?.invoke()
        }
    }
}