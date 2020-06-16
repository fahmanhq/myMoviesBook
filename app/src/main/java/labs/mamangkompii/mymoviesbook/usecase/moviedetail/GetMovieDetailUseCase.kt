package labs.mamangkompii.mymoviesbook.usecase.moviedetail

import io.reactivex.Single
import labs.mamangkompii.mymoviesbook.usecase.model.MovieDetail

interface GetMovieDetailUseCase {
    fun getMovieDetail(movieId: Int): Single<MovieDetail>
}