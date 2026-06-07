package com.example.appdopt.model

enum class RequestStatus { PENDING, APPROVED, REJECTED }

data class AdoptionRequest(
    val id: Int,
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
