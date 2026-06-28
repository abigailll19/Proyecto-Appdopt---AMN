package com.example.appdopt.features.pets.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as lazyItems
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appdopt.components.PetCard
import com.example.appdopt.core.model.Pet
import com.example.appdopt.core.model.Species
import com.example.appdopt.core.model.User
import com.example.appdopt.core.model.AppSettings
import com.example.appdopt.ui.theme.NavigationSelected

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    searchQuery: String,
    selectedSpecies: Species?,
    settings: AppSettings,
    user: User?,
    pets: List<Pet>,
    onSearchQueryChange: (String) -> Unit,
    onSpeciesSelect: (Species?) -> Unit,
    onToggleFavorite: (Int) -> Unit,
    onPetClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Custom Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Hola, ${user?.name ?: "Danna"}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            fontSize = (24 * settings.fontSizeMultiplier).sp
                        )
                        Text(
                            text = "Encuentra a tu nuevo mejor amigo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            fontSize = (14 * settings.fontSizeMultiplier).sp
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(45.dp),
                        shadowElevation = 2.dp
                    ) {
                        IconButton(onClick = { /* Notificaciones */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.Gray)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { 
                        Text("Buscar por nombre o raza...", 
                            fontSize = (16 * settings.fontSizeMultiplier).sp,
                            color = Color.Gray
                        ) 
                    },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                    shape = CircleShape,
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        unfocusedBorderColor = Color.Transparent
                    )
                )
            }
        }

        // Species Filter
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedSpecies == null,
                    onClick = { onSpeciesSelect(null) },
                    label = { Text("Todos") },
                    shape = MaterialTheme.shapes.medium,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NavigationSelected,
                        selectedLabelColor = MaterialTheme.colorScheme.primary
                    ),
                    border = null
                )
            }
            lazyItems(Species.entries) { species ->
                val label = when(species) {
                    Species.DOG -> "Perros"
                    Species.CAT -> "Gatos"
                }
                FilterChip(
                    selected = selectedSpecies == species,
                    onClick = { onSpeciesSelect(species) },
                    label = { Text(label) },
                    shape = MaterialTheme.shapes.medium,
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedSpecies == species,
                        borderColor = Color.LightGray
                    )
                )
            }
        }

        Text(
            text = "Mascotas disponibles",
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // Pet Grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.weight(1f)
        ) {
            gridItems(pets, key = { it.id }) { pet ->
                PetCard(
                    pet = pet,
                    fontSizeMultiplier = settings.fontSizeMultiplier,
                    onFavoriteClick = { onToggleFavorite(pet.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPetClick(pet.id) }
                )
            }
        }
    }
}
