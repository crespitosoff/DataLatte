package com.example.datalatte.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object para la tabla de cafés.
 * Todas las consultas de lectura retornan Flow para reactividad.
 */
@Dao
interface CoffeeDao {

    /** Obtiene todos los cafés ordenados por id. */
    @Query("SELECT * FROM coffees ORDER BY id ASC")
    fun getAll(): Flow<List<CoffeeEntity>>

    /** Obtiene solo los cafés marcados como favoritos. */
    @Query("SELECT * FROM coffees WHERE isFavorite = 1 ORDER BY id ASC")
    fun getFavorites(): Flow<List<CoffeeEntity>>

    /** Busca cafés cuyo título contenga la consulta (case-insensitive). */
    @Query("SELECT * FROM coffees WHERE title LIKE '%' || :query || '%' ORDER BY id ASC")
    fun search(query: String): Flow<List<CoffeeEntity>>

    /** Busca entre favoritos cuyo título contenga la consulta. */
    @Query("SELECT * FROM coffees WHERE isFavorite = 1 AND title LIKE '%' || :query || '%' ORDER BY id ASC")
    fun searchFavorites(query: String): Flow<List<CoffeeEntity>>

    /** Obtiene un café por su id. */
    @Query("SELECT * FROM coffees WHERE id = :id")
    fun getById(id: Int): Flow<CoffeeEntity?>

    /** Inserta una lista de cafés. Si hay conflicto de id, reemplaza. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coffees: List<CoffeeEntity>)

    /** Alterna el estado de favorito de un café. */
    @Query("UPDATE coffees SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFavorite: Boolean)

    /** Cuenta el número de registros (para saber si la BD está vacía). */
    @Query("SELECT COUNT(*) FROM coffees")
    suspend fun getCount(): Int

    /** Elimina todos los cafés (vaciar caché). */
    @Query("DELETE FROM coffees")
    suspend fun deleteAll()
}
