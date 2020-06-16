package labs.mamangkompii.mymoviesbook.usecase.moviedetail

import io.reactivex.Completable
import labs.mamangkompii.mymoviesbook.usecase.model.MovieDetail

interface AddOrRemoveFavoriteMovieUseCase {

    fun addMovieAsFavorite(movieDetail: MovieDetail): Completable
    fun removeMovieFromFavorite(movieId: Int): Completable

}