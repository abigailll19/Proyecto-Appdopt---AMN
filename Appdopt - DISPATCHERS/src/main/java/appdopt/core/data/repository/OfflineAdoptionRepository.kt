package com.example.appdopt.core.data.repository

import com.example.appdopt.core.data.local.AdoptionRequestDao
import com.example.appdopt.core.model.AdoptionRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OfflineAdoptionRepository @Inject constructor(
    private val adoptionRequestDao: AdoptionRequestDao
) : AdoptionRepository {
    override fun getAllRequestsStream(): Flow<List<AdoptionRequest>> = 
        adoptionRequestDao.getAllRequests().flowOn(Dispatchers.IO)

    override suspend fun insertRequest(request: AdoptionRequest) = withContext(Dispatchers.IO) {
        adoptionRequestDao.insertRequest(request)
    }

    override suspend fun updateRequest(request: AdoptionRequest) = withContext(Dispatchers.IO) {
        adoptionRequestDao.updateRequest(request)
    }

    override suspend fun deleteRequest(request: AdoptionRequest) = withContext(Dispatchers.IO) {
        adoptionRequestDao.deleteRequest(request)
    }

    override suspend fun deleteRequestById(id: Int) = withContext(Dispatchers.IO) {
        adoptionRequestDao.deleteRequestById(id)
    }
}
