package labs.mamangkompii.mymoviesbook.presenter.moviedetail

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import labs.mamangkompii.mymoviesbook.usecase.model.MovieDetail
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.AddOrRemoveFavoriteMovieUseCase
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.GetMovieDetailUseCase
import labs.mamangkompii.mymoviesbook.view.moviedetail.MovieDetailView
import org.joda.time.DateTime
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieDetailPresenterTest {

    private lateinit var getMovieDetailUseCase: GetMovieDetailUseCase
    private lateinit var favoriteMovieUseCase: AddOrRemoveFavoriteMovieUseCase
    private lateinit var movieDetailPresenter: MovieDetailPresenter
    private lateinit var compositeDisposable: CompositeDisposable

    private val movieId = 1223
    private val view: MovieDetailView = mock()

    @Before
    fun setUp() {
        getMovieDetailUseCase = mock()
        favoriteMovieUseCase = mock()
        compositeDisposable = CompositeDisposable()

        movieDetailPresenter = MovieDetailPresenterImpl(
            view,
            getMovieDetailUseCase,
            favoriteMovieUseCase,
            compositeDisposable,
            Schedulers.trampoline()
        )
    }

    @Test
    fun requestDetail_WithSuccessResponse() {
        val movieDetail = createMovieDetail(false)

        whenever(getMovieDetailUseCase.getMovieDetail(movieId))
            .thenReturn(Single.just(movieDetail))

        movieDetailPresenter.requestDetail(movieId)

        val inOrder = inOrder(view)

        inOrder.verify(view).showLoadingMovieDetail()
        inOrder.verify(view).disableFavoriteButton()
        inOrder.verify(view).showMovieDetail(movieDetail)
        inOrder.verify(view).showAllWidget()
        inOrder.verify(view).enableFavoriteButton()
        inOrder.verify(view).toggleFavoriteState(false)
    }

    @Test
    fun requestDetail_WithErrorResponse() {
        whenever(getMovieDetailUseCase.getMovieDetail(movieId))
            .thenReturn(Single.error(Throwable()))

        movieDetailPresenter.requestDetail(movieId)

        val inOrder = inOrder(view)

        inOrder.verify(view).showLoadingMovieDetail()
        inOrder.verify(view).disableFavoriteButton()
        inOrder.verify(view).showErrorGettingMovieDetail()
    }

    @Test
    fun onFavoriteButtonClick_WithMovieIsNotFavoritedBefore_MustAddMovieAsFavorite() {
        whenever(getMovieDetailUseCase.getMovieDetail(movieId))
            .thenReturn(Single.just(createMovieDetail(false)))
        whenever(favoriteMovieUseCase.addMovieAsFavorite(any()))
            .thenReturn(Completable.complete())

        movieDetailPresenter.requestDetail(movieId)
        movieDetailPresenter.onFavoriteButtonClick()

        val argumentCaptor = argumentCaptor<Boolean>()

        val inOrder = inOrder(view)

        inOrder.verify(view).disableFavoriteButton()
        inOrder.verify(view).enableFavoriteButton()
        inOrder.verify(view).toggleFavoriteState(false)
        inOrder.verify(view).disableFavoriteButton()
        inOrder.verify(view).enableFavoriteButton()
        inOrder.verify(view).toggleFavoriteState(argumentCaptor.capture())

        assertTrue(argumentCaptor.firstValue)
    }

    private fun createMovieDetail(isFavorited: Boolean): MovieDetail {
        return MovieDetail(
            movieId, "", "", "", DateTime.now(), isFavorited
        )
    }

    @Test
    fun onFavoriteButtonClick_WithMovieIsFavoritedBefore_MustRemoveMovieFromFavorite() {
        whenever(getMovieDetailUseCase.getMovieDetail(movieId))
            .thenReturn(Single.just(createMovieDetail(true)))
        whenever(favoriteMovieUseCase.removeMovieFromFavorite(any()))
            .thenReturn(Completable.complete())

        movieDetailPresenter.requestDetail(movieId)
        movieDetailPresenter.onFavoriteButtonClick()

        val argumentCaptor = argumentCaptor<Boolean>()

        val inOrder = inOrder(view)

        inOrder.verify(view).disableFavoriteButton()
        inOrder.verify(view).enableFavoriteButton()
        inOrder.verify(view).toggleFavoriteState(true)
        inOrder.verify(view).disableFavoriteButton()
        inOrder.verify(view).enableFavoriteButton()
        inOrder.verify(view).toggleFavoriteState(argumentCaptor.capture())

        assertFalse(argumentCaptor.firstValue)
    }

    @Test
    fun onDestroy_MustDisposeCompositeDisposable() {
        movieDetailPresenter.onDestroy()
        assertTrue(compositeDisposable.isDisposed)
    }
}