package com.example.appdopt.core.data.repository

import com.example.appdopt.core.model.Pet
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    fun getAllPetsStream(): Flow<List<Pet>>
    fun getPetStream(id: Int): Flow<Pet?>
    suspend fun insertPet(pet: Pet)
    suspend fun deletePet(pet: Pet)
    suspend fun updatePet(pet: Pet)
    suspend fun toggleFavorite(petId: Int, currentFavoriteState: Boolean)
}
