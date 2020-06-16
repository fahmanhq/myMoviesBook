package labs.mamangkompii.mymoviesbook.usecase.moviedetail

import io.reactivex.Scheduler
import io.reactivex.Single
import labs.mamangkompii.mymoviesbook.di.annotation.ComputationScheduler
import labs.mamangkompii.mymoviesbook.gateway.local.db.dao.FavoriteMovieDao
import labs.mamangkompii.mymoviesbook.gateway.remote.MovieDBApi
import labs.mamangkompii.mymoviesbook.usecase.model.ApiToDomainModelMapper
import labs.mamangkompii.mymoviesbook.usecase.model.MovieDetail
import javax.inject.Inject

class GetMovieDetailUseCaseImpl @Inject constructor(
    private val movieDBApi: MovieDBApi,
    private val favoriteMovieDao: FavoriteMovieDao,
    private val apiToDomainModelMapper: ApiToDomainModelMapper,
    @ComputationScheduler private val compScheduler: Scheduler
) : GetMovieDetailUseCase {

    override fun getMovieDetail(movieId: Int): Single<MovieDetail> {
        return movieDBApi.getMovieDetails(movieId)
            .observeOn(compScheduler)
            .map { apiToDomainModelMapper.map(it) }
            .flatMap { movieDetail ->
                favoriteMovieDao.getMovie(movieId)
                    .map { movieDetail.apply { isFavorited = true } }
                    .onErrorReturnItem(movieDetail)
            }
    }
}