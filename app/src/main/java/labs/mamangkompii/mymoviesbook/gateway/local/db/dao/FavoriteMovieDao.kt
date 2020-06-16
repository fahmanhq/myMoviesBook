package labs.mamangkompii.mymoviesbook.gateway.local.db.dao

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import labs.mamangkompii.mymoviesbook.gateway.local.db.entity.MovieSummaryEntity

@Dao
interface FavoriteMovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMovie(movieSummaryEntity: MovieSummaryEntity): Completable

    @Query("DELETE FROM movie_summary WHERE movie_id = :movieId")
    fun deleteMovieById(movieId: Int): Completable

    @Query("SELECT * FROM movie_summary ORDER BY title DESC")
    fun getAllMovies(): Single<List<MovieSummaryEntity>>

    @Query("SELECT * FROM movie_summary WHERE movie_id = :movieId")
    fun getMovies(movieId: Int): Single<MovieSummaryEntity>
}