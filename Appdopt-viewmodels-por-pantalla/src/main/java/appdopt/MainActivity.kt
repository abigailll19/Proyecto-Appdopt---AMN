package com.example.appdopt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.appdopt.navigation.AppNavigation
import com.example.appdopt.ui.theme.AppdoptTheme
import com.example.appdopt.core.viewmodel.AppViewModel
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: AppViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()
            val windowSizeClass = calculateWindowSizeClass(this)
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(Unit) {
                viewModel.uiEvent.collectLatest { message ->
                    snackbarHostState.showSnackbar(message)
                }
            }
            
            AppdoptTheme(settings = uiState.settings) {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { _ ->
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
}
