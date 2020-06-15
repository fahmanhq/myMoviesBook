package labs.mamangkompii.mymoviesbook.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import labs.mamangkompii.mymoviesbook.di.annotation.ActivityScope
import labs.mamangkompii.mymoviesbook.di.annotation.ComputationScheduler
import labs.mamangkompii.mymoviesbook.presenter.movielist.MovieListPresenter
import labs.mamangkompii.mymoviesbook.presenter.movielist.MovieListPresenterImpl
import labs.mamangkompii.mymoviesbook.presenter.movielist.MoviePagedListDataSourceFactory
import labs.mamangkompii.mymoviesbook.usecase.movielist.GetMovieListUseCase
import labs.mamangkompii.mymoviesbook.usecase.movielist.GetMovieListUseCaseImpl
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

@Module
interface MovieListModule {

    @ActivityScope
    @Binds
    fun movieListUseCase(movieListUseCaseImpl: GetMovieListUseCaseImpl): GetMovieListUseCase

    @ActivityScope
    @Binds
    fun movieListPresenter(movieListPresenterImpl: MovieListPresenterImpl): MovieListPresenter

    companion object {

        @ActivityScope
        @Provides
        fun moviePagedListDataSourceFactory(
            getMovieListUseCase: GetMovieListUseCase,
            @ComputationScheduler scheduler: Scheduler
        ): MoviePagedListDataSourceFactory {
            return MoviePagedListDataSourceFactory(
                getMovieListUseCase,
                CompositeDisposable(),
                PublishSubject.create(),
                scheduler
            )
        }

        @Provides
        fun movieReleaseDateTimeFormatter(): DateTimeFormatter {
            return DateTimeFormat.forPattern("YYYY-MM-dd")
        }

//        @Provides
//        @ActivityScope
//        fun providePresenter(view: MovieListView) {
//            return MovieListPresenterImpl(
//                view,
//                pagedListDataSourceFactory,
//                getMovieListUseCase,
//
//            )
//        }
    }
}