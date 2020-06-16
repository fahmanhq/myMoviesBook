package labs.mamangkompii.mymoviesbook.usecase.moviedetail

import io.reactivex.Single
import labs.mamangkompii.mymoviesbook.usecase.model.MovieReviewItem

interface GetMovieReviewsUseCase {
    fun getMovieReviews(movieId: Int, page: Int): Single<List<MovieReviewItem>>
}