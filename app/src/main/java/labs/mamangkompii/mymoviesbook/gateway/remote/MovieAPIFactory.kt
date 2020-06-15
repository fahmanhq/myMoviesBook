package labs.mamangkompii.mymoviesbook.gateway.remote

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object MovieAPIFactory {

    private const val MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/"
    private const val URL_QUERY_API_KEY = "api_key"
    private const val API_KEY = "6c927da259a796eaec873d0eff0ec2b7"

    private val authInterceptor = Interceptor { chain ->
        val enrichedUrl = chain.request().url
            .newBuilder()
            .addQueryParameter(
                URL_QUERY_API_KEY,
                API_KEY
            )
            .build()

        val updatedRequest = chain.request()
            .newBuilder()
            .url(enrichedUrl)
            .build()

        chain.proceed(updatedRequest)
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(authInterceptor)
        .build()

    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(MOVIE_DB_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .build()

    val movieDBApi = retrofit.create(MovieDBApi::class.java)
}