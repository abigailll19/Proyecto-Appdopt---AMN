package com.example.appdopt.features.admin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdopt.core.data.repository.PetRepository
import com.example.appdopt.core.model.AgeGroup
import com.example.appdopt.core.model.Gender
import com.example.appdopt.core.model.Pet
import com.example.appdopt.core.model.Size
import com.example.appdopt.core.model.Species
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddPetUiState(
    val name: String = "",
    val age: String = "",
    val size: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AddPetViewModel @Inject constructor(
    private val petRepository: PetRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPetUiState())
    val uiState: StateFlow<AddPetUiState> = _uiState.asStateFlow()

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun updateAge(age: String) {
        _uiState.update { it.copy(age = age) }
    }

    fun updateSize(size: String) {
        _uiState.update { it.copy(size = size) }
    }

    fun addPet() {
        val state = _uiState.value
        if (state.name.isBlank() || state.age.isBlank() || state.size.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Todos los campos son obligatorios") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // Aquí iría la lógica para mapear y guardar
                // Por ahora simulamos éxito
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}
