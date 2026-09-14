package com.danidev.apprickmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.danidev.apprickmorty.ui.navigation.AppNavigation
import com.danidev.apprickmorty.ui.theme.ApprickmortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApprickmortyTheme {
                AppNavigation()
            }
        }
    }
}
