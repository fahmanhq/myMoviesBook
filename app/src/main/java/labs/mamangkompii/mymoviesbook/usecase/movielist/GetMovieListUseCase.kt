package labs.mamangkompii.mymoviesbook.usecase.movielist

import io.reactivex.Observable
import labs.mamangkompii.mymoviesbook.usecase.model.MovieSummary

interface GetMovieListUseCase {
    fun getPopularMovies(pageIndex: Int): Observable<List<MovieSummary>>
}