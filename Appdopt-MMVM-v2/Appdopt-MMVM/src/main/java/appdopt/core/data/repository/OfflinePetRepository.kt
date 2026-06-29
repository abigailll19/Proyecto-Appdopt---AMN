package com.example.appdopt.core.data.repository

import com.example.appdopt.core.data.local.PetDao
import com.example.appdopt.core.model.Pet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflinePetRepository @Inject constructor(
    private val petDao: PetDao
) : PetRepository {
    override fun getAllPetsStream(): Flow<List<Pet>> = petDao.getAllPets()

    override fun getPetStream(id: Int): Flow<Pet?> = petDao.getPetById(id)

    override suspend fun insertPet(pet: Pet) = petDao.insertPet(pet)

    override suspend fun deletePet(pet: Pet) = petDao.deletePet(pet)

    override suspend fun updatePet(pet: Pet) = petDao.updatePet(pet)

    override suspend fun toggleFavorite(petId: Int, currentFavoriteState: Boolean) {
        petDao.updateFavoriteStatus(petId, !currentFavoriteState)
    }
}
