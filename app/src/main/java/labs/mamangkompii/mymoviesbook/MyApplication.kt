package labs.mamangkompii.mymoviesbook

import android.app.Application
import labs.mamangkompii.mymoviesbook.di.component.AppComponent
import labs.mamangkompii.mymoviesbook.di.component.DaggerAppComponent

class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}