package com.example.appdopt.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdopt.core.data.repository.AdoptionRepository
import com.example.appdopt.core.data.repository.PetRepository
import com.example.appdopt.core.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdoptionFormUiState(
    val phone: String = "",
    val reason: String = "",
    val housing: String = "",
    val space: String = "",
    val time: String = "",
    val experience: String = ""
)

data class AddPetUiState(
    val name: String = "",
    val age: String = "",
    val size: String = ""
)

data class LoginUiState(
    val email: String = "",
    val password: String = ""
)

data class EditShelterUiState(
    val location: String = "",
    val sector: String = "",
    val buildingNumber: String = ""
)

data class AppUiState(
    val pets: List<Pet> = emptyList(),
    val filteredPets: List<Pet> = emptyList(),
    val currentUser: User? = null,
    val adoptionRequests: List<AdoptionRequest> = emptyList(),
    val settings: AppSettings = AppSettings(),
    val searchQuery: String = "",
    val selectedSpecies: Species? = null,
    val selectedSize: Size? = null,
    val selectedAge: AgeGroup? = null,
    val selectedGender: Gender? = null,
    val adoptionFormState: AdoptionFormUiState = AdoptionFormUiState(),
    val addPetUiState: AddPetUiState = AddPetUiState(),
    val loginUiState: LoginUiState = LoginUiState(),
    val editShelterUiState: EditShelterUiState = EditShelterUiState(),
    val editingRequestId: Int? = null,
    val editedReason: String = "",
    val editedPhone: String = ""
)

@HiltViewModel
class AppViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val adoptionRepository: AdoptionRepository,
    private val userPreferencesRepository: com.example.appdopt.core.data.repository.UserPreferencesRepository
) : ViewModel() {

    init {
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch {
            try {
                petRepository.refresh()
                adoptionRepository.refresh()
            } catch (e: Exception) {
                _uiEvent.emit("Error al sincronizar datos: ${e.message}")
            }
        }
    }

    // --- EVENTOS DE UN SOLO USO (SharedFlow) ---
    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    // --- FLUJOS REACTIVOS DESDE REPOSITORIOS (SSOT) ---
    private val _pets = petRepository.getAllPetsStream()
        .flowOn(Dispatchers.IO)
    
    private val _requests = adoptionRepository.getAllRequestsStream()
        .flowOn(Dispatchers.IO)

    // --- PERSISTENCIA DE AJUSTES (DataStore) ---
    private val _settings = userPreferencesRepository.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppSettings()
        )

    // --- ESTADOS DE UI (INPUTS TEMPORALES) ---
    private val _currentUser = MutableStateFlow<User?>(User())
    private val _searchQuery = MutableStateFlow("")
    private val _selectedSpecies = MutableStateFlow<Species?>(null)
    private val _selectedSize = MutableStateFlow<Size?>(null)
    private val _selectedAge = MutableStateFlow<AgeGroup?>(null)
    private val _selectedGender = MutableStateFlow<Gender?>(null)
    
    private val _adoptionFormState = MutableStateFlow(AdoptionFormUiState())
    private val _addPetUiState = MutableStateFlow(AddPetUiState())
    private val _loginUiState = MutableStateFlow(LoginUiState())
    private val _editShelterUiState = MutableStateFlow(EditShelterUiState())
    private val _editingRequestId = MutableStateFlow<Int?>(null)
    private val _editedReason = MutableStateFlow("")
    private val _editedPhone = MutableStateFlow("")

    // --- CONSTRUCCIÓN DEL ESTADO GLOBAL ---
    val uiState: StateFlow<AppUiState> = combine(
        _pets, _currentUser, _requests, _settings, _searchQuery, 
        _selectedSpecies, _selectedSize, _selectedAge, _selectedGender,
        _adoptionFormState, _addPetUiState, _loginUiState, _editShelterUiState,
        _editingRequestId, _editedReason, _editedPhone
    ) { flows ->
        val pets = flows[0] as List<Pet>
        val user = flows[1] as User?
        val requests = flows[2] as List<AdoptionRequest>
        val settings = flows[3] as AppSettings
        val query = flows[4] as String
        val species = flows[5] as Species?
        val size = flows[6] as Size?
        val age = flows[7] as AgeGroup?
        val gender = flows[8] as Gender?
        val formState = flows[9] as AdoptionFormUiState
        val addPetState = flows[10] as AddPetUiState
        val loginState = flows[11] as LoginUiState
        val editShelterState = flows[12] as EditShelterUiState
        val editingId = flows[13] as Int?
        val reason = flows[14] as String
        val phone = flows[15] as String

        val filtered = pets.filter { pet ->
            (query.isEmpty() || pet.name.contains(query, ignoreCase = true)) &&
            (species == null || pet.species == species) &&
            (size == null || pet.size == size) &&
            (age == null || pet.ageGroup == age) &&
            (gender == null || pet.gender == gender)
        }

        AppUiState(
            pets = pets,
            filteredPets = filtered,
            currentUser = user,
            adoptionRequests = requests,
            settings = settings,
            searchQuery = query,
            selectedSpecies = species,
            selectedSize = size,
            selectedAge = age,
            selectedGender = gender,
            adoptionFormState = formState,
            addPetUiState = addPetState,
            loginUiState = loginState,
            editShelterUiState = editShelterState,
            editingRequestId = editingId,
            editedReason = reason,
            editedPhone = phone
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppUiState()
    )

    // --- FUNCIONES DE ACCIÓN (DELEGAN A LA CAPA DE DATOS) ---
    
    fun toggleFavorite(pet: Pet) {
        viewModelScope.launch {
            petRepository.toggleFavorite(pet.id, pet.isFavorite)
        }
    }

    fun addAdoptionRequest(petId: Int) {
        val user = _currentUser.value ?: return
        val pet = uiState.value.pets.find { it.id == petId } ?: return
        val form = _adoptionFormState.value
        
        viewModelScope.launch {
            try {
                val newRequest = AdoptionRequest(
                    petId = pet.id,
                    petName = pet.name,
                    applicantName = user.name,
                    applicantPhone = form.phone,
                    reason = form.reason,
                    housingType = form.housing,
                    availableSpace = form.space,
                    availableTime = form.time,
                    previousExperience = form.experience,
                    status = RequestStatus.PENDING
                )
                adoptionRepository.insertRequest(newRequest)
                clearAdoptionForm()
                _uiEvent.emit("Solicitud enviada con éxito")
            } catch (e: Exception) {
                _uiEvent.emit("Error al enviar solicitud: ${e.message}")
            }
        }
    }

    fun cancelAdoptionRequest(requestId: Int) {
        viewModelScope.launch {
            try {
                adoptionRepository.deleteRequestById(requestId)
                _uiEvent.emit("Solicitud cancelada")
            } catch (e: Exception) {
                _uiEvent.emit("Error al cancelar solicitud")
            }
        }
    }

    fun saveEditedRequest() {
        val requestId = _editingRequestId.value ?: return
        val request = uiState.value.adoptionRequests.find { it.id == requestId } ?: return
        
        viewModelScope.launch {
            try {
                val updatedRequest = request.copy(
                    reason = _editedReason.value,
                    applicantPhone = _editedPhone.value
                )
                adoptionRepository.updateRequest(updatedRequest)
                cancelEditing()
                _uiEvent.emit("Solicitud actualizada")
            } catch (e: Exception) {
                _uiEvent.emit("Error al actualizar solicitud")
            }
        }
    }

    // --- ACTUALIZACIÓN DE FORMULARIOS (SOLO UI STATE) ---
    fun updateSearchQuery(query: String) { _searchQuery.value = query }
    fun updateSpecies(species: Species?) { _selectedSpecies.value = species }
    fun updateSize(size: Size?) { _selectedSize.value = size }
    fun updateAge(age: AgeGroup?) { _selectedAge.value = age }
    fun updateGender(gender: Gender?) { _selectedGender.value = gender }

    fun updateAdoptionForm(
        phone: String = _adoptionFormState.value.phone,
        reason: String = _adoptionFormState.value.reason,
        housing: String = _adoptionFormState.value.housing,
        space: String = _adoptionFormState.value.space,
        time: String = _adoptionFormState.value.time,
        experience: String = _adoptionFormState.value.experience
    ) {
        _adoptionFormState.update { 
            it.copy(phone = phone, reason = reason, housing = housing, space = space, time = time, experience = experience)
        }
    }

    fun clearAdoptionForm() { _adoptionFormState.value = AdoptionFormUiState() }

    fun updateLoginForm(email: String = _loginUiState.value.email, password: String = _loginUiState.value.password) {
        _loginUiState.update { it.copy(email = email, password = password) }
    }

    fun updateAddPetForm(name: String = _addPetUiState.value.name, age: String = _addPetUiState.value.age, size: String = _addPetUiState.value.size) {
        _addPetUiState.update { it.copy(name = name, age = age, size = size) }
    }

    fun updateEditShelterForm(location: String = _editShelterUiState.value.location, sector: String = _editShelterUiState.value.sector, building: String = _editShelterUiState.value.buildingNumber) {
        _editShelterUiState.update { it.copy(location = location, sector = sector, buildingNumber = building) }
    }

    fun startEditingRequest(request: AdoptionRequest) {
        _editingRequestId.value = request.id
        _editedReason.value = request.reason
        _editedPhone.value = request.applicantPhone
    }

    fun updateEditedReason(reason: String) { _editedReason.value = reason }
    fun updateEditedPhone(phone: String) { _editedPhone.value = phone }

    fun cancelEditing() {
        _editingRequestId.value = null
        _editedReason.value = ""
        _editedPhone.value = ""
    }

    // --- ACCESIBILIDAD Y PERFIL ---
    fun updateDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateDarkMode(isDark)
        }
    }

    fun updateHighContrast(isHigh: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateHighContrast(isHigh)
        }
    }

    fun updateFontSize(multiplier: Float) {
        viewModelScope.launch {
            userPreferencesRepository.updateFontSize(multiplier)
        }
    }

    fun updateColorBlindMode(mode: ColorBlindMode) {
        viewModelScope.launch {
            userPreferencesRepository.updateColorBlindMode(mode)
        }
    }

    fun updateLanguage(lang: AppLanguage) {
        viewModelScope.launch {
            userPreferencesRepository.updateLanguage(lang)
        }
    }
    
    fun updateProfile(name: String, email: String, phone: String) {
        _currentUser.update { it?.copy(name = name, email = email, phone = phone) }
    }
}
