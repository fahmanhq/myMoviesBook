package labs.mamangkompii.mymoviesbook.presenter.movielist

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import labs.mamangkompii.mymoviesbook.di.annotation.ActivityScope
import labs.mamangkompii.mymoviesbook.di.annotation.MainScheduler
import labs.mamangkompii.mymoviesbook.usecase.model.MovieListCategory
import labs.mamangkompii.mymoviesbook.view.movielist.MovieListView
import javax.inject.Inject

@ActivityScope
class MovieListPresenterImpl @Inject constructor(
    private val view: MovieListView,
    private val moviePagedListDataSourceFactory: MoviePagedListDataSourceFactory,
    private val compositeDisposable: CompositeDisposable,
    @MainScheduler private val mainScheduler: Scheduler
) : MovieListPresenter {

    override fun setupMovieListDataSource() {
        val config: PagedList.Config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(10)
            .setPageSize(20)
            .build()

        compositeDisposable.add(
            RxPagedListBuilder(moviePagedListDataSourceFactory, config)
                .buildObservable()
                .subscribe {
                    view.updateMovieList(it)
                }
        )

        compositeDisposable.add(
            moviePagedListDataSourceFactory.loadStateSubject
                .observeOn(mainScheduler)
                .subscribe {
                    if (it is LoadState.Error) {
                        view.showErrorGetDataIndicator()
                    }
                }
        )
    }

    override fun changeMovieListCategory(movieListCategory: MovieListCategory) {
        moviePagedListDataSourceFactory.changeCategory(movieListCategory)
    }

    override fun retryFetchData() {
        moviePagedListDataSourceFactory.getExistingSource().retryAllRequest()
    }

    override fun onDestroy() {
        moviePagedListDataSourceFactory.dispose()
        compositeDisposable.dispose()
    }
}