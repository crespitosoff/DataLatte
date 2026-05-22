package com.example.datalatte.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.datalatte.data.local.AppDatabase
import com.example.datalatte.data.local.CoffeeEntity
import com.example.datalatte.data.network.RetrofitInstance
import com.example.datalatte.data.preferences.PreferencesManager
import com.example.datalatte.data.repository.CoffeeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel principal que orquesta la lógica de la app.
 *
 * Patrón Single Source of Truth:
 * - Al iniciar, verifica si Room está vacío → si sí, llama a la API.
 * - La UI solo observa Flows emitidos desde Room.
 * - Las búsquedas se ejecutan como queries reactivas en Room.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class CoffeeViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = CoffeeRepository(database.coffeeDao(), RetrofitInstance.api)
    val preferencesManager = PreferencesManager(application)

    // ── Estado de búsqueda ──
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // ── Estado de carga y error ──
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    /**
     * Lista reactiva de cafés.
     * Combina el query de búsqueda con la preferencia de "solo favoritos"
     * para seleccionar dinámicamente la consulta Room correcta.
     */
    val coffees: StateFlow<List<CoffeeEntity>> =
        combine(_searchQuery, preferencesManager.showOnlyFavorites) { query, onlyFavs ->
            Pair(query, onlyFavs)
        }.flatMapLatest { (query, onlyFavs) ->
            when {
                query.isBlank() && !onlyFavs -> repository.getAllCoffees()
                query.isBlank() && onlyFavs -> repository.getFavorites()
                query.isNotBlank() && !onlyFavs -> repository.search(query)
                else -> repository.searchFavorites(query)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    init {
        // Carga inicial: API → Room si la BD está vacía
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.refreshIfEmpty()
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar datos: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Obtiene un café por id (reactivo desde Room). */
    fun getCoffeeById(id: Int) = repository.getCoffeeById(id)

    /** Actualiza el texto de búsqueda. La lista se filtra reactivamente. */
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    /** Alterna el estado de favorito de un café. */
    fun toggleFavorite(coffee: CoffeeEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(coffee.id, coffee.isFavorite)
        }
    }

    /**
     * Vacía la caché local (Room) y fuerza una recarga desde la API.
     * Usado desde la pantalla de Settings como "botón de peligro".
     */
    fun clearCacheAndReload() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                repository.clearCacheAndReload()
            } catch (e: Exception) {
                _errorMessage.value = "Error al recargar: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /** Limpia el mensaje de error después de mostrarlo. */
    fun clearError() {
        _errorMessage.value = null
    }
}
