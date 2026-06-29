package com.example.appdopt.core.di

import android.content.Context
import androidx.room.Room
import com.example.appdopt.core.data.local.AdoptionRequestDao
import com.example.appdopt.core.data.local.AppDatabase
import com.example.appdopt.core.data.local.PetDao
import com.example.appdopt.core.data.repository.AdoptionRepository
import com.example.appdopt.core.data.repository.OfflineAdoptionRepository
import com.example.appdopt.core.data.repository.OfflinePetRepository
import com.example.appdopt.core.data.repository.PetRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindPetRepository(
        offlinePetRepository: OfflinePetRepository
    ): PetRepository

    @Binds
    @Singleton
    abstract fun bindAdoptionRepository(
        offlineAdoptionRepository: OfflineAdoptionRepository
    ): AdoptionRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "appdopt_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun providePetDao(database: AppDatabase): PetDao = database.petDao()

    @Provides
    fun provideAdoptionRequestDao(database: AppDatabase): AdoptionRequestDao = 
        database.adoptionRequestDao()
}
