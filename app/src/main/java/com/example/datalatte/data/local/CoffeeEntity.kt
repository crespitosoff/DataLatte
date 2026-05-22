package com.example.datalatte.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Room que representa un café almacenado localmente.
 * Mapea el modelo JSON de la API e incluye el campo adicional [isFavorite].
 */
@Entity(tableName = "coffees")
data class CoffeeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val image: String,
    val isFavorite: Boolean = false
)
