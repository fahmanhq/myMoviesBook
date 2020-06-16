package labs.mamangkompii.mymoviesbook.di.module

import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import labs.mamangkompii.mymoviesbook.di.annotation.ActivityScope
import labs.mamangkompii.mymoviesbook.di.annotation.ComputationScheduler
import labs.mamangkompii.mymoviesbook.di.annotation.Params
import labs.mamangkompii.mymoviesbook.presenter.moviedetail.*
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.GetMovieDetailUseCase
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.GetMovieDetailUseCaseImpl
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.GetMovieReviewsUseCase
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.GetMovieReviewsUseCaseImpl

@Module
interface MovieDetailsModule {

    @ActivityScope
    @Binds
    fun movieDetailsUseCase(getMovieDetailUseCaseImpl: GetMovieDetailUseCaseImpl): GetMovieDetailUseCase

    @ActivityScope
    @Binds
    fun movieReviewsUseCase(movieReviewsUseCaseImpl: GetMovieReviewsUseCaseImpl): GetMovieReviewsUseCase

    @ActivityScope
    @Binds
    fun movieDetailsPresenter(movieDetailPresenterImpl: MovieDetailPresenterImpl): MovieDetailPresenter

    @ActivityScope
    @Binds
    fun movieReviewsPresenter(movieReviewPresenterImpl: MovieReviewPresenterImpl): MovieReviewPresenter

    companion object {

        @ActivityScope
        @Provides
        fun movieReviewPagedListDataSourceFactory(
            @Params("movieId") movieId: Int,
            getMovieReviewsUseCase: GetMovieReviewsUseCase,
            @ComputationScheduler scheduler: Scheduler
        ): MovieReviewPagedListDataSourceFactory {
            return MovieReviewPagedListDataSourceFactory(
                movieId,
                getMovieReviewsUseCase,
                CompositeDisposable(),
                PublishSubject.create(),
                scheduler
            )
        }
    }

}