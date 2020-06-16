package labs.mamangkompii.mymoviesbook.usecase.movielist

import io.reactivex.Observable
import labs.mamangkompii.mymoviesbook.gateway.remote.model.MovieListResponse
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary
import retrofit2.http.GET
import retrofit2.http.Query

interface GetMovieListUseCase {
    fun getPopularMovies(pageIndex: Int): Observable<List<MovieSummary>>
    fun getTopRatedMovies(pageIndex: Int): Observable<List<MovieSummary>>
    fun getNowPlayingMovies(pageIndex: Int): Observable<List<MovieSummary>>
}