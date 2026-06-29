package com.example.appdopt.features.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appdopt.R
import com.example.appdopt.core.model.AppSettings
import com.example.appdopt.core.viewmodel.LoginUiState
import com.example.appdopt.ui.theme.AppdoptTheme
import com.example.appdopt.ui.theme.BluePrimary

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginRoute(
    onLoginSuccess: (Boolean) -> Unit,
    onRegisterClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoginSuccess(uiState.isAdmin)
            viewModel.resetLoginStatus()
        }
    }

    LoginScreen(
        state = uiState,
        onUpdateEmail = viewModel::updateEmail,
        onUpdatePassword = viewModel::updatePassword,
        onLoginClick = viewModel::login,
        onRegisterClick = onRegisterClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    state: LoginUiState,
    onUpdateEmail: (String) -> Unit,
    onUpdatePassword: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ... (resto del código del logo y textos igual)

        if (state.errorMessage != null) {
            Text(text = state.errorMessage, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
        }

        OutlinedTextField(
            value = state.email,
            onValueChange = onUpdateEmail,
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = !state.isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password,
            onValueChange = onUpdatePassword,
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.VisibilityOff, contentDescription = null) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = !state.isLoading
        )

        // ... (botones y demás)

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            enabled = !state.isLoading
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
        // ...
    }
}

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("¿No tienes cuenta? ", color = Color.Gray)
            TextButton(onClick = onRegisterClick) {
                Text("Regístrate", color = BluePrimary, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("O continúa con", color = Color.Gray, fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            // Placeholders for social icons
            Surface(shape = CircleShape, color = Color(0xFFF5F5F5), modifier = Modifier.size(48.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Pets, contentDescription = null, modifier = Modifier.size(24.dp)) }
            }
            Surface(shape = CircleShape, color = Color(0xFFF5F5F5), modifier = Modifier.size(48.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Pets, contentDescription = null, modifier = Modifier.size(24.dp)) }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    AppdoptTheme(settings = AppSettings()) {
        LoginScreen(
            state = LoginUiState(email = "test@appdopt.com"),
            onUpdateEmail = {},
            onUpdatePassword = {},
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}
