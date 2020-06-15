package labs.mamangkompii.mymoviesbook.di.module

import dagger.Module
import dagger.Provides
import labs.mamangkompii.mymoviesbook.di.annotation.Constants
import labs.mamangkompii.mymoviesbook.di.annotation.QualifierKeys
import labs.mamangkompii.mymoviesbook.gateway.remote.MovieAPIFactory
import labs.mamangkompii.mymoviesbook.gateway.remote.MovieDBApi
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Module
interface APIModule {

    companion object {
        @Provides
        fun moviesDBApi(): MovieDBApi = MovieAPIFactory.movieDBApi

        @Provides
        @Constants(QualifierKeys.POSTER_BASE_URL)
        fun posterBaseUrl(): String = "https://image.tmdb.org/t/p/w500/"

        @Provides
        fun movieReleaseDateTimeFormatter(): DateTimeFormatter {
            return DateTimeFormat.forPattern("YYYY-MM-dd")
        }
    }
}