package labs.mamangkompii.mymoviesbook.usecase.model

import labs.mamangkompii.mymoviesbook.di.annotation.Constants
import labs.mamangkompii.mymoviesbook.di.annotation.QualifierKeys
import labs.mamangkompii.mymoviesbook.gateway.remote.model.MovieListResponse
import labs.mamangkompii.mymoviesbook.gateway.remote.model.MovieReviewsResponse
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
                        it.id!!,
                        it.title!!,
                        it.overview,
                        it.posterPath?.run { posterBaseUrl + this },
                        it.releaseDate?.let(movieReleaseDateTimeFormatter::parseDateTime)
                    )
                )
            }
        }

        return resultList
    }

    fun map(movieReviewsResponse: MovieReviewsResponse): List<MovieReviewItem> {
        return movieReviewsResponse.results?.map {
            MovieReviewItem(
                it.id!!,
                it.author,
                it.content
            )
        } ?: emptyList()
    }
}