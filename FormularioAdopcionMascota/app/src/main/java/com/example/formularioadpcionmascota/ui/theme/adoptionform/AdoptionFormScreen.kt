package com.example.formularioadpcionmascota.ui.theme.adoptionform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.text.KeyboardOptions
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdoptionFormScreen(
    navController: NavController,
    viewModel: AdoptionFormViewModel = viewModel()
) {
    var showSuccessDialog by rememberSaveable { mutableStateOf(false) }

    val housingOptions = remember { listOf("Casa", "Apartamento", "Finca", "Otro") }
    val locationOptions = remember { listOf("Quito", "Guayaquil", "Cuenca", "Otra") }

    var housingExpanded by remember { mutableStateOf(false) }
    var locationExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Appdopt 🐾") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(Modifier.height(16.dp))

            Text(
                text = "Formulario de adopción",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Adoptando a: Luna",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(Modifier.height(20.dp))

            FormTextField(
                label = "Nombre completo",
                value = viewModel.form.fullName,
                onValueChange = viewModel::onNameChange
            )
            FormTextField(
                label = "Correo electrónico",
                value = viewModel.form.email,
                onValueChange = viewModel::onEmailChange,
                keyboardType = KeyboardType.Email
            )
            FormTextField(
                label = "Teléfono",
                value = viewModel.form.phone,
                onValueChange = viewModel::onPhoneChange,
                keyboardType = KeyboardType.Phone
            )
            FormTextField(
                label = "Dirección",
                value = viewModel.form.address,
                onValueChange = viewModel::onAddressChange
            )

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {

                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { housingExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(viewModel.form.housingType.ifBlank { "Tipo de casa" }, maxLines = 1)
                        Icon(Icons.Default.ArrowDropDown, null)
                    }
                    DropdownMenu(
                        expanded = housingExpanded,
                        onDismissRequest = { housingExpanded = false }
                    ) {
                        housingOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.onHousingChange(option)
                                    housingExpanded = false
                                }
                            )
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    OutlinedButton(
                        onClick = { locationExpanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(viewModel.form.location.ifBlank { "Ubicación" }, maxLines = 1)
                        Icon(Icons.Default.ArrowDropDown, null)
                    }
                    DropdownMenu(
                        expanded = locationExpanded,
                        onDismissRequest = { locationExpanded = false }
                    ) {
                        locationOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    viewModel.onLocationChange(option)
                                    locationExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    if (viewModel.submitForm()) {
                        showSuccessDialog = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar solicitud")
            }

            Spacer(Modifier.height(32.dp))
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("¡Solicitud enviada! 🐾") },
                text = { Text("Nos pondremos en contacto contigo pronto.") },
                confirmButton = {
                    TextButton(onClick = {
                        showSuccessDialog = false
                        navController.popBackStack()
                    }) {
                        Text("Volver al inicio")
                    }
                }
            )
        }
    }
}

@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        singleLine = true
    )
}