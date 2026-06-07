package com.example.appdopt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.appdopt.navigation.AppNavigation
import com.example.appdopt.ui.theme.AppdoptTheme
import com.example.appdopt.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AppViewModel = viewModel()
            val settings by viewModel.settings.collectAsState()
            val windowSizeClass = calculateWindowSizeClass(this)
            
            AppdoptTheme(settings = settings) {
                val navController = rememberNavController()
                AppNavigation(
                    navController = navController, 
                    viewModel = viewModel,
                    windowSize = windowSizeClass
                )
            }
        }
    }
}
