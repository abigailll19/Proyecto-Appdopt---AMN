package com.example.appdopt.core.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
enum class Species { DOG, CAT }

@Serializable
enum class Gender { MALE, FEMALE }

@Serializable
enum class Size { SMALL, MEDIUM, LARGE }

@Serializable
enum class AgeGroup { PUPPY, ADULT, SENIOR }

@Entity(tableName = "pets")
@Serializable
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val species: Species,
    val breed: String,
    val age: Int,
    val ageGroup: AgeGroup,
    val gender: Gender,
    val size: Size,
    val weight: Double,
    val description: String,
    val personality: List<String>,
    val health: String,
    val isFavorite: Boolean = false
)
