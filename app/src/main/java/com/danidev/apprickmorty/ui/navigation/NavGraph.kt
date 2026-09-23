package com.danidev.apprickmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.danidev.apprickmorty.ui.components.NavTab
import com.danidev.apprickmorty.ui.screens.CharacterDetailScreen
import com.danidev.apprickmorty.ui.screens.ComposableCharacterScreen
import com.danidev.apprickmorty.ui.screens.HomeScreen
import com.danidev.apprickmorty.ui.screens.ProfileScreen
import com.danidev.apprickmorty.ui.screens.SplashScreen

/**
 * Rutas de la app. Se movieron aquí desde AppNavigation.kt
 * para que toda la definición de navegación viva en un solo archivo.
 */
object Screen {
    const val SPLASH = "splash_screen"
    const val HOME = "home_screen"
    const val PROFILE = "profile_screen"
    const val EXPLORE = "explore_screen"

    // Detalle con argumento obligatorio
    const val ARG_CHARACTER_ID = "characterId"
    const val CHARACTER_DETAIL = "character_detail_screen/{$ARG_CHARACTER_ID}"

    fun characterDetail(characterId: Int) = "character_detail_screen/$characterId"
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Screen.SPLASH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Splash: al terminar va a Home y se elimina del back stack
        composable(Screen.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Screen.HOME) {
                        popUpTo(Screen.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.HOME) {
            HomeScreen(onTabSelected = { tab -> navController.navigateToTab(tab) })
        }

        composable(Screen.EXPLORE) {
            ComposableCharacterScreen(
                onCharacterClick = { id ->
                    navController.navigate(Screen.characterDetail(id))
                },
                onTabSelected = { tab -> navController.navigateToTab(tab) }
            )
        }

        composable(Screen.PROFILE) {
            ProfileScreen(onTabSelected = { tab -> navController.navigateToTab(tab) })
        }

        // Detalle del personaje: recibe el id por la ruta
        composable(
            route = Screen.CHARACTER_DETAIL,
            arguments = listOf(
                navArgument(Screen.ARG_CHARACTER_ID) { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val characterId = backStackEntry.arguments?.getInt(Screen.ARG_CHARACTER_ID) ?: return@composable
            CharacterDetailScreen(
                characterId = characterId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

/** Navegación entre pestañas del BottomNavBar conservando el estado de cada una. */
fun NavHostController.navigateToTab(tab: NavTab) {
    val route = when (tab) {
        NavTab.HOME -> Screen.HOME
        NavTab.EXPLORE -> Screen.EXPLORE
        NavTab.PROFILE -> Screen.PROFILE
        NavTab.SAVED -> return // sin pantalla propia todavía
    }
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}