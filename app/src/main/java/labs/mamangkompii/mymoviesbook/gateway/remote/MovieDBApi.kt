package labs.mamangkompii.mymoviesbook.gateway.remote

import io.reactivex.Observable
import labs.mamangkompii.mymoviesbook.gateway.remote.model.MovieListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDBApi {

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int? = 1): Observable<MovieListResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int? = 1): Observable<MovieListResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("page") page: Int? = 1): Observable<MovieListResponse>
}