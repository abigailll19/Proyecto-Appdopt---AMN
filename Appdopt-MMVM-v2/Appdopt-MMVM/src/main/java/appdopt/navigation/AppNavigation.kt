package com.example.appdopt.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.appdopt.features.admin.ui.*
import com.example.appdopt.features.auth.ui.*
import com.example.appdopt.features.pets.ui.*
import com.example.appdopt.features.adoption.ui.*
import com.example.appdopt.features.settings.ui.*
import com.example.appdopt.core.viewmodel.AppViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: AppViewModel,
    windowSize: WindowSizeClass
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isMediumOrExpanded = windowSize.widthSizeClass != WindowWidthSizeClass.Compact
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            if (!isMediumOrExpanded && shouldShowBottomBar(currentDestination?.route)) {
                BottomNavigationBar(navController)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isMediumOrExpanded && shouldShowBottomBar(currentDestination?.route)) {
                NavigationRailComponent(navController)
            }
            
            Box(modifier = Modifier.fillMaxSize()) {
                NavHost(
                    navController = navController,
                    startDestination = "login"
                ) {
                    composable("login") {
                        LoginScreen(
                            state = uiState.loginUiState,
                            onUpdateForm = { email, pass -> viewModel.updateLoginForm(email, pass) },
                            onLoginSuccess = { isAdmin: Boolean ->
                                if (isAdmin) {
                                    navController.navigate("admin_dashboard") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            },
                            onRegisterClick = { /* Navegar a registro if exists */ }
                        )
                    }
                    composable("admin_dashboard") {
                        AdminDashboardScreen(
                            onNavigateToAddPet = { navController.navigate("add_pet") },
                            onNavigateToEditShelter = { navController.navigate("edit_shelter") }
                        )
                    }
                    composable("add_pet") {
                        AddPetScreen(
                            state = uiState.addPetUiState,
                            onUpdateForm = { name, age, size -> viewModel.updateAddPetForm(name, age, size) },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("edit_shelter") {
                        EditShelterScreen(
                            state = uiState.editShelterUiState,
                            onUpdateForm = { loc, sec, bld -> viewModel.updateEditShelterForm(loc, sec, bld) },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("home") {
                        HomeScreen(
                            searchQuery = uiState.searchQuery,
                            selectedSpecies = uiState.selectedSpecies,
                            settings = uiState.settings,
                            user = uiState.currentUser,
                            pets = uiState.filteredPets,
                            onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                            onSpeciesSelect = { viewModel.updateSpecies(it) },
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onPetClick = { id -> navController.navigate("pet_detail/$id") }
                        )
                    }
                    composable(
                        route = "pet_detail/{petId}",
                        arguments = listOf(navArgument("petId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val petId = backStackEntry.arguments?.getInt("petId") ?: return@composable
                        val pet = uiState.pets.find { it.id == petId } ?: return@composable

                        PetDetailScreen(
                            pet = pet,
                            fontSizeMultiplier = uiState.settings.fontSizeMultiplier,
                            onToggleFavorite = { viewModel.toggleFavorite(it) },
                            onNavigateToForm = { id -> navController.navigate("adoption_form/$id") },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "adoption_form/{petId}",
                        arguments = listOf(navArgument("petId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val petId = backStackEntry.arguments?.getInt("petId") ?: return@composable
                        val pet = uiState.pets.find { it.id == petId } ?: return@composable

                        AdoptionFormScreen(
                            pet = pet,
                            formState = uiState.adoptionFormState,
                            onUpdateForm = { viewModel.updateAdoptionForm(it.phone, it.reason, it.housing, it.space, it.time, it.experience) },
                            onSendRequest = { viewModel.addAdoptionRequest(petId) },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("favorites") {
                        val favoritePets = uiState.pets.filter { it.isFavorite }
                        
                        FavoritesScreen(
                            favoritePets = favoritePets,
                            fontSizeMultiplier = uiState.settings.fontSizeMultiplier,
                            onPetClick = { id -> navController.navigate("pet_detail/$id") },
                            onFavoriteClick = { viewModel.toggleFavorite(it) },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("requests") {
                        MyRequestsScreen(
                            requests = uiState.adoptionRequests,
                            settings = uiState.settings,
                            editingRequestId = uiState.editingRequestId,
                            editedPhone = uiState.editedPhone,
                            editedReason = uiState.editedReason,
                            onStartEditing = { viewModel.startEditingRequest(it) },
                            onUpdateEditedPhone = { viewModel.updateEditedPhone(it) },
                            onUpdateEditedReason = { viewModel.updateEditedReason(it) },
                            onSaveEdit = { viewModel.saveEditedRequest() },
                            onCancelEdit = { viewModel.cancelEditing() },
                            onCancelRequest = { id -> viewModel.cancelAdoptionRequest(id) },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("profile") {
                        ProfileScreen(
                            user = uiState.currentUser,
                            settings = uiState.settings,
                            onNavigateToSettings = { navController.navigate("settings") },
                            onEditProfile = { /* TODO: Implement edit profile */ }
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            settings = uiState.settings,
                            onUpdateDarkMode = { viewModel.updateDarkMode(it) },
                            onUpdateHighContrast = { viewModel.updateHighContrast(it) },
                            onUpdateFontSize = { viewModel.updateFontSize(it) },
                            onUpdateColorBlindMode = { viewModel.updateColorBlindMode(it) },
                            onUpdateLanguage = { viewModel.updateLanguage(it) },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Inicio") },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") { launchSingleTop = true } }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            label = { Text("Favoritos") },
            selected = currentRoute == "favorites",
            onClick = { navController.navigate("favorites") { launchSingleTop = true } }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, contentDescription = null) },
            label = { Text("Solicitudes") },
            selected = currentRoute == "requests",
            onClick = { navController.navigate("requests") { launchSingleTop = true } }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Perfil") },
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") { launchSingleTop = true } }
        )
    }
}

@Composable
fun NavigationRailComponent(navController: NavHostController) {
    NavigationRail {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationRailItem(
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Inicio") },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") { launchSingleTop = true } }
        )
        NavigationRailItem(
            icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
            label = { Text("Favoritos") },
            selected = currentRoute == "favorites",
            onClick = { navController.navigate("favorites") { launchSingleTop = true } }
        )
        NavigationRailItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, contentDescription = null) },
            label = { Text("Solicitudes") },
            selected = currentRoute == "requests",
            onClick = { navController.navigate("requests") { launchSingleTop = true } }
        )
        NavigationRailItem(
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Perfil") },
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") { launchSingleTop = true } }
        )
    }
}

fun shouldShowBottomBar(route: String?): Boolean {
    return route in listOf("home", "favorites", "requests", "profile")
}
