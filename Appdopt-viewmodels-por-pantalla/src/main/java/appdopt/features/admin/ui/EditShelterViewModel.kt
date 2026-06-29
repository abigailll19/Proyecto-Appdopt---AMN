package com.example.appdopt.features.admin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditShelterUiState(
    val location: String = "",
    val sector: String = "",
    val buildingNumber: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class EditShelterViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(EditShelterUiState())
    val uiState: StateFlow<EditShelterUiState> = _uiState.asStateFlow()

    fun updateLocation(location: String) {
        _uiState.update { it.copy(location = location) }
    }

    fun updateSector(sector: String) {
        _uiState.update { it.copy(sector = sector) }
    }

    fun updateBuildingNumber(buildingNumber: String) {
        _uiState.update { it.copy(buildingNumber = buildingNumber) }
    }

    fun updateShelter() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Simulación de guardado
            kotlinx.coroutines.delay(1000)
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        }
    }
}
