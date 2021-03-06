package labs.mamangkompii.mymoviesbook.di.component

import dagger.BindsInstance
import dagger.Subcomponent
import labs.mamangkompii.mymoviesbook.view.movielist.MovieListActivity
import labs.mamangkompii.mymoviesbook.di.annotation.ActivityScope
import labs.mamangkompii.mymoviesbook.di.module.FavoriteMovieModule
import labs.mamangkompii.mymoviesbook.di.module.MovieListModule
import labs.mamangkompii.mymoviesbook.view.movielist.MovieListView

@ActivityScope
@Subcomponent(modules = [MovieListModule::class, FavoriteMovieModule::class])
interface MovieListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance movieListView: MovieListView): MovieListComponent
    }

    fun inject(movieListActivity: MovieListActivity)
}