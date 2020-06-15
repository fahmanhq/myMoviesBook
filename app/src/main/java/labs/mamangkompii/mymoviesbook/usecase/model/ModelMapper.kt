package labs.mamangkompii.mymoviesbook.usecase.model

import labs.mamangkompii.mymoviesbook.di.annotation.Constants
import labs.mamangkompii.mymoviesbook.di.annotation.QualifierKeys
import labs.mamangkompii.mymoviesbook.gateway.remote.MovieAPIFactory
import labs.mamangkompii.mymoviesbook.gateway.remote.model.MovieListResponse
import org.joda.time.format.DateTimeFormatter
import javax.inject.Inject

class ModelMapper @Inject constructor(
    @Constants(QualifierKeys.POSTER_BASE_URL) private val posterBaseUrl: String,
    private val movieReleaseDateTimeFormatter: DateTimeFormatter
) {

    fun map(movieListResponse: MovieListResponse): List<MovieSummary> {
        val resultList = ArrayList<MovieSummary>()

        movieListResponse.results?.forEach {
            if (it.isMinimumDataAcquired()) {
                resultList.add(
                    MovieSummary(
                        id = it.id!!,
                        title = it.title!!,
                        overview = it.overview,
                        posterPath = it.posterPath?.run { posterBaseUrl + this },
                        releaseDate = it.releaseDate?.let(movieReleaseDateTimeFormatter::parseDateTime)
                    )
                )
            }
        }

        return resultList
    }
}