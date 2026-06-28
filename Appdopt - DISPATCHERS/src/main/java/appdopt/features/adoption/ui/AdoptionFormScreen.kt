package com.example.appdopt.features.adoption.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appdopt.core.model.*
import com.example.appdopt.core.viewmodel.AdoptionFormUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdoptionFormScreen(
    pet: Pet,
    formState: AdoptionFormUiState,
    onUpdateForm: (AdoptionFormUiState) -> Unit,
    onSendRequest: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Solicitud de Adopción",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(60.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ) {
                        Icon(
                            Icons.Default.Pets, 
                            contentDescription = null, 
                            modifier = Modifier.padding(12.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            "Estás solicitando a ${pet.name}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${pet.breed} • ${if(pet.gender == Gender.MALE) "Macho" else "Hembra"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Text(
                "Información del Solicitante",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            FormTextField(value = formState.phone, onValueChange = { onUpdateForm(formState.copy(phone = it)) }, label = "Teléfono de contacto", icon = Icons.Default.Phone)
            FormTextField(value = formState.housing, onValueChange = { onUpdateForm(formState.copy(housing = it)) }, label = "Tipo de vivienda (Casa/Apto)", icon = Icons.Default.Home)
            FormTextField(value = formState.space, onValueChange = { onUpdateForm(formState.copy(space = it)) }, label = "Espacio disponible (m2, jardín...)", icon = Icons.Default.AspectRatio)
            FormTextField(value = formState.time, onValueChange = { onUpdateForm(formState.copy(time = it)) }, label = "Tiempo disponible al día", icon = Icons.Default.Schedule)
            FormTextField(value = formState.experience, onValueChange = { onUpdateForm(formState.copy(experience = it)) }, label = "¿Tienes experiencia previa?", icon = Icons.Default.History)
            FormTextField(value = formState.reason, onValueChange = { onUpdateForm(formState.copy(reason = it)) }, label = "¿Por qué quieres adoptar a ${pet.name}?", icon = Icons.Default.ChatBubbleOutline, isMultiline = true)

            Button(
                onClick = {
                    onSendRequest()
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = formState.phone.isNotBlank() && formState.reason.isNotBlank() && formState.housing.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Enviar Solicitud", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    isMultiline: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        minLines = if (isMultiline) 3 else 1,
        maxLines = if (isMultiline) 5 else 1
    )
}

@Preview(showBackground = true)
@Composable
fun AdoptionFormScreenPreview() {
    AdoptionFormScreen(
        pet = Pet(
            id = 1,
            name = "Luna",
            species = Species.CAT,
            breed = "Gato",
            age = 2,
            ageGroup = AgeGroup.ADULT,
            gender = Gender.FEMALE,
            size = Size.MEDIUM,
            weight = 4.5,
            description = "Una gatita muy cariñosa.",
            personality = listOf("Cariñosa", "Juguetona"),
            health = "Sana"
        ),
        formState = AdoptionFormUiState(),
        onUpdateForm = {},
        onSendRequest = {},
        onNavigateBack = {}
    )
}
