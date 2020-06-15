package labs.mamangkompii.mymoviesbook.usecase.movielist

import io.reactivex.Observable
import io.reactivex.Scheduler
import labs.mamangkompii.mymoviesbook.di.annotation.ComputationScheduler
import labs.mamangkompii.mymoviesbook.gateway.remote.MovieDBApi
import labs.mamangkompii.mymoviesbook.usecase.model.ModelMapper
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import javax.inject.Inject

class GetMovieListUseCaseImpl @Inject constructor(
    private val movieDBApi: MovieDBApi,
    private val modelMapper: ModelMapper,
    @ComputationScheduler private val compScheduler: Scheduler
) : GetMovieListUseCase {

    override fun getPopularMovies(pageIndex: Int): Observable<List<MovieSummary>> {
        return movieDBApi.getPopularMovies(pageIndex)
            .observeOn(compScheduler)
            .map { modelMapper.map(it) }
    }
}