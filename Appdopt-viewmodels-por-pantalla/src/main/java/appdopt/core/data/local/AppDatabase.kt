package com.example.appdopt.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appdopt.core.model.AdoptionRequest
import com.example.appdopt.core.model.Pet

@Database(entities = [Pet::class, AdoptionRequest::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun adoptionRequestDao(): AdoptionRequestDao
}
