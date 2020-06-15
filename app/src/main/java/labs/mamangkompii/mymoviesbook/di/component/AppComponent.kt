package labs.mamangkompii.mymoviesbook.di.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import labs.mamangkompii.mymoviesbook.di.module.APIModule
import labs.mamangkompii.mymoviesbook.di.module.AppSubcomponents
import labs.mamangkompii.mymoviesbook.di.module.SchedulersModule

@Component(modules = [SchedulersModule::class, APIModule::class, AppSubcomponents::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun movieListComponent(): MovieListComponent.Factory
}