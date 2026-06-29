package com.example.appdopt.features.admin.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun AddPetRoute(
    onNavigateBack: () -> Unit,
    viewModel: AddPetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.isSuccess) {
        LaunchedEffect(Unit) {
            onNavigateBack()
        }
    }

    AddPetScreen(
        state = uiState,
        onUpdateName = viewModel::updateName,
        onUpdateAge = viewModel::updateAge,
        onUpdateSize = viewModel::updateSize,
        onAddClick = viewModel::addPet,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetScreen(
    state: AddPetUiState,
    onUpdateName: (String) -> Unit,
    onUpdateAge: (String) -> Unit,
    onUpdateSize: (String) -> Unit,
    onAddClick: () -> Unit,
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
                text = "Añadir Mascota - Formulario",
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
                        label = "Nombre", 
                        value = state.name, 
                        onValueChange = onUpdateName
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FormField(
                        label = "Edad", 
                        value = state.age, 
                        onValueChange = onUpdateAge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    FormField(
                        label = "Tamaño", 
                        value = state.size, 
                        onValueChange = onUpdateSize
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onAddClick,
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
                    Text("Añadir", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FormField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ingrese texto aquí...", color = Color.Gray, fontSize = 14.sp) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFB2D8D8).copy(alpha = 0.5f),
                focusedContainerColor = Color(0xFFB2D8D8).copy(alpha = 0.5f),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddPetScreenPreview() {
    AppdoptTheme(settings = AppSettings()) {
        AddPetScreen(
            state = AddPetUiState(name = "Fido", age = "2 años", size = "Mediano"),
            onUpdateName = {},
            onUpdateAge = {},
            onUpdateSize = {},
            onAddClick = {},
            onNavigateBack = {}
        )
    }
}
