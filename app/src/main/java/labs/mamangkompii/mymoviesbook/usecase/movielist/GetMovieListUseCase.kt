package labs.mamangkompii.mymoviesbook.usecase.movielist

import io.reactivex.Single
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary

interface GetMovieListUseCase {
    fun getPopularMovies(pageIndex: Int): Single<List<MovieSummary>>
    fun getTopRatedMovies(pageIndex: Int): Single<List<MovieSummary>>
    fun getNowPlayingMovies(pageIndex: Int): Single<List<MovieSummary>>
    fun getFavorites(pageIndex: Int, numOfRequestedData: Int): Single<List<MovieSummary>>
}