package com.example.appdopt.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.example.appdopt.core.model.AppSettings
import com.example.appdopt.core.model.User
import com.example.appdopt.ui.theme.AvatarBackground
import com.example.appdopt.ui.theme.EditButtonColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: User?,
    settings: AppSettings,
    onNavigateToSettings: () -> Unit,
    onEditProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Mi Perfil", fontWeight = FontWeight.SemiBold) },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Ajustes", tint = Color.Gray)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Avatar (Mimicking the image)
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .background(AvatarBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color.Black
                )
                // Small dot decoration as in image
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 10.dp, bottom = 10.dp)
                        .size(30.dp)
                        .background(Color(0xFFFF8A65), CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = user?.name ?: "Danna",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = (24 * settings.fontSizeMultiplier).sp
            )
            
            Text(
                text = user?.email ?: "danna@example.com",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                fontSize = (16 * settings.fontSizeMultiplier).sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Edit Button
            Button(
                onClick = onEditProfile,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EditButtonColor),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Editar Información", fontSize = (16 * settings.fontSizeMultiplier).sp)
            }
        }
    }
}
