package labs.mamangkompii.mymoviesbook.di.component

import dagger.BindsInstance
import dagger.Subcomponent
import labs.mamangkompii.mymoviesbook.MainActivity
import labs.mamangkompii.mymoviesbook.di.annotation.ActivityScope
import labs.mamangkompii.mymoviesbook.di.module.MovieListModule
import labs.mamangkompii.mymoviesbook.view.movielist.MovieListView

@ActivityScope
@Subcomponent(modules = [MovieListModule::class])
interface MovieListComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance movieListView: MovieListView): MovieListComponent
    }

    fun inject(mainActivity: MainActivity)
}