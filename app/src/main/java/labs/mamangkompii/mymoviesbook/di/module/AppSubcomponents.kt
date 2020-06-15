package labs.mamangkompii.mymoviesbook.di.module

import dagger.Module
import labs.mamangkompii.mymoviesbook.di.component.MovieListComponent

@Module(subcomponents = [MovieListComponent::class])
interface AppSubcomponents