package com.example.datalatte.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.datalatte.ui.viewmodel.CoffeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    coffeeId: Int,
    viewModel: CoffeeViewModel,
    onNavigateBack: () -> Unit
) {
    val coffee by viewModel.getCoffeeById(coffeeId).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(coffee?.title ?: "Cargando...") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        floatingActionButton = {
            coffee?.let { currentCoffee ->
                FloatingActionButton(
                    onClick = { viewModel.toggleFavorite(currentCoffee) }
                ) {
                    Icon(
                        imageVector = if (currentCoffee.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorito"
                    )
                }
            }
        }
    ) { innerPadding ->
        coffee?.let { currentCoffee ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = currentCoffee.image,
                    contentDescription = currentCoffee.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f),
                    contentScale = ContentScale.Crop
                )
                
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = currentCoffee.title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Descripción",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = currentCoffee.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Ingredientes",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    currentCoffee.ingredients.forEach { ingredient ->
                        Text(
                            text = "• $ingredient",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        } ?: run {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
