package com.example.everypractice.di

import com.example.everypractice.data.red.*
import dagger.*
import dagger.hilt.*
import dagger.hilt.components.*
import okhttp3.*
import okhttp3.Interceptor.Chain
import okhttp3.logging.*
import retrofit2.*
import retrofit2.converter.gson.*
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.*

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    const val API_KEY = "21ff02d316f95915429ebe923b39a82c"
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    private const val API_TOKEN_KEY_V4 = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIyMWZmMDJkMzE2Zjk1OTE1NDI5ZWJlOTIzYjM5YTgyYyIsInN1YiI6IjYzNjgwODE5MWU5MjI1MDA4MjYyMzA4NSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.Tvu87rmVDgbkwwxm5K0cO3xJtV-eKOjECT-5cJDYMHo"

    @Provides
    @Singleton
    fun provideHttpTimberInterceptor() : HttpLoggingInterceptor {
        val customLogger = HttpLoggingInterceptor.Logger {
            //Log.d("OkHttp", message)
            //Timber.tag("OkHttp").v(message)
        }
        return HttpLoggingInterceptor(customLogger).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        timberInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(timberInterceptor)
        .addInterceptor{ chain: Chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $API_TOKEN_KEY_V4")
                .addHeader("Content-Type","application/json;charset=utf-8")
                .build()
            chain.proceed(request)
        }
        .callTimeout(timeout = 20L, SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideApiServices(
        retrofit: Retrofit
    ): MovieApiService = retrofit.create(MovieApiService::class.java)

}