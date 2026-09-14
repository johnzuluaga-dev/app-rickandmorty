package com.danidev.apprickmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danidev.apprickmorty.ui.components.NavTab
import com.danidev.apprickmorty.ui.screens.ComposableCharacterScreen
import com.danidev.apprickmorty.ui.screens.HomeScreen
import com.danidev.apprickmorty.ui.screens.ProfileScreen
import com.danidev.apprickmorty.ui.screens.SplashScreen

object Screen {
    const val SPLASH = "splash_screen"
    const val HOME = "home_screen"
    const val PROFILE = "profile_screen"
    const val EXPLORE = "explore_screen"
}

@Composable
fun AppNavigation(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Screen.SPLASH) {
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
        composable(Screen.PROFILE) {
            ProfileScreen(onTabSelected = { tab -> navController.navigateToTab(tab) })
        }
        composable(Screen.EXPLORE) {
            ComposableCharacterScreen(
                onCharacterClick = { /* sin pantalla de detalle todavía */ },
                onTabSelected = { tab -> navController.navigateToTab(tab) }
            )
        }
    }
}

private fun NavHostController.navigateToTab(tab: NavTab) {
    val route = when (tab) {
        NavTab.HOME -> Screen.HOME
        NavTab.PROFILE -> Screen.PROFILE
        NavTab.EXPLORE -> Screen.EXPLORE
        NavTab.SAVED -> return // sin pantalla propia todavía
    }
    navigate(route) {
        popUpTo(graph.startDestinationId) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
