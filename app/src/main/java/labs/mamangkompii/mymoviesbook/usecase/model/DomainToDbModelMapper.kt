package labs.mamangkompii.mymoviesbook.usecase.model

import labs.mamangkompii.mymoviesbook.gateway.local.db.entity.MovieSummaryEntity
import org.joda.time.format.DateTimeFormat
import javax.inject.Inject

class DomainToDbModelMapper @Inject constructor() {

    private val dateTimeParser = DateTimeFormat.forPattern("YYYY-MM-dd")

    fun toDBModel(movieDetail: MovieDetail): MovieSummaryEntity {
        return MovieSummaryEntity(
            movieDetail.id,
            movieDetail.title,
            movieDetail.overview ?: "",
            movieDetail.posterPath ?: "",
            movieDetail.releaseDate?.toString("YYYY-MM-dd") ?: ""
        )
    }

    fun map(movieSummaryEntities: List<MovieSummaryEntity>) : List<MovieSummary> {
        return movieSummaryEntities.map {
            MovieSummary(
                it.movieId,
                it.title,
                it.overview,
                it.posterPath,
                it.releaseDate.run { dateTimeParser.parseDateTime(this) }
            )
        }
    }
}