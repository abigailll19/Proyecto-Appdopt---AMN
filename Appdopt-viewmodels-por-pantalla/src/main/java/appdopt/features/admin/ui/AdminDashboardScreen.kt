package com.example.appdopt.features.admin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.appdopt.R
import com.example.appdopt.core.model.AppSettings
import com.example.appdopt.ui.theme.AppdoptTheme
import com.example.appdopt.ui.theme.BluePrimary

@Composable
fun EditShelterRoute(
    onNavigateBack: () -> Unit,
    viewModel: EditShelterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isSuccess) {
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
    }

    EditShelterScreen(
        state = uiState,
        onUpdateLocation = viewModel::updateLocation,
        onUpdateSector = viewModel::updateSector,
        onUpdateBuildingNumber = viewModel::updateBuildingNumber,
        onUpdateClick = viewModel::updateShelter,
        onNavigateBack = onNavigateBack
    )
}

@Composable
fun AdminDashboardScreen(
    onNavigateToAddPet: () -> Unit,
    onNavigateToEditShelter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Appdopt",
                style = MaterialTheme.typography.headlineLarge,
                color = BluePrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_icono_appdotme),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Buenos días, Administrador",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(32.dp))

        AdminMenuButton(
            text = "Añadir Mascota",
            icon = Icons.Default.AddCircleOutline,
            onClick = onNavigateToAddPet
        )

        Spacer(modifier = Modifier.height(24.dp))

        AdminMenuButton(
            text = "Editar Información del Refugio",
            icon = Icons.Default.Info,
            onClick = onNavigateToEditShelter
        )
    }
}

@Composable
fun AdminMenuButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFB2D8D8),
            contentColor = Color.Black
        )
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.padding(8.dp).size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardScreenPreview() {
    AppdoptTheme(settings = AppSettings()) {
        AdminDashboardScreen(onNavigateToAddPet = {}, onNavigateToEditShelter = {})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditShelterScreen(
    state: EditShelterUiState,
    onUpdateLocation: (String) -> Unit,
    onUpdateSector: (String) -> Unit,
    onUpdateBuildingNumber: (String) -> Unit,
    onUpdateClick: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Appdopt", fontWeight = FontWeight.Bold, color = BluePrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.ic_icono_appdotme),
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(
                text = "Información Refugio - Formulario",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.outlinedCardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    FormField(
                        label = "Ubicación",
                        value = state.location,
                        onValueChange = onUpdateLocation
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FormField(
                        label = "Sector",
                        value = state.sector,
                        onValueChange = onUpdateSector
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FormField(
                        label = "N° de Edificio",
                        value = state.buildingNumber,
                        onValueChange = onUpdateBuildingNumber
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onUpdateClick,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(200.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF99C2FF)),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Actualizar", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditShelterScreenPreview() {
    AppdoptTheme(settings = AppSettings()) {
        EditShelterScreen(
            state = EditShelterUiState(
                location = "Quito",
                sector = "Norte",
                buildingNumber = "123"
            ),
            onUpdateForm = { _, _, _ -> },
            onNavigateBack = {}
        )
    }
}