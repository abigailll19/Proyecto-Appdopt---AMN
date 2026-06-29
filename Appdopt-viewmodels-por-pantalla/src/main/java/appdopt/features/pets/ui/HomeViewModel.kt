package com.example.appdopt.features.pets.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appdopt.core.data.repository.PetRepository
import com.example.appdopt.core.data.repository.UserPreferencesRepository
import com.example.appdopt.core.model.AppSettings
import com.example.appdopt.core.model.Pet
import com.example.appdopt.core.model.Species
import com.example.appdopt.core.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val searchQuery: String = "",
    val selectedSpecies: Species? = null,
    val pets: List<Pet> = emptyList(),
    val user: User? = null,
    val settings: AppSettings = AppSettings(),
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val petRepository: PetRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedSpecies = MutableStateFlow<Species?>(null)
    
    // SSOT from repositories
    private val _pets = petRepository.getAllPetsStream()
    private val _settings = userPreferencesRepository.userPreferencesFlow

    val uiState: StateFlow<HomeUiState> = combine(
        _searchQuery,
        _selectedSpecies,
        _pets,
        _settings
    ) { query, species, pets, settings ->
        val filteredPets = pets.filter { pet ->
            (query.isEmpty() || pet.name.contains(query, ignoreCase = true)) &&
            (species == null || pet.species == species)
        }
        HomeUiState(
            searchQuery = query,
            selectedSpecies = species,
            pets = filteredPets,
            settings = settings,
            user = User(name = "Danna") // Mock or get from a UserSession if available
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState(isLoading = true)
    )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSpecies(species: Species?) {
        _selectedSpecies.value = species
    }

    fun toggleFavorite(petId: Int) {
        viewModelScope.launch {
            val pet = uiState.value.pets.find { it.id == petId }
            pet?.let {
                petRepository.toggleFavorite(it.id, it.isFavorite)
            }
        }
    }
}
