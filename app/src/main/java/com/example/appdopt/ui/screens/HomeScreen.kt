package com.example.appdopt.ui.screens

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
import com.example.appdopt.model.Pet
import com.example.appdopt.model.Species
import com.example.appdopt.ui.theme.CardBackground
import com.example.appdopt.ui.theme.NavigationSelected
import com.example.appdopt.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: AppViewModel,
    onPetClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedSpecies by viewModel.selectedSpecies.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val user by viewModel.currentUser.collectAsState()
    val pets by viewModel.filteredPets.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Custom Header as in references
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

                // Search Bar fully rounded
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
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

        // Species Filter (LazyRow)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedSpecies == null,
                    onClick = { viewModel.updateSpecies(null) },
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
                    onClick = { viewModel.updateSpecies(species) },
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
                    onFavoriteClick = { viewModel.toggleFavorite(pet.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onPetClick(pet.id) }
                )
            }
        }
    }
}

@Composable
fun PetCard(
    pet: Pet,
    fontSizeMultiplier: Float,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        )
    ) {
        Box {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Pets,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.LightGray.copy(alpha = 0.5f)
                    )
                }

                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = pet.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = (16 * fontSizeMultiplier).sp
                    )
                    Text(
                        text = "${if(pet.species == Species.CAT) "Gato" else "Perro"} • ${pet.breed}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        fontSize = (12 * fontSizeMultiplier).sp
                    )
                }
            }
            
            // Favorite Button
            Surface(
                shape = CircleShape,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(32.dp),
                shadowElevation = 2.dp
            ) {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        imageVector = if (pet.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (pet.isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
