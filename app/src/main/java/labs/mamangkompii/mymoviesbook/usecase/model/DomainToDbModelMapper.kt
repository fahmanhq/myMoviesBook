package labs.mamangkompii.mymoviesbook.usecase.model

import labs.mamangkompii.mymoviesbook.gateway.local.db.entity.MovieSummaryEntity
import javax.inject.Inject

class DomainToDbModelMapper @Inject constructor() {
    fun toDBModel(movieDetail: MovieDetail): MovieSummaryEntity {
        return MovieSummaryEntity(
            movieDetail.id,
            movieDetail.title,
            movieDetail.overview ?: "",
            movieDetail.posterPath ?: "",
            movieDetail.releaseDate?.toString("YYYY-MM-dd") ?: ""
        )
    }
}