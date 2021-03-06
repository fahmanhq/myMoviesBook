package labs.mamangkompii.mymoviesbook.usecase.model

import org.joda.time.DateTime
import java.io.Serializable

data class MovieSummary(
    var id: Int,
    var title: String,
    var overview: String?,
    var posterPath: String?,
    var releaseDate: DateTime?,
    val isFavorited: Boolean = false
) : Serializable