package com.example.appdopt.features.adoption.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdopt.core.data.repository.AdoptionRepository
import com.example.appdopt.core.model.AdoptionRequest
import com.example.appdopt.core.model.RequestStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdoptionFormUiState(
    val phone: String = "",
    val reason: String = "",
    val housing: String = "",
    val space: String = "",
    val time: String = "",
    val experience: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AdoptionFormViewModel @Inject constructor(
    private val adoptionRepository: AdoptionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdoptionFormUiState())
    val uiState: StateFlow<AdoptionFormUiState> = _uiState.asStateFlow()

    fun updateForm(
        phone: String = _uiState.value.phone,
        reason: String = _uiState.value.reason,
        housing: String = _uiState.value.housing,
        space: String = _uiState.value.space,
        time: String = _uiState.value.time,
        experience: String = _uiState.value.experience
    ) {
        _uiState.update { 
            it.copy(phone = phone, reason = reason, housing = housing, space = space, time = time, experience = experience)
        }
    }

    fun submitRequest(petId: Int, petName: String, applicantName: String) {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val request = AdoptionRequest(
                    petId = petId,
                    petName = petName,
                    applicantName = applicantName,
                    applicantPhone = state.phone,
                    reason = state.reason,
                    housingType = state.housing,
                    availableSpace = state.space,
                    availableTime = state.time,
                    previousExperience = state.experience,
                    status = RequestStatus.PENDING
                )
                adoptionRepository.insertRequest(request)
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }
}
