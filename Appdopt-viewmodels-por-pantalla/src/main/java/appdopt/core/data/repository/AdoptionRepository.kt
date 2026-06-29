package com.example.appdopt.core.data.repository

import com.example.appdopt.core.model.AdoptionRequest
import kotlinx.coroutines.flow.Flow

interface AdoptionRepository {
    fun getAllRequestsStream(): Flow<List<AdoptionRequest>>
    suspend fun insertRequest(request: AdoptionRequest)
    suspend fun updateRequest(request: AdoptionRequest)
    suspend fun deleteRequest(request: AdoptionRequest)
    suspend fun deleteRequestById(id: Int)
    suspend fun refresh() // Sincronización con SSOT
}
