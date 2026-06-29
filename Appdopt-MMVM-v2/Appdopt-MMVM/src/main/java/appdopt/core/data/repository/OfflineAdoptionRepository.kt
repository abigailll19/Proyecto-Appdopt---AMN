package com.example.appdopt.core.data.repository

import com.example.appdopt.core.data.local.AdoptionRequestDao
import com.example.appdopt.core.model.AdoptionRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OfflineAdoptionRepository @Inject constructor(
    private val adoptionRequestDao: AdoptionRequestDao
) : AdoptionRepository {
    override fun getAllRequestsStream(): Flow<List<AdoptionRequest>> = 
        adoptionRequestDao.getAllRequests()

    override suspend fun insertRequest(request: AdoptionRequest) = 
        adoptionRequestDao.insertRequest(request)

    override suspend fun updateRequest(request: AdoptionRequest) = 
        adoptionRequestDao.updateRequest(request)

    override suspend fun deleteRequest(request: AdoptionRequest) = 
        adoptionRequestDao.deleteRequest(request)

    override suspend fun deleteRequestById(id: Int) = 
        adoptionRequestDao.deleteRequestById(id)
}
