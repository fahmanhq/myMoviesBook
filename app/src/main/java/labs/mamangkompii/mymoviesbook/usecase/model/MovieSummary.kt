package labs.mamangkompii.mymoviesbook.usecase.model

import org.joda.time.DateTime

data class MovieSummary(
    var id: String,
    var title: String,
    var overview: String?,
    var posterPath: String?,
    var releaseDate: DateTime?
)