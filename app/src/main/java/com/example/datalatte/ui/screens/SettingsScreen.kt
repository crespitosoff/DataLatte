package com.example.datalatte.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.datalatte.ui.viewmodel.CoffeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: CoffeeViewModel,
    onNavigateBack: () -> Unit
) {
    val isDarkTheme by viewModel.preferencesManager.isDarkTheme.collectAsState()
    val isGridView by viewModel.preferencesManager.isGridView.collectAsState()
    val showOnlyFavorites by viewModel.preferencesManager.showOnlyFavorites.collectAsState()

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
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Preferencias",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Tema Oscuro")
                Switch(
                    checked = isDarkTheme,
                    onCheckedChange = { viewModel.preferencesManager.setDarkTheme(it) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Vista en Cuadrícula")
                Switch(
                    checked = isGridView,
                    onCheckedChange = { viewModel.preferencesManager.setGridView(it) }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Mostrar Solo Favoritos al Inicio")
                Switch(
                    checked = showOnlyFavorites,
                    onCheckedChange = { viewModel.preferencesManager.setShowOnlyFavorites(it) }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    viewModel.clearCacheAndReload()
                    onNavigateBack() // Opcional, para volver al home tras vaciar caché
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Vaciar Caché Local")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
