package labs.mamangkompii.mymoviesbook.presenter.moviedetail

import android.util.Log
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import labs.mamangkompii.mymoviesbook.di.annotation.MainScheduler
import labs.mamangkompii.mymoviesbook.presenter.movielist.LoadState
import labs.mamangkompii.mymoviesbook.view.moviedetail.MovieReviewsView
import javax.inject.Inject

class MovieReviewPresenterImpl @Inject constructor(
    private val view: MovieReviewsView,
    private val movieReviewPagedListDataSourceFactory: MovieReviewPagedListDataSourceFactory,
    private val compositeDisposable: CompositeDisposable,
    @MainScheduler private val mainScheduler: Scheduler
) : MovieReviewPresenter {

    private var isInit = true

    override fun requestReview() {
        if (isInit) {
            isInit = false

            val config: PagedList.Config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPrefetchDistance(10)
                .setPageSize(20)
                .build()

            compositeDisposable.add(
                RxPagedListBuilder(movieReviewPagedListDataSourceFactory, config)
                    .buildObservable()
                    .subscribe {
                        view.showReviews(it)
                    }
            )

            compositeDisposable.add(
                movieReviewPagedListDataSourceFactory.loadStateSubject
                    .observeOn(mainScheduler)
                    .subscribe {
                        when (it) {
                            is LoadState.Error -> {
                                view.hideGetReviewLoadingProgress()
                                view.showErrorGettingReview()
                            }
                            is LoadState.InitialLoadFinished, LoadState.NoLoadMoreRemaining -> {
                                view.hideGetReviewLoadingProgress()
                                view.showEmptyPlaceholderIfNeeded()
                            }
                            is LoadState.LoadingInitialData -> {
                                view.showGetReviewLoadingProgress()
                            }
                        }
                    }
            )
        }
    }

    override fun retryRequestReview() {
        movieReviewPagedListDataSourceFactory.getExistingSource().retryAllRequest()
    }

    override fun onDestroy() {
        movieReviewPagedListDataSourceFactory.dispose()
        compositeDisposable.dispose()
    }
}