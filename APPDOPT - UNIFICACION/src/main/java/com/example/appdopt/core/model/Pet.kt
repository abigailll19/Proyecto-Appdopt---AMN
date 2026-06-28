package com.example.appdopt.core.model

enum class Species { DOG, CAT }
enum class Gender { MALE, FEMALE }
enum class Size { SMALL, MEDIUM, LARGE }
enum class AgeGroup { PUPPY, ADULT, SENIOR }

data class Pet(
    val id: Int,
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
