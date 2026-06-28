package com.example.appdopt.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
enum class RequestStatus { PENDING, APPROVED, REJECTED }

@Entity(tableName = "adoption_requests")
@Serializable
data class AdoptionRequest(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petId: Int,
    val petName: String,
    val applicantName: String,
    val applicantPhone: String,
    val reason: String = "",
    val housingType: String,
    val availableSpace: String,
    val availableTime: String,
    val previousExperience: String,
    val status: RequestStatus = RequestStatus.PENDING
)
