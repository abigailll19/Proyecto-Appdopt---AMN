package com.example.appdopt.core.data.repository

import com.example.appdopt.core.data.local.AdoptionRequestDao
import com.example.appdopt.core.model.AdoptionRequest
import io.github.jan_tennert.supabase.SupabaseClient
import io.github.jan_tennert.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SupabaseAdoptionRepository @Inject constructor(
    private val adoptionRequestDao: AdoptionRequestDao,
    private val supabaseClient: SupabaseClient
) : AdoptionRepository {

    override fun getAllRequestsStream(): Flow<List<AdoptionRequest>> = 
        adoptionRequestDao.getAllRequests().flowOn(Dispatchers.IO)

    override suspend fun refresh() = withContext(Dispatchers.IO) {
        try {
            val remoteRequests = supabaseClient.postgrest["adoption_requests"]
                .select().decodeList<AdoptionRequest>()
            // Sincronizar localmente
            remoteRequests.forEach { adoptionRequestDao.insertRequest(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun insertRequest(request: AdoptionRequest) = withContext(Dispatchers.IO) {
        try {
            val remoteRequest = supabaseClient.postgrest["adoption_requests"]
                .insert(request).decodeSingle<AdoptionRequest>()
            adoptionRequestDao.insertRequest(remoteRequest)
        } catch (e: Exception) {
            // Si falla internet, guardamos localmente (o manejamos error según política)
            adoptionRequestDao.insertRequest(request)
        }
    }

    override suspend fun updateRequest(request: AdoptionRequest) = withContext(Dispatchers.IO) {
        try {
            supabaseClient.postgrest["adoption_requests"].update(request) {
                filter { eq("id", request.id) }
            }
        } catch (e: Exception) { e.printStackTrace() }
        adoptionRequestDao.updateRequest(request)
    }

    override suspend fun deleteRequest(request: AdoptionRequest) = withContext(Dispatchers.IO) {
        try {
            supabaseClient.postgrest["adoption_requests"].delete {
                filter { eq("id", request.id) }
            }
        } catch (e: Exception) { e.printStackTrace() }
        adoptionRequestDao.deleteRequest(request)
    }

    override suspend fun deleteRequestById(id: Int) = withContext(Dispatchers.IO) {
        try {
            supabaseClient.postgrest["adoption_requests"].delete {
                filter { eq("id", id) }
            }
        } catch (e: Exception) { e.printStackTrace() }
        adoptionRequestDao.deleteRequestById(id)
    }
}
