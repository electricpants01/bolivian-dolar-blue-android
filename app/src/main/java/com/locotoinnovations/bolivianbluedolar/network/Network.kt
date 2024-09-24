package com.locotoinnovations.bolivianbluedolar.network

import com.locotoinnovations.bolivianbluedolar.network.binance_search.BinanceSearchService
import com.locotoinnovations.bolivianbluedolar.ui.screen.BinanceSearchRepository
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
object Network {

    private const val API_PROD = "https://p2p.binance.com/"

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(API_PROD)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun providesBinanceSearchService(retrofit: Retrofit): BinanceSearchService {
        return retrofit.create(BinanceSearchService::class.java)
    }

    @Provides
    @Singleton
    fun providesBinanceSearchRepository(todoService: BinanceSearchService): BinanceSearchRepository {
        return BinanceSearchRepository(todoService)
    }
}