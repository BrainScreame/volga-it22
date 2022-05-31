package com.osenov.trades.di

import com.google.gson.Gson
import com.osenov.trades.BuildConfig
import com.osenov.trades.Config.STOCK_URL
import com.osenov.trades.data.remote.StockService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        //val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .addInterceptor {
                val newUrl = it.request().url.newBuilder()
                    .addQueryParameter("token", BuildConfig.USER_ACCESS_TOKEN).build()
                it.proceed(it.request().newBuilder().url(newUrl).build())
            }
            //.addInterceptor(logging)
            //.addInterceptor(TimeoutInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideWeatherService(
        gsonConverterFactory: GsonConverterFactory,
        okHttpClient: OkHttpClient
    ): StockService {
        return Retrofit.Builder()
            .baseUrl(STOCK_URL)
            .addConverterFactory(gsonConverterFactory)
            .client(okHttpClient)
            .build()
            .create(StockService::class.java)
    }
}