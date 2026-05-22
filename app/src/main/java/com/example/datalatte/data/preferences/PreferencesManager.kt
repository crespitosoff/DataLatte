package com.example.datalatte.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Gestor de preferencias de la app usando SharedPreferences.
 * Expone StateFlows para que la UI reaccione a cambios en tiempo real.
 */
class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ── Tema oscuro ──
    private val _isDarkTheme = MutableStateFlow(prefs.getBoolean(KEY_DARK_THEME, false))
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun setDarkTheme(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_DARK_THEME, enabled).apply()
        _isDarkTheme.value = enabled
    }

    // ── Vista en cuadrícula ──
    private val _isGridView = MutableStateFlow(prefs.getBoolean(KEY_GRID_VIEW, false))
    val isGridView: StateFlow<Boolean> = _isGridView.asStateFlow()

    fun setGridView(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_GRID_VIEW, enabled).apply()
        _isGridView.value = enabled
    }

    // ── Mostrar solo favoritos al inicio ──
    private val _showOnlyFavorites = MutableStateFlow(prefs.getBoolean(KEY_SHOW_FAVORITES, false))
    val showOnlyFavorites: StateFlow<Boolean> = _showOnlyFavorites.asStateFlow()

    fun setShowOnlyFavorites(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_FAVORITES, enabled).apply()
        _showOnlyFavorites.value = enabled
    }

    companion object {
        private const val PREFS_NAME = "datalatte_prefs"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_GRID_VIEW = "grid_view"
        private const val KEY_SHOW_FAVORITES = "show_only_favorites"
    }
}
