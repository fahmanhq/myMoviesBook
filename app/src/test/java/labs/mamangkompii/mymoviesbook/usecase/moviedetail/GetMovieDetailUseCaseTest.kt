package labs.mamangkompii.mymoviesbook.usecase.moviedetail

import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import labs.mamangkompii.mymoviesbook.gateway.local.db.dao.FavoriteMovieDao
import labs.mamangkompii.mymoviesbook.gateway.local.db.entity.MovieSummaryEntity
import labs.mamangkompii.mymoviesbook.gateway.remote.MovieDBApi
import labs.mamangkompii.mymoviesbook.gateway.remote.model.MovieDetailApiModel
import labs.mamangkompii.mymoviesbook.usecase.model.ApiToDomainModelMapper
import labs.mamangkompii.mymoviesbook.usecase.model.MovieDetail
import org.joda.time.format.DateTimeFormatter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetMovieDetailUseCaseTest {

    private lateinit var movieDBApi: MovieDBApi
    private lateinit var favoriteMovieDao: FavoriteMovieDao
    private var apiToDomainModelMapper: ApiToDomainModelMapper

    private lateinit var getMovieDetailUseCase: GetMovieDetailUseCase

    init {
        val dateTimeFormatter = mock<DateTimeFormatter>()
        apiToDomainModelMapper = ApiToDomainModelMapper("", dateTimeFormatter)
    }

    @Before
    fun setup() {
        movieDBApi = mock()
        favoriteMovieDao = mock()

        getMovieDetailUseCase = GetMovieDetailUseCaseImpl(
            movieDBApi,
            favoriteMovieDao,
            apiToDomainModelMapper,
            Schedulers.trampoline()
        )
    }

    @Test
    fun getMovieDetail_WithMovieInFavoriteDB_ShouldReturnMovieDetailWithFavoriteStateIsTrue() {
        val testObserver = TestObserver<MovieDetail>()
        val movieId = 1231

        val mockMovieDetailApiModel = MovieDetailApiModel().apply {
            id = movieId
            title = ""
        }
        whenever(movieDBApi.getMovieDetails(movieId)).thenReturn(Single.just(mockMovieDetailApiModel))
        val dummyMovieSummaryEntity = MovieSummaryEntity(
            movieId,
            "", "", "", ""
        )
        whenever(favoriteMovieDao.getMovie(movieId)).thenReturn(Single.just(dummyMovieSummaryEntity))

        getMovieDetailUseCase.getMovieDetail(movieId).subscribe(testObserver)

        verify(movieDBApi).getMovieDetails(eq(movieId))
        verify(favoriteMovieDao).getMovie(eq(movieId))

        testObserver.assertValue { it.isFavorited }
    }

    @Test
    fun getMovieDetail_WithMovieNotInFavoriteDB_ShouldReturnMovieDetailWithFavoriteStateIsFalse() {
        val testObserver = TestObserver<MovieDetail>()
        val movieId = 1231

        val mockMovieDetailApiModel = MovieDetailApiModel().apply {
            id = movieId
            title = ""
        }
        whenever(movieDBApi.getMovieDetails(movieId)).thenReturn(Single.just(mockMovieDetailApiModel))
        whenever(favoriteMovieDao.getMovie(movieId)).thenReturn(Single.error(Throwable()))

        getMovieDetailUseCase.getMovieDetail(movieId).subscribe(testObserver)

        verify(movieDBApi).getMovieDetails(eq(movieId))
        verify(favoriteMovieDao).getMovie(eq(movieId))

        testObserver.assertValue { !it.isFavorited }
    }
}