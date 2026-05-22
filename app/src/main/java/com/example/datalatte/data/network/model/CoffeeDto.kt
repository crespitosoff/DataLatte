package com.example.datalatte.data.network.model

/**
 * Data Transfer Object (DTO) que representa un café tal como
 * llega de la API: GET https://api.sampleapis.com/coffee/hot
 *
 * Mapea directamente la estructura JSON:
 * { "id": 1, "title": "...", "description": "...", "ingredients": [...], "image": "..." }
 */
data class CoffeeDto(
    val id: Int,
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val image: String
)
