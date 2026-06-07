package com.example.formularioadpcionmascota

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.formularioadpcionmascota.ui.theme.adoptionform.AdoptionFormScreen
import com.example.formularioadpcionmascota.ui.theme.themes.FormularioAdpcionMascotaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FormularioAdpcionMascotaTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "adoption_form"
                ) {
                    composable("adoption_form") {
                        AdoptionFormScreen(navController = navController)
                    }
                }
            }
        }
    }
}