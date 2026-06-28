package com.example.appdopt.core.data.repository

import com.example.appdopt.core.data.local.PetDao
import com.example.appdopt.core.model.Pet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflinePetRepository @Inject constructor(
    private val petDao: PetDao
) : PetRepository {
    override fun getAllPetsStream(): Flow<List<Pet>> = 
        petDao.getAllPets().flowOn(Dispatchers.IO)

    override fun getPetStream(id: Int): Flow<Pet?> = 
        petDao.getPetById(id).flowOn(Dispatchers.IO)

    override suspend fun insertPet(pet: Pet) = withContext(Dispatchers.IO) {
        petDao.insertPet(pet)
    }

    override suspend fun deletePet(pet: Pet) = withContext(Dispatchers.IO) {
        petDao.deletePet(pet)
    }

    override suspend fun updatePet(pet: Pet) = withContext(Dispatchers.IO) {
        petDao.updatePet(pet)
    }

    override suspend fun toggleFavorite(petId: Int, currentFavoriteState: Boolean) = withContext(Dispatchers.IO) {
        petDao.updateFavoriteStatus(petId, !currentFavoriteState)
    }
}
