package com.example.appdopt.core.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdopt.core.data.DataSource
import com.example.appdopt.core.model.*
import kotlinx.coroutines.flow.*

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

class AppViewModel : ViewModel() {
    private val _pets = MutableStateFlow(DataSource.pets)
    private val _currentUser = MutableStateFlow<User?>(User())
    private val _requests = MutableStateFlow<List<AdoptionRequest>>(listOf(
        AdoptionRequest(
            id = 1,
            petId = 1,
            petName = "Luna",
            applicantName = "Danna",
            applicantPhone = "0987654321",
            reason = "Amo los gatos y tengo espacio en mi departamento.",
            housingType = "Departamento",
            availableSpace = "Suficiente",
            availableTime = "Mucho",
            previousExperience = "Sí",
            status = RequestStatus.PENDING
        )
    ))
    private val _settings = MutableStateFlow(AppSettings())
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
        initialValue = AppUiState(
            pets = DataSource.pets,
            filteredPets = DataSource.pets,
            currentUser = _currentUser.value,
            adoptionRequests = _requests.value,
            settings = _settings.value,
            searchQuery = _searchQuery.value,
            selectedSpecies = _selectedSpecies.value,
            selectedSize = _selectedSize.value,
            selectedAge = _selectedAge.value,
            selectedGender = _selectedGender.value,
            adoptionFormState = _adoptionFormState.value,
            addPetUiState = _addPetUiState.value,
            loginUiState = _loginUiState.value,
            editShelterUiState = _editShelterUiState.value,
            editingRequestId = _editingRequestId.value,
            editedReason = _editedReason.value,
            editedPhone = _editedPhone.value
        )
    )

    // Favoritos
    fun toggleFavorite(petId: Int) {
        _pets.update { list ->
            list.map { if (it.id == petId) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }

    // Actualizar Filtros
    fun updateSearchQuery(query: String) { _searchQuery.value = query }
    fun updateSpecies(species: Species?) { _selectedSpecies.value = species }
    fun updateSize(size: Size?) { _selectedSize.value = size }
    fun updateAge(age: AgeGroup?) { _selectedAge.value = age }
    fun updateGender(gender: Gender?) { _selectedGender.value = gender }

    // Formulario de Adopción
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

    fun clearAdoptionForm() {
        _adoptionFormState.value = AdoptionFormUiState()
    }

    // Auth
    fun updateLoginForm(email: String = _loginUiState.value.email, password: String = _loginUiState.value.password) {
        _loginUiState.update { it.copy(email = email, password = password) }
    }

    // Gestión Admin (Mascotas y Refugio)
    fun updateAddPetForm(name: String = _addPetUiState.value.name, age: String = _addPetUiState.value.age, size: String = _addPetUiState.value.size) {
        _addPetUiState.update { it.copy(name = name, age = age, size = size) }
    }

    fun updateEditShelterForm(location: String = _editShelterUiState.value.location, sector: String = _editShelterUiState.value.sector, building: String = _editShelterUiState.value.buildingNumber) {
        _editShelterUiState.update { it.copy(location = location, sector = sector, buildingNumber = building) }
    }

    // Solicitudes
    fun addAdoptionRequest(petId: Int) {
        val user = _currentUser.value ?: return
        val pet = _pets.value.find { it.id == petId } ?: return
        val form = _adoptionFormState.value
        val newRequest = AdoptionRequest(
            id = (_requests.value.maxOfOrNull { it.id } ?: 0) + 1,
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
        _requests.update { it + newRequest }
        clearAdoptionForm()
    }

    fun startEditingRequest(request: AdoptionRequest) {
        _editingRequestId.value = request.id
        _editedReason.value = request.reason
        _editedPhone.value = request.applicantPhone
    }

    fun updateEditedReason(reason: String) { _editedReason.value = reason }
    fun updateEditedPhone(phone: String) { _editedPhone.value = phone }

    fun saveEditedRequest() {
        val requestId = _editingRequestId.value ?: return
        val reason = _editedReason.value
        val phone = _editedPhone.value
        _requests.update { list ->
            list.map { if (it.id == requestId) it.copy(reason = reason, applicantPhone = phone) else it }
        }
        cancelEditing()
    }

    fun cancelEditing() {
        _editingRequestId.value = null
        _editedReason.value = ""
        _editedPhone.value = ""
    }

    fun cancelAdoptionRequest(requestId: Int) {
        _requests.update { list -> list.filter { it.id != requestId } }
    }

    // Accesibilidad
    fun updateDarkMode(isDark: Boolean) { _settings.update { it.copy(isDarkMode = isDark) } }
    fun updateHighContrast(isHigh: Boolean) { _settings.update { it.copy(isHighContrast = isHigh) } }
    fun updateFontSize(multiplier: Float) { _settings.update { it.copy(fontSizeMultiplier = multiplier) } }
    fun updateColorBlindMode(mode: ColorBlindMode) { _settings.update { it.copy(colorBlindMode = mode) } }
    fun updateLanguage(lang: AppLanguage) { _settings.update { it.copy(language = lang) } }
    
    fun updateProfile(name: String, email: String, phone: String) {
        _currentUser.update { it?.copy(name = name, email = email, phone = phone) }
    }
}
