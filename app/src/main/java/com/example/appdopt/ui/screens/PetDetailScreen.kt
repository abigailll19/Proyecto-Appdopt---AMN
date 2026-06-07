package com.example.appdopt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import com.example.appdopt.model.Species
import com.example.appdopt.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PetDetailScreen(
    viewModel: AppViewModel,
    petId: Int,
    onNavigateToForm: (Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    val petState = viewModel.pets.collectAsState()
    val pet = petState.value.find { it.id == petId } ?: return
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { onNavigateToForm(pet.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    shape = MaterialTheme.shapes.extraLarge,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "Quiero Adoptarlo", 
                        fontSize = (18 * settings.fontSizeMultiplier).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header Image Box with Back Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            ) {
                // Background icon
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = null,
                    modifier = Modifier
                        .size(150.dp)
                        .align(Alignment.Center),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )

                // Top Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.8f),
                        onClick = onNavigateBack
                    ) {
                        Icon(
                            Icons.Default.ArrowBack, 
                            contentDescription = "Atrás",
                            modifier = Modifier.padding(12.dp),
                            tint = Color.Black
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.8f),
                        onClick = { viewModel.toggleFavorite(pet.id) }
                    ) {
                        Icon(
                            imageVector = if (pet.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            modifier = Modifier.padding(12.dp),
                            tint = if (pet.isFavorite) Color.Red else Color.Gray
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-30).dp)
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = pet.name,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = (32 * settings.fontSizeMultiplier).sp
                        )
                        Text(
                            text = "${pet.breed} • ${if(pet.species == Species.DOG) "Perro" else "Gato"}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.Gray,
                            fontSize = (16 * settings.fontSizeMultiplier).sp
                        )
                    }
                    
                    Surface(
                        color = if(pet.gender.name == "FEMALE") Color(0xFFFCE4EC) else Color(0xFFE3F2FD),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(
                            imageVector = if(pet.gender.name == "FEMALE") Icons.Default.Female else Icons.Default.Male,
                            contentDescription = null,
                            modifier = Modifier.padding(8.dp),
                            tint = if(pet.gender.name == "FEMALE") Color(0xFFD81B60) else Color(0xFF1976D2)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Info Cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InfoCard(label = "Edad", value = "${pet.age} años", modifier = Modifier.weight(weight = 1f))
                    InfoCard(label = "Peso", value = "${pet.weight} kg", modifier = Modifier.weight(weight = 1f))
                    InfoCard(label = "Sexo", value = if(pet.gender.name == "MALE") "Macho" else "Hembra", modifier = Modifier.weight(weight = 1f))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Historia",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = (20 * settings.fontSizeMultiplier).sp
                )
                Text(
                    text = pet.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    fontSize = (16 * settings.fontSizeMultiplier).sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Personalidad",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                FlowRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    pet.personality.forEach { trait ->
                        AssistChip(
                            onClick = {},
                            label = { Text(trait) },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                labelColor = MaterialTheme.colorScheme.primary
                            ),
                            border = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(label: String, value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = Color(0xFFF5F5F5)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
            Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }
    }
}
