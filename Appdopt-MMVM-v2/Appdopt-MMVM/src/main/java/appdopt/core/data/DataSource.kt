package com.example.appdopt.core.data

import com.example.appdopt.core.model.*

object DataSource {
    val pets = listOf(
        Pet(
            id = 1,
            name = "Luna",
            species = Species.CAT,
            breed = "Siamés",
            age = 2,
            ageGroup = AgeGroup.ADULT,
            gender = Gender.FEMALE,
            size = Size.SMALL,
            weight = 4.5,
            description = "Luna es muy cariñosa y juguetona. Le encanta estar acompañada.",
            personality = listOf("Cariñosa", "Jugunota"),
            health = "Saludable",
            isFavorite = true
        ),
        Pet(
            id = 2,
            name = "Simba",
            species = Species.CAT,
            breed = "Tabby",
            age = 1,
            ageGroup = AgeGroup.PUPPY,
            gender = Gender.MALE,
            size = Size.SMALL,
            weight = 3.2,
            description = "Simba busca un hogar lleno de amor. Es muy activo.",
            personality = listOf("Activo", "Curioso"),
            health = "Vacunado",
            isFavorite = false
        ),
        Pet(
            id = 3,
            name = "Thor",
            species = Species.DOG,
            breed = "Golden Retriever",
            age = 3,
            ageGroup = AgeGroup.ADULT,
            gender = Gender.MALE,
            size = Size.LARGE,
            weight = 28.5,
            description = "Thor es un perro muy leal y protector. Ideal para familias.",
            personality = listOf("Leal", "Tranquilo"),
            health = "Excelente",
            isFavorite = false
        ),
        Pet(
            id = 4,
            name = "Nieve",
            species = Species.CAT,
            breed = "Persa",
            age = 4,
            ageGroup = AgeGroup.ADULT,
            gender = Gender.FEMALE,
            size = Size.MEDIUM,
            weight = 5.4,
            description = "Nieve es tranquila y le gusta dormir en lugares cómodos.",
            personality = listOf("Tranquila", "Elegante"),
            health = "Saludable",
            isFavorite = false
        ),
        Pet(
            id = 5,
            name = "Daisy",
            species = Species.DOG,
            breed = "Beagle",
            age = 2,
            ageGroup = AgeGroup.ADULT,
            gender = Gender.FEMALE,
            size = Size.SMALL,
            weight = 7.1,
            description = "Daisy tiene un olfato increíble y es muy curiosa.",
            personality = listOf("Curiosa", "Amigable"),
            health = "Saludable",
            isFavorite = true
        ),
        Pet(
            id = 6,
            name = "Coco",
            species = Species.DOG,
            breed = "Poodle",
            age = 1,
            ageGroup = AgeGroup.PUPPY,
            gender = Gender.MALE,
            size = Size.SMALL,
            weight = 4.8,
            description = "Coco es pequeño pero con mucha energía.",
            personality = listOf("Energético"),
            health = "Saludable",
            isFavorite = false
        )
    )
}
