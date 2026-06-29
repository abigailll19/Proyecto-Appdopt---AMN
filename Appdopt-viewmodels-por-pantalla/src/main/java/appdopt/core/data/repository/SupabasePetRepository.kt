package com.example.appdopt.core.data.repository

import com.example.appdopt.core.data.local.PetDao
import com.example.appdopt.core.model.Pet
import io.github.jan_tennert.supabase.SupabaseClient
import io.github.jan_tennert.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SupabasePetRepository @Inject constructor(
    private val petDao: PetDao,
    private val supabaseClient: SupabaseClient
) : PetRepository {

    // SSOT: La UI siempre observa la base de datos local
    override fun getAllPetsStream(): Flow<List<Pet>> = 
        petDao.getAllPets().flowOn(Dispatchers.IO)

    override fun getPetStream(id: Int): Flow<Pet?> = 
        petDao.getPetById(id).flowOn(Dispatchers.IO)

    // MAD Skill: Sincronización remota -> local
    override suspend fun refresh() = withContext(Dispatchers.IO) {
        try {
            val remotePets = supabaseClient.postgrest["pets"].select().decodeList<Pet>()
            petDao.insertPets(remotePets)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun insertPet(pet: Pet) = withContext(Dispatchers.IO) {
        try {
            // 1. Insertar en remoto (Supabase)
            supabaseClient.postgrest["pets"].insert(pet)
            // 2. Actualizar localmente para SSOT
            petDao.insertPet(pet)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deletePet(pet: Pet) = withContext(Dispatchers.IO) {
        supabaseClient.postgrest["pets"].delete {
            filter { eq("id", pet.id) }
        }
        petDao.deletePet(pet)
    }

    override suspend fun updatePet(pet: Pet) = withContext(Dispatchers.IO) {
        supabaseClient.postgrest["pets"].update(pet) {
            filter { eq("id", pet.id) }
        }
        petDao.updatePet(pet)
    }

    override suspend fun toggleFavorite(petId: Int, currentFavoriteState: Boolean) = withContext(Dispatchers.IO) {
        // Los favoritos suelen ser locales, pero si quieres sincronizarlos:
        val newState = !currentFavoriteState
        try {
            supabaseClient.postgrest["pets"].update({
                Pet::isFavorite.name setTo newState
            }) {
                filter { eq("id", petId) }
            }
        } catch (e: Exception) { /* Omitir si falla internet */ }
        
        petDao.updateFavoriteStatus(petId, newState)
    }
}
