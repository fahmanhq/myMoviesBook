package labs.mamangkompii.mymoviesbook.di.annotation

import javax.inject.Qualifier

@Qualifier
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ComputationScheduler

@Qualifier
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class IOScheduler

@Qualifier
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class MainScheduler

@Qualifier
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class Constants(val value: String)


