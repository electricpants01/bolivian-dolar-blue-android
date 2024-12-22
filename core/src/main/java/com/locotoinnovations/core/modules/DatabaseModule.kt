package com.locotoinnovations.core.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.locotoinnovations.core.dolarbluedata.dao.DolarBlueDao
import com.locotoinnovations.core.dolarbluedata.operation.FetchDolarBlueDataOperation
import com.locotoinnovations.core.dolarbluedata.operation.ReadDolarBlueDataOperation
import com.locotoinnovations.core.dolarbluedata.operation.SaveDolarBlueDataOperation
import com.locotoinnovations.core.network.NetworkProvider
import com.locotoinnovations.core.repository.ApiTimestampRepositoryImpl
import com.locotoinnovations.core.repository.BinanceSearchRepository
import com.locotoinnovations.core.repository.BinanceSearchRepositoryImpl
import com.locotoinnovations.core.room.DolarBlueDatabase
import com.locotoinnovations.core.room.dao.ApiTimestampDao
import com.locotoinnovations.core.room.operation.apitimestamp.ApiTimestampRepository
import com.locototeam.bolivianbluedolar.network.binance_search.BinanceSearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun providesRoomDatabase(context: Context): DolarBlueDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = DolarBlueDatabase::class.java,
            name = DolarBlueDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun providesDolarBlueDao(db: DolarBlueDatabase) = db.dolarBlueDao()

    @Singleton
    @Provides
    fun providesSaveDolarBlueDataOperation(dolarBlueDao: DolarBlueDao): SaveDolarBlueDataOperation {
        return SaveDolarBlueDataOperation(dolarBlueDao)
    }

    @Singleton
    @Provides
    fun providesApiTimestampRepositoryImpl(apiTimestampDao: ApiTimestampDao): ApiTimestampRepository {
        return ApiTimestampRepositoryImpl(apiTimestampDao)
    }

    @Singleton
    @Provides
    fun providesBinanceSearchRepositoryImpl(
        dolarBlueDao: DolarBlueDao,
        binanceSearchService: BinanceSearchService,
        saveDolarBlueDataOperation: SaveDolarBlueDataOperation,
        apiTimestampRepository: ApiTimestampRepository,
    ): BinanceSearchRepository {
        return BinanceSearchRepositoryImpl(
            readDolarBlueDataOperation = ReadDolarBlueDataOperation(dolarBlueDao),
            fetchDolarBlueDataOperation = FetchDolarBlueDataOperation(
                binanceSearchService = binanceSearchService,
                apiTimestampRepository = apiTimestampRepository,
                networkProvider = NetworkProvider(),
                saveDolarBlueDataOperation = saveDolarBlueDataOperation
            )
        )
    }
}