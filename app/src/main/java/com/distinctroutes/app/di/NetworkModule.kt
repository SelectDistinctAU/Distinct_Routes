package com.distinctroutes.app.di

import android.content.Context
import com.distinctroutes.app.data.api.BBBikeApiService
import com.distinctroutes.app.data.api.NominatimApiService
import com.distinctroutes.app.util.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Dependency injection module for network and storage layers.
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(Constants.NETWORK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .readTimeout(Constants.NETWORK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(Constants.NETWORK_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .connectionPool(
                ConnectionPool(
                    maxIdleConnections = 5,
                    keepAliveDuration = 5,
                    timeUnit = TimeUnit.MINUTES
                )
            )
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "DistinctRoutes/0.1.0 (Android; OpenSource Cycling Navigator)")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://router.project-osrm.org/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideBBBikeApiService(retrofit: Retrofit): BBBikeApiService {
        return retrofit.create(BBBikeApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNominatimApiService(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): NominatimApiService {
        return Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(NominatimApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCacheDirectory(@ApplicationContext context: Context): File {
        return File(context.cacheDir, "routes")
    }
}