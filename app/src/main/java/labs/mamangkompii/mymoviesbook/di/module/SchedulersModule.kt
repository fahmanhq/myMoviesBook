package labs.mamangkompii.mymoviesbook.di.module

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import labs.mamangkompii.mymoviesbook.di.annotation.ComputationScheduler
import labs.mamangkompii.mymoviesbook.di.annotation.IOScheduler
import labs.mamangkompii.mymoviesbook.di.annotation.MainScheduler

@Module
interface SchedulersModule {
    companion object {
        @Provides
        @ComputationScheduler
        fun computationScheduler(): Scheduler = Schedulers.computation()

        @Provides
        @IOScheduler
        fun ioScheduler(): Scheduler = Schedulers.io()

        @Provides
        @MainScheduler
        fun mainScheduler(): Scheduler = AndroidSchedulers.mainThread()

        @Provides
        fun compositeDisposable(): CompositeDisposable = CompositeDisposable()
    }
}