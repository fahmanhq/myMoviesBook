package labs.mamangkompii.mymoviesbook.usecase.movielist

import io.reactivex.Scheduler
import io.reactivex.Single
import labs.mamangkompii.mymoviesbook.di.annotation.ComputationScheduler
import labs.mamangkompii.mymoviesbook.di.annotation.IOScheduler
import labs.mamangkompii.mymoviesbook.gateway.local.db.dao.FavoriteMovieDao
import labs.mamangkompii.mymoviesbook.gateway.remote.MovieDBApi
import labs.mamangkompii.mymoviesbook.usecase.model.ApiToDomainModelMapper
import labs.mamangkompii.mymoviesbook.usecase.model.DomainToDbModelMapper
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import javax.inject.Inject

class GetMovieListUseCaseImpl @Inject constructor(
    private val movieDBApi: MovieDBApi,
    private val favoriteMovieDao: FavoriteMovieDao,
    private val apiToDomainModelMapper: ApiToDomainModelMapper,
    private val domainToDbModelMapper: DomainToDbModelMapper,
    @IOScheduler private val ioScheduler: Scheduler,
    @ComputationScheduler private val compScheduler: Scheduler
) : GetMovieListUseCase {

    override fun getPopularMovies(pageIndex: Int): Single<List<MovieSummary>> {
        return movieDBApi.getPopularMovies(pageIndex)
            .observeOn(compScheduler)
            .map { apiToDomainModelMapper.map(it) }
    }

    override fun getTopRatedMovies(pageIndex: Int): Single<List<MovieSummary>> {
        return movieDBApi.getTopRatedMovies(pageIndex)
            .observeOn(compScheduler)
            .map { apiToDomainModelMapper.map(it) }
    }

    override fun getNowPlayingMovies(pageIndex: Int): Single<List<MovieSummary>> {
        return movieDBApi.getNowPlayingMovies(pageIndex)
            .observeOn(compScheduler)
            .map { apiToDomainModelMapper.map(it) }
    }

    override fun getFavorites(
        pageIndex: Int,
        numOfRequestedData: Int
    ): Single<List<MovieSummary>> {
        val offset = (pageIndex - 1) * numOfRequestedData
        return favoriteMovieDao.getMoviesByPaging(numOfRequestedData, offset)
            .subscribeOn(ioScheduler)
            .observeOn(compScheduler)
            .map { domainToDbModelMapper.map(it) }
    }
}