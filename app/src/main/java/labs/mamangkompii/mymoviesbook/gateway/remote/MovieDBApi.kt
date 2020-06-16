package labs.mamangkompii.mymoviesbook.gateway.remote

import io.reactivex.Observable
import io.reactivex.Single
import labs.mamangkompii.mymoviesbook.gateway.remote.model.MovieDetailApiModel
import labs.mamangkompii.mymoviesbook.gateway.remote.model.MovieListResponse
import labs.mamangkompii.mymoviesbook.gateway.remote.model.MovieReviewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieDBApi {

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int? = 1): Observable<MovieListResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int? = 1): Observable<MovieListResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("page") page: Int? = 1): Observable<MovieListResponse>

    @GET("movie/{movieId}")
    fun getMovieDetails(@Path("movieId") movieId: Int): Single<MovieDetailApiModel>

    @GET("movie/{movieId}/reviews")
    fun getMovieReviews(
        @Path("movieId") movieId: Int,
        @Query("page") page: Int? = 1
    ): Single<MovieReviewsResponse>
}