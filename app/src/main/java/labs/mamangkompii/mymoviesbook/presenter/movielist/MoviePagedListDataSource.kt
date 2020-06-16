package labs.mamangkompii.mymoviesbook.presenter.movielist

import androidx.paging.PageKeyedDataSource
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import labs.mamangkompii.mymoviesbook.usecase.model.MovieListCategory
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import labs.mamangkompii.mymoviesbook.usecase.movielist.GetMovieListUseCase

class MoviePagedListDataSource(
    private val movieListCategory: MovieListCategory,
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
            getMovieListStream(1, params.requestedLoadSize)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, MovieSummary>) {
        compositeDisposable.add(
            getMovieListStream(params.key, params.requestedLoadSize)
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, MovieSummary>) {
    }

    private fun getMovieListStream(pageIndex: Int, numOfRequestedData: Int): Single<List<MovieSummary>> {
        return when (movieListCategory) {
            MovieListCategory.Popular -> getMovieListUseCase.getPopularMovies(pageIndex)
            MovieListCategory.TopRated -> getMovieListUseCase.getTopRatedMovies(pageIndex)
            MovieListCategory.NowPlaying -> getMovieListUseCase.getNowPlayingMovies(pageIndex)
            MovieListCategory.Favorite -> getMovieListUseCase.getFavorites(pageIndex, numOfRequestedData)
        }
    }

    fun retryAllRequest() {
        val prefRetry = retry
        retry = null
        retryScheduler.scheduleDirect {
            prefRetry?.invoke()
        }
    }
}