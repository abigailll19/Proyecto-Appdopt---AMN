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
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.appdopt.ui.screens.*
import com.example.appdopt.viewmodel.AppViewModel

@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: AppViewModel,
    windowSize: WindowSizeClass
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isMediumOrExpanded = windowSize.widthSizeClass != WindowWidthSizeClass.Compact

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
                            onLoginSuccess = { isAdmin ->
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
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("edit_shelter") {
                        EditShelterScreen(
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onPetClick = { id -> navController.navigate("pet_detail/$id") }
                        )
                    }
                    composable(
                        route = "pet_detail/{petId}",
                        arguments = listOf(navArgument("petId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val petId = backStackEntry.arguments?.getInt("petId") ?: return@composable
                        PetDetailScreen(
                            viewModel = viewModel,
                            petId = petId,
                            onNavigateToForm = { id -> navController.navigate("adoption_form/$id") },
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(
                        route = "adoption_form/{petId}",
                        arguments = listOf(navArgument("petId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val petId = backStackEntry.arguments?.getInt("petId") ?: return@composable
                        AdoptionFormScreen(
                            viewModel = viewModel,
                            petId = petId,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable("favorites") {
                        FavoritesScreen(
                            viewModel = viewModel,
                            onPetClick = { id -> navController.navigate("pet_detail/$id") },
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("requests") {
                        MyRequestsScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                    composable("profile") {
                        ProfileScreen(
                            viewModel = viewModel,
                            onNavigateToSettings = { navController.navigate("settings") }
                        )
                    }
                    composable("settings") {
                        SettingsScreen(
                            viewModel = viewModel,
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
