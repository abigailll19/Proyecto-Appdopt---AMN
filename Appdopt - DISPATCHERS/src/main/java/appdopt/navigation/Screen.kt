package com.example.appdopt.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object PetDetail : Screen("petDetail/{petId}") {
        fun createRoute(petId: Int) = "petDetail/$petId"
    }
    object Favorites : Screen("favorites")
    object AdoptionForm : Screen("adoptionForm/{petId}") {
        fun createRoute(petId: Int) = "adoptionForm/$petId"
    }
    object MyRequests : Screen("myRequests")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}
