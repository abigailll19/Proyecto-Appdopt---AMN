package com.example.appdopt.features.auth.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccess: Boolean = false,
    val isAdmin: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    // Aquí inyectarías tu AuthRepository si existiera, 
    // por ahora simularemos la lógica o usaremos lo que tengas disponible.
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun login() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        if (email.isBlank() || password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Por favor, completa todos los campos") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            
            // Simulación de delay de red
            kotlinx.coroutines.delay(1500)

            // Lógica de negocio simple (sin capa de dominio)
            if (email == "admin@appdopt.com" && password == "admin123") {
                _uiState.update { 
                    it.copy(isLoading = false, isLoginSuccess = true, isAdmin = true) 
                }
            } else if (email == "user@appdopt.com" && password == "user123") {
                _uiState.update { 
                    it.copy(isLoading = false, isLoginSuccess = true, isAdmin = false) 
                }
            } else {
                _uiState.update { 
                    it.copy(isLoading = false, errorMessage = "Credenciales incorrectas") 
                }
            }
        }
    }

    fun resetLoginStatus() {
        _uiState.update { it.copy(isLoginSuccess = false) }
    }
}
