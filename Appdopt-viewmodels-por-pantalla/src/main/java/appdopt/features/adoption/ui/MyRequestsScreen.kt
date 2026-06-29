package com.example.appdopt.features.adoption.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appdopt.core.model.AdoptionRequest
import com.example.appdopt.core.model.RequestStatus
import com.example.appdopt.core.model.AppSettings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyRequestsScreen(
    requests: List<AdoptionRequest>,
    settings: AppSettings,
    editingRequestId: Int?,
    editedPhone: String,
    editedReason: String,
    onStartEditing: (AdoptionRequest) -> Unit,
    onUpdateEditedPhone: (String) -> Unit,
    onUpdateEditedReason: (String) -> Unit,
    onSaveEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onCancelRequest: (Int) -> Unit,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = { 
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Mis Solicitudes", 
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            ) 
        }
    ) { innerPadding ->
        if (requests.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.AssignmentLate, 
                        contentDescription = null, 
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "No tienes solicitudes activas", 
                        fontSize = (18 * settings.fontSizeMultiplier).sp,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(requests) { request ->
                    RequestCard(
                        request = request,
                        isEditing = editingRequestId == request.id,
                        editedPhone = editedPhone,
                        editedReason = editedReason,
                        fontSizeMultiplier = settings.fontSizeMultiplier,
                        onPhoneChange = onUpdateEditedPhone,
                        onReasonChange = onUpdateEditedReason,
                        onEdit = { onStartEditing(request) },
                        onCancelEdit = onCancelEdit,
                        onSave = onSaveEdit,
                        onDelete = { onCancelRequest(request.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RequestCard(
    request: AdoptionRequest,
    isEditing: Boolean,
    editedPhone: String,
    editedReason: String,
    fontSizeMultiplier: Float,
    onPhoneChange: (String) -> Unit,
    onReasonChange: (String) -> Unit,
    onEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Icon(
                            Icons.Default.Pets, 
                            contentDescription = null, 
                            modifier = Modifier.padding(8.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = request.petName,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = (18 * fontSizeMultiplier).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                StatusBadge(status = request.status)
            }

            HorizontalDivider(Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            
            if (isEditing) {
                OutlinedTextField(
                    value = editedPhone,
                    onValueChange = onPhoneChange,
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = editedReason,
                    onValueChange = onReasonChange,
                    label = { Text("Motivo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2
                )
                Row(
                    Modifier.fillMaxWidth().padding(top = 12.dp), 
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onCancelEdit) { Text("Cancelar") }
                    Button(onClick = onSave, shape = RoundedCornerShape(8.dp)) { Text("Guardar") }
                }
            } else {
                InfoRow(Icons.Default.Home, "Vivienda: ${request.housingType}")
                InfoRow(Icons.Default.AspectRatio, "Espacio: ${request.availableSpace}")
                InfoRow(Icons.Default.ChatBubbleOutline, request.reason)

                if (request.status == RequestStatus.PENDING) {
                    Row(
                        Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(
                            onClick = onDelete,
                            colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFFD32F2F))
                        ) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar")
                        }
                        
                        Spacer(Modifier.width(8.dp))
                        
                        Button(
                            onClick = onEdit,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF00BCD4),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("Editar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(status: RequestStatus) {
    val (bgColor, textColor, label) = when(status) {
        RequestStatus.PENDING -> Triple(Color(0xFFFFE0B2), Color(0xFFE65100), "Pendiente")
        RequestStatus.APPROVED -> Triple(Color(0xFFC8E6C9), Color(0xFF1B5E20), "Aprobada")
        RequestStatus.REJECTED -> Triple(Color(0xFFFFCDD2), Color(0xFFB71C1C), "Rechazada")
    }
    
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(
        modifier = Modifier.padding(vertical = 2.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            icon, 
            contentDescription = null, 
            modifier = Modifier.size(16.dp).padding(top = 2.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyRequestsScreenPreview() {
    MyRequestsScreen(
        requests = listOf(
            AdoptionRequest(
                id = 1,
                petId = 1,
                petName = "Luna",
                applicantName = "Danna",
                applicantPhone = "0987654321",
                reason = "Amo los gatos.",
                housingType = "Casa",
                availableSpace = "Amplio",
                availableTime = "Mucho",
                previousExperience = "Sí",
                status = RequestStatus.PENDING
            )
        ),
        settings = AppSettings(),
        editingRequestId = null,
        editedPhone = "",
        editedReason = "",
        onStartEditing = {},
        onUpdateEditedPhone = {},
        onUpdateEditedReason = {},
        onSaveEdit = {},
        onCancelEdit = {},
        onCancelRequest = {}
    )
}
