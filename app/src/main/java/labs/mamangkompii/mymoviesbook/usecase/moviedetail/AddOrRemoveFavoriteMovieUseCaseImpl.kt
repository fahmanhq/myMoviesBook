package labs.mamangkompii.mymoviesbook.usecase.moviedetail

import io.reactivex.Completable
import io.reactivex.Scheduler
import labs.mamangkompii.mymoviesbook.di.annotation.ComputationScheduler
import labs.mamangkompii.mymoviesbook.di.annotation.IOScheduler
import labs.mamangkompii.mymoviesbook.gateway.local.db.dao.FavoriteMovieDao
import labs.mamangkompii.mymoviesbook.usecase.model.DomainToDbModelMapper
import labs.mamangkompii.mymoviesbook.usecase.model.MovieDetail
import javax.inject.Inject

class AddOrRemoveFavoriteMovieUseCaseImpl @Inject constructor(
    private val favoriteMovieDao: FavoriteMovieDao,
    private val domainToDbModelMapper: DomainToDbModelMapper,
    @IOScheduler private val ioScheduler: Scheduler,
    @ComputationScheduler private val compScheduler: Scheduler
) : AddOrRemoveFavoriteMovieUseCase {

    override fun addMovieAsFavorite(movieDetail: MovieDetail): Completable {
        return favoriteMovieDao.insertMovie(domainToDbModelMapper.toDBModel(movieDetail))
            .subscribeOn(ioScheduler)
            .observeOn(compScheduler)
    }

    override fun removeMovieFromFavorite(movieId: Int): Completable {
        return favoriteMovieDao.deleteMovieById(movieId)
            .subscribeOn(ioScheduler)
            .observeOn(compScheduler)
    }
}