package com.danidev.apprickmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danidev.apprickmorty.data.AuthService
import com.danidev.apprickmorty.ui.components.NavTab
import com.danidev.apprickmorty.ui.screens.CharacterDetailScreen
import com.danidev.apprickmorty.ui.screens.ComposableCharacterScreen
import com.danidev.apprickmorty.ui.screens.HomeScreen
import com.danidev.apprickmorty.ui.screens.LoginScreen
import com.danidev.apprickmorty.ui.screens.ProfileScreen
import com.danidev.apprickmorty.ui.screens.RegisterScreen

@Composable
fun AppNavigation(authService: AuthService = AuthService()) {
    val navController = rememberNavController()

    // Validar si hay usuario con sesión activa en Firebase Auth
    val currentUser = authService.getCurrentUser()
    val startDestination = if (currentUser != null) Screen.HOME else Screen.LOGIN

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.LOGIN) {
            LoginScreen(
                authService = authService,
                onLoginSuccess = {
                    navController.navigate(Screen.HOME) {
                        popUpTo(Screen.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.REGISTER)
                }
            )
        }

        composable(Screen.REGISTER) {
            RegisterScreen(
                authService = authService,
                onRegisterSuccess = {
                    navController.navigate(Screen.HOME) {
                        popUpTo(Screen.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
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
            ProfileScreen(
                authService = authService,
                onLogout = {
                    authService.logout()
                    navController.navigate(Screen.LOGIN) {
                        popUpTo(Screen.HOME) { inclusive = true }
                    }
                },
                onTabSelected = { tab -> navController.navigateToTab(tab) }
            )
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

/** Navegación entre pestañas del BottomNavBar conservando el estado de cada una. */
fun NavHostController.navigateToTab(tab: NavTab) {
    val route = when (tab) {
        NavTab.HOME -> Screen.HOME
        NavTab.EXPLORE -> Screen.EXPLORE
        NavTab.PROFILE -> Screen.PROFILE
        NavTab.SAVED -> return
    }
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
