package com.example.appdopt.core.data.local

import androidx.room.*
import com.example.appdopt.core.model.AdoptionRequest
import kotlinx.coroutines.flow.Flow

@Dao
interface AdoptionRequestDao {
    @Query("SELECT * FROM adoption_requests")
    fun getAllRequests(): Flow<List<AdoptionRequest>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: AdoptionRequest)

    @Update
    suspend fun updateRequest(request: AdoptionRequest)

    @Delete
    suspend fun deleteRequest(request: AdoptionRequest)

    @Query("DELETE FROM adoption_requests WHERE id = :requestId")
    suspend fun deleteRequestById(requestId: Int)
}
