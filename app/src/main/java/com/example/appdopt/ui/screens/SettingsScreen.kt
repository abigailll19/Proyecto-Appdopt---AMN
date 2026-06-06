package com.example.appdopt.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appdopt.model.AppLanguage
import com.example.appdopt.model.ColorBlindMode
import com.example.appdopt.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: AppViewModel,
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            Text("Accessibility", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            ListItem(
                headlineContent = { Text("Dark Mode") },
                trailingContent = {
                    Switch(
                        checked = settings.isDarkMode,
                        onCheckedChange = { viewModel.updateDarkMode(it) }
                    )
                }
            )

            ListItem(
                headlineContent = { Text("High Contrast") },
                trailingContent = {
                    Switch(
                        checked = settings.isHighContrast,
                        onCheckedChange = { viewModel.updateHighContrast(it) }
                    )
                }
            )

            Text("Text Size", modifier = Modifier.padding(top = 16.dp))
            Slider(
                value = settings.fontSizeMultiplier,
                onValueChange = { viewModel.updateFontSize(it) },
                valueRange = 0.8f..1.5f,
                steps = 3
            )

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Color Blind Palette", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            ColorBlindMode.values().forEach { mode ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = settings.colorBlindMode == mode,
                        onClick = { viewModel.updateColorBlindMode(mode) }
                    )
                    Text(mode.name, modifier = Modifier.padding(start = 8.dp))
                }
            }

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text("Language", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            AppLanguage.values().forEach { lang ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = settings.language == lang,
                        onClick = { viewModel.updateLanguage(lang) }
                    )
                    Text(lang.name, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }
    }
}
