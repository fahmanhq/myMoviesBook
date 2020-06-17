package labs.mamangkompii.mymoviesbook.presenter.moviedetail

import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import labs.mamangkompii.mymoviesbook.di.annotation.MainScheduler
import labs.mamangkompii.mymoviesbook.usecase.model.MovieDetail
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.AddOrRemoveFavoriteMovieUseCase
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.GetMovieDetailUseCase
import labs.mamangkompii.mymoviesbook.view.moviedetail.MovieDetailView
import javax.inject.Inject

class MovieDetailPresenterImpl @Inject constructor(
    private val view: MovieDetailView,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val favoriteMovieUseCase: AddOrRemoveFavoriteMovieUseCase,
    private val compositeDisposable: CompositeDisposable,
    @MainScheduler private val mainScheduler: Scheduler
) : MovieDetailPresenter {

    private var movieDetail: MovieDetail? = null

    override fun requestDetail(movieId: Int) {
        compositeDisposable.add(
            getMovieDetailUseCase.getMovieDetail(movieId)
                .observeOn(mainScheduler)
                .doOnSubscribe {
                    view.showLoadingMovieDetail()
                    view.disableFavoriteButton()
                }
                .subscribe({
                    movieDetail = it

                    view.showMovieDetail(it)
                    view.showAllWidget()
                    view.enableFavoriteButton()
                    view.toggleFavoriteState(it.isFavorited)
                }, {
                    view.showErrorGettingMovieDetail()
                })
        )
    }

    override fun onFavoriteButtonClick() {
        if (movieDetail!!.isFavorited) {
            removeMovieFromFavorite()
        } else {
            addMovieAsFavorite()
        }
    }

    private fun addMovieAsFavorite() {
        compositeDisposable.add(
            favoriteMovieUseCase.addMovieAsFavorite(movieDetail!!)
                .observeOn(mainScheduler)
                .doOnSubscribe { view.disableFavoriteButton() }
                .subscribe({
                    movieDetail!!.isFavorited = true

                    view.enableFavoriteButton()
                    view.toggleFavoriteState(true)
                }, {
                    view.enableFavoriteButton()
                    view.showErrorAddMovieAsFavorite()
                })
        )
    }

    private fun removeMovieFromFavorite() {
        compositeDisposable.add(
            favoriteMovieUseCase.removeMovieFromFavorite(movieDetail!!.id)
                .observeOn(mainScheduler)
                .doOnSubscribe { view.disableFavoriteButton() }
                .subscribe({
                    movieDetail!!.isFavorited = false

                    view.enableFavoriteButton()
                    view.toggleFavoriteState(false)
                }, {
                    view.enableFavoriteButton()
                    view.showErrorRemoveMovieFromFavorite()
                })
        )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }
}