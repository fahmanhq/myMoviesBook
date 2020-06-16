package labs.mamangkompii.mymoviesbook.di.component

import dagger.BindsInstance
import dagger.Subcomponent
import labs.mamangkompii.mymoviesbook.di.annotation.ActivityScope
import labs.mamangkompii.mymoviesbook.di.annotation.Params
import labs.mamangkompii.mymoviesbook.di.module.FavoriteMovieModule
import labs.mamangkompii.mymoviesbook.di.module.MovieDetailsModule
import labs.mamangkompii.mymoviesbook.view.moviedetail.MovieDetailActivity
import labs.mamangkompii.mymoviesbook.view.moviedetail.MovieDetailView
import labs.mamangkompii.mymoviesbook.view.moviedetail.MovieReviewsView

@ActivityScope
@Subcomponent(modules = [MovieDetailsModule::class, FavoriteMovieModule::class])
interface MovieDetailsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(
            @BindsInstance movieDetailView: MovieDetailView,
            @BindsInstance movieReviewsView: MovieReviewsView,
            @BindsInstance @Params("movieId") movieId: Int
        ): MovieDetailsComponent
    }

    fun inject(movieDetailActivity: MovieDetailActivity)
}