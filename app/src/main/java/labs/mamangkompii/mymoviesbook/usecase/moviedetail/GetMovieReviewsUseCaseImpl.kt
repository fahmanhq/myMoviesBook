package labs.mamangkompii.mymoviesbook.usecase.moviedetail

import io.reactivex.Scheduler
import io.reactivex.Single
import labs.mamangkompii.mymoviesbook.di.annotation.ComputationScheduler
import labs.mamangkompii.mymoviesbook.gateway.remote.MovieDBApi
import labs.mamangkompii.mymoviesbook.usecase.model.ModelMapper
import labs.mamangkompii.mymoviesbook.usecase.model.MovieReviewItem
import javax.inject.Inject

class GetMovieReviewsUseCaseImpl @Inject constructor(
    private val movieDBApi: MovieDBApi,
    private val modelMapper: ModelMapper,
    @ComputationScheduler private val compScheduler: Scheduler
) : GetMovieReviewsUseCase {

    override fun getMovieReviews(movieId: String, page: Int): Single<List<MovieReviewItem>> {
        return movieDBApi.getMovieReviews(movieId, page)
            .observeOn(compScheduler)
            .map { modelMapper.map(it) }
    }
}