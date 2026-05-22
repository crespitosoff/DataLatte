package com.example.datalatte.data.repository

import com.example.datalatte.data.local.CoffeeDao
import com.example.datalatte.data.local.CoffeeEntity
import com.example.datalatte.data.network.CoffeeApiService
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio que implementa el patrón Single Source of Truth.
 *
 * Flujo de datos:
 *   API  ──▶  Room (escritura)
 *   Room ──▶  UI   (lectura reactiva vía Flow)
 *
 * La UI nunca lee directamente de la API.
 */
class CoffeeRepository(
    private val dao: CoffeeDao,
    private val api: CoffeeApiService
) {

    /** Flujo reactivo con todos los cafés desde Room. */
    fun getAllCoffees(): Flow<List<CoffeeEntity>> = dao.getAll()

    /** Flujo reactivo solo con favoritos. */
    fun getFavorites(): Flow<List<CoffeeEntity>> = dao.getFavorites()

    /** Búsqueda reactiva por título en todos los cafés. */
    fun search(query: String): Flow<List<CoffeeEntity>> = dao.search(query)

    /** Búsqueda reactiva por título solo entre favoritos. */
    fun searchFavorites(query: String): Flow<List<CoffeeEntity>> = dao.searchFavorites(query)

    /** Obtiene un café por id (reactivo). */
    fun getCoffeeById(id: Int): Flow<CoffeeEntity?> = dao.getById(id)

    /**
     * Sincroniza desde la API si Room está vacío.
     * Convierte DTOs a Entities preservando isFavorite = false por defecto.
     */
    suspend fun refreshIfEmpty() {
        val count = dao.getCount()
        if (count == 0) {
            fetchFromApi()
        }
    }

    /**
     * Fuerza una nueva descarga desde la API.
     * Borra todo el contenido local y vuelve a insertar desde la red.
     */
    suspend fun forceRefresh() {
        dao.deleteAll()
        fetchFromApi()
    }

    /** Alterna el estado de favorito de un café. */
    suspend fun toggleFavorite(id: Int, currentStatus: Boolean) {
        dao.updateFavorite(id, !currentStatus)
    }

    /** Vacía toda la caché local y recarga desde la API. */
    suspend fun clearCacheAndReload() {
        forceRefresh()
    }

    /**
     * Descarga los cafés de la API y los inserta en Room.
     * Mapea CoffeeDto → CoffeeEntity.
     */
    private suspend fun fetchFromApi() {
        val dtos = api.getHotCoffees()
        val entities = dtos.map { dto ->
            CoffeeEntity(
                id = dto.id,
                title = dto.title,
                description = dto.description,
                ingredients = dto.ingredients,
                image = dto.image,
                isFavorite = false
            )
        }
        dao.insertAll(entities)
    }
}
