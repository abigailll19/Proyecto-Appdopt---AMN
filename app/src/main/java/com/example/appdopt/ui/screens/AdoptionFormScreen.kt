package com.example.appdopt.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appdopt.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdoptionFormScreen(
    viewModel: AppViewModel,
    petId: Int,
    onNavigateBack: () -> Unit
) {
    val pet = viewModel.pets.collectAsState().value.find { it.id == petId } ?: return
    val settings by viewModel.settings.collectAsState()
    
    var phone by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var housing by remember { mutableStateOf("") }
    var space by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }

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
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
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
            
            // Pet Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Placeholder for Pet Image or Icon
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
                            "${pet.breed} • ${pet.gender.name}",
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

            FormTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Teléfono de contacto",
                icon = Icons.Default.Phone
            )

            FormTextField(
                value = housing,
                onValueChange = { housing = it },
                label = "Tipo de vivienda (Casa/Apto)",
                icon = Icons.Default.Home
            )

            FormTextField(
                value = space,
                onValueChange = { space = it },
                label = "Espacio disponible (m2, jardín...)",
                icon = Icons.Default.AspectRatio
            )

            FormTextField(
                value = time,
                onValueChange = { time = it },
                label = "Tiempo disponible al día",
                icon = Icons.Default.Schedule
            )

            FormTextField(
                value = experience,
                onValueChange = { experience = it },
                label = "¿Tienes experiencia previa?",
                icon = Icons.Default.History
            )

            FormTextField(
                value = reason,
                onValueChange = { reason = it },
                label = "¿Por qué quieres adoptar a ${pet.name}?",
                icon = Icons.Default.ChatBubbleOutline,
                isMultiline = true
            )

            Button(
                onClick = {
                    viewModel.addRequest(
                        pet = pet,
                        reason = reason,
                        phone = phone,
                        housingType = housing,
                        availableSpace = space,
                        availableTime = time,
                        previousExperience = experience
                    )
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = phone.isNotBlank() && reason.isNotBlank() && housing.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
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
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        minLines = if (isMultiline) 3 else 1,
        maxLines = if (isMultiline) 5 else 1
    )
}
