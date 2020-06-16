package labs.mamangkompii.mymoviesbook.di.module

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import labs.mamangkompii.mymoviesbook.di.annotation.ActivityScope
import labs.mamangkompii.mymoviesbook.gateway.local.db.AppDatabase
import labs.mamangkompii.mymoviesbook.gateway.local.db.dao.FavoriteMovieDao
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.AddOrRemoveFavoriteMovieUseCase
import labs.mamangkompii.mymoviesbook.usecase.moviedetail.AddOrRemoveFavoriteMovieUseCaseImpl

@Module
interface FavoriteMovieModule {

    @ActivityScope
    @Binds
    fun addOrRemoveFavoriteMovieUseCase(addOrRemoveFavoriteMovieUseCaseImpl: AddOrRemoveFavoriteMovieUseCaseImpl): AddOrRemoveFavoriteMovieUseCase

    companion object {

        @Provides
        fun favoriteMovieDao(context: Context): FavoriteMovieDao {
            return AppDatabase.getInstance(context).favoriteMovieDao()
        }
    }
}