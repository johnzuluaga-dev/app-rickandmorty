package com.danidev.apprickmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.danidev.apprickmorty.ui.screens.CharacterDetailScreen
import com.danidev.apprickmorty.ui.screens.ComposableCharacterScreen
import com.danidev.apprickmorty.ui.screens.HomeScreen
import com.danidev.apprickmorty.ui.screens.ProfileScreen
import com.danidev.apprickmorty.ui.screens.SplashScreen

/**
 * Rutas de la app.
 */
object Screen {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val SPLASH = "splash_screen"
    const val HOME = "home"
    const val PROFILE = "profile"
    const val EXPLORE = "explore"

    // Detalle con argumento obligatorio
    const val ARG_CHARACTER_ID = "characterId"
    const val CHARACTER_DETAIL = "character_detail/{$ARG_CHARACTER_ID}"

    fun characterDetail(characterId: Int) = "character_detail/$characterId"
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
            HomeScreen(
                onTabSelected = { tab -> navController.navigateToTab(tab) },
                onCharacterClick = { id ->
                    navController.navigate(Screen.characterDetail(id))
                }
            )
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
