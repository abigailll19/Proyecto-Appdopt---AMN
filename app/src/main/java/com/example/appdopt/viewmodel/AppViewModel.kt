package com.example.appdopt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdopt.data.DataSource
import com.example.appdopt.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

import kotlinx.coroutines.flow.*

class AppViewModel : ViewModel() {
    private val _pets = MutableStateFlow(DataSource.pets)
    
    private val _currentUser = MutableStateFlow<User?>(User())
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

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
    val requests: StateFlow<List<AdoptionRequest>> = _requests.asStateFlow()

    private val _settings = MutableStateFlow(AppSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    // Filtros de búsqueda
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedSpecies = MutableStateFlow<Species?>(null)
    val selectedSpecies = _selectedSpecies.asStateFlow()

    private val _selectedSize = MutableStateFlow<Size?>(null)
    val selectedSize = _selectedSize.asStateFlow()

    private val _selectedAge = MutableStateFlow<AgeGroup?>(null)
    val selectedAge = _selectedAge.asStateFlow()

    private val _selectedGender = MutableStateFlow<Gender?>(null)
    val selectedGender = _selectedGender.asStateFlow()

    // Estado reactivo de mascotas filtradas
    val filteredPets: StateFlow<List<Pet>> = combine(
        _pets, _searchQuery, _selectedSpecies, _selectedSize, _selectedAge, _selectedGender
    ) { flows ->
        val pets = flows[0] as List<Pet>
        val query = flows[1] as String
        val species = flows[2] as Species?
        val size = flows[3] as Size?
        val age = flows[4] as AgeGroup?
        val gender = flows[5] as Gender?

        pets.filter { pet ->
            (query.isEmpty() || pet.name.contains(query, ignoreCase = true)) &&
            (species == null || pet.species == species) &&
            (size == null || pet.size == size) &&
            (age == null || pet.ageGroup == age) &&
            (gender == null || pet.gender == gender)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DataSource.pets
    )


    val pets: StateFlow<List<Pet>> = _pets.asStateFlow()



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

    fun getFilteredPets(): List<Pet> {
        return filteredPets.value
    }

    // Solicitudes
    fun addRequest(
        pet: Pet,
        reason: String,
        phone: String,
        housingType: String,
        availableSpace: String,
        availableTime: String,
        previousExperience: String
    ) {
        val user = _currentUser.value ?: return
        val newRequest = AdoptionRequest(
            id = (_requests.value.maxOfOrNull { it.id } ?: 0) + 1,
            petId = pet.id,
            petName = pet.name,
            applicantName = user.name,
            applicantPhone = phone,
            reason = reason,
            housingType = housingType,
            availableSpace = availableSpace,
            availableTime = availableTime,
            previousExperience = previousExperience,
            status = RequestStatus.PENDING
        )
        _requests.update { it + newRequest }
    }

    fun updateRequest(requestId: Int, newReason: String, newPhone: String) {
        _requests.update { list ->
            list.map { if (it.id == requestId) it.copy(reason = newReason, applicantPhone = newPhone) else it }
        }
    }

    fun cancelRequest(requestId: Int) {
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
