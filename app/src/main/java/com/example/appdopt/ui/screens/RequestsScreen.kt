package com.example.appdopt.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appdopt.model.AdoptionRequest
import com.example.appdopt.model.RequestStatus
import com.example.appdopt.ui.theme.*
import com.example.appdopt.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val requests by viewModel.requests.collectAsState()
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Solicitudes", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (requests.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tienes solicitudes aún.", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(requests) { request ->
                    RequestItem(
                        request = request,
                        fontSizeMultiplier = settings.fontSizeMultiplier,
                        onCancel = { viewModel.cancelRequest(request.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RequestItem(
    request: AdoptionRequest,
    fontSizeMultiplier: Float,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Solicitud para ${request.petName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = (16 * fontSizeMultiplier).sp
                )
                
                // Status Badge
                val (bgColor, textColor, text) = when(request.status) {
                    RequestStatus.PENDING -> Triple(StatusPendingBg, StatusPendingText, "Pendiente")
                    RequestStatus.APPROVED -> Triple(StatusApprovedBg, StatusApprovedText, "Aprobada")
                    RequestStatus.REJECTED -> Triple(Color(0xFFFFCDD2), Color(0xFFB71C1C), "Rechazada")
                }
                
                Surface(
                    color = bgColor,
                    shape = CircleShape,
                    border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = text,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = (12 * fontSizeMultiplier).sp,
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Vivienda: ${request.housingType}",
                style = MaterialTheme.typography.bodySmall,
                fontSize = (12 * fontSizeMultiplier).sp
            )
            Text(
                text = "Espacio: ${request.availableSpace}",
                style = MaterialTheme.typography.bodySmall,
                fontSize = (12 * fontSizeMultiplier).sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (request.status == RequestStatus.PENDING) {
                    TextButton(onClick = onCancel) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = Color(0xFFC62828), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Cancelar", color = Color(0xFFC62828))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { /* Edit logic */ }) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = EditButtonColor, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Editar", color = EditButtonColor)
                    }
                }
            }
        }
    }
}
