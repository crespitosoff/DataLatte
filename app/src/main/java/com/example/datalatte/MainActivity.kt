package com.example.datalatte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.datalatte.ui.navigation.DataLatteNavGraph
import com.example.datalatte.ui.theme.DataLatteTheme
import com.example.datalatte.ui.viewmodel.CoffeeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: CoffeeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkThemePref by viewModel.preferencesManager.isDarkTheme.collectAsState()
            
            // Si la preferencia está activa, forzamos el tema oscuro.
            // Si está inactiva, se respeta si el sistema está en modo oscuro o no.
            val useDarkTheme = isDarkThemePref || isSystemInDarkTheme()

            DataLatteTheme(darkTheme = useDarkTheme) {
                DataLatteNavGraph(viewModel = viewModel)
            }
        }
    }
}