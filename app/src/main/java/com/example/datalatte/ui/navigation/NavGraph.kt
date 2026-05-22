package com.example.datalatte.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.datalatte.ui.screens.DetailScreen
import com.example.datalatte.ui.screens.HomeScreen
import com.example.datalatte.ui.screens.SettingsScreen
import com.example.datalatte.ui.viewmodel.CoffeeViewModel

object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{coffeeId}"
    const val SETTINGS = "settings"

    fun createDetailRoute(coffeeId: Int) = "detail/$coffeeId"
}

@Composable
fun DataLatteNavGraph(
    viewModel: CoffeeViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToDetail = { coffeeId ->
                    navController.navigate(Routes.createDetailRoute(coffeeId))
                },
                onNavigateToSettings = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("coffeeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val coffeeId = backStackEntry.arguments?.getInt("coffeeId") ?: return@composable
            DetailScreen(
                coffeeId = coffeeId,
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
