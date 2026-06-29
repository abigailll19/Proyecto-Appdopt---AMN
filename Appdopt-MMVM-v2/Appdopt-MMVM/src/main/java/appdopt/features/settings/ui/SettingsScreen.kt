package com.example.appdopt.features.settings.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appdopt.core.model.AppSettings
import com.example.appdopt.core.model.AppLanguage
import com.example.appdopt.core.model.ColorBlindMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: AppSettings,
    onUpdateDarkMode: (Boolean) -> Unit,
    onUpdateHighContrast: (Boolean) -> Unit,
    onUpdateFontSize: (Float) -> Unit,
    onUpdateColorBlindMode: (ColorBlindMode) -> Unit,
    onUpdateLanguage: (AppLanguage) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ajustes") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Accesibilidad", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            ListItem(
                headlineContent = { Text("Modo Oscuro") },
                trailingContent = {
                    Switch(
                        checked = settings.isDarkMode,
                        onCheckedChange = onUpdateDarkMode
                    )
                }
            )

            ListItem(
                headlineContent = { Text("Alto Contraste") },
                trailingContent = {
                    Switch(
                        checked = settings.isHighContrast,
                        onCheckedChange = onUpdateHighContrast
                    )
                }
            )

            Text("Tamaño de Texto", modifier = Modifier.padding(top = 16.dp))
            Slider(
                value = settings.fontSizeMultiplier,
                onValueChange = onUpdateFontSize,
                valueRange = 0.8f..1.5f,
                steps = 3
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Daltonismo", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            ColorBlindMode.entries.forEach { mode ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = settings.colorBlindMode == mode,
                        onClick = { onUpdateColorBlindMode(mode) }
                    )
                    Text(mode.name, modifier = Modifier.padding(start = 8.dp))
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Idioma", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            AppLanguage.entries.forEach { lang ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = settings.language == lang,
                        onClick = { onUpdateLanguage(lang) }
                    )
                    Text(lang.name, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}
