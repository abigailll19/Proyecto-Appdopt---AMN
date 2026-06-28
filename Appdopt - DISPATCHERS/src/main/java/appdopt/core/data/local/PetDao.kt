package com.example.appdopt.core.data.local

import androidx.room.*
import com.example.appdopt.core.model.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pets")
    fun getAllPets(): Flow<List<Pet>>

    @Query("SELECT * FROM pets WHERE id = :id")
    fun getPetById(id: Int): Flow<Pet?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: Pet)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPets(pets: List<Pet>)

    @Update
    suspend fun updatePet(pet: Pet)

    @Delete
    suspend fun deletePet(pet: Pet)

    @Query("UPDATE pets SET isFavorite = :isFavorite WHERE id = :petId")
    suspend fun updateFavoriteStatus(petId: Int, isFavorite: Boolean)
}
