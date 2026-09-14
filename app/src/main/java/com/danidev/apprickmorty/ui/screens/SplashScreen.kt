package com.danidev.apprickmorty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.danidev.apprickmorty.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.radialGradient(
                colors = listOf(SplashGradientCenter, SplashGradientMid, SplashGradientEdge),
                radius = 900f
            )
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 44.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.height(120.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(32.dp)) {
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(elevation = 6.dp, shape = CircleShape, ambientColor = NeonGreen, spotColor = NeonGreen)
                        .clip(CircleShape)
                        .background(GlowCircleBg)
                        .border(width = 3.dp, color = NeonGreen, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(48.dp))
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Text("APP RICKMORTY", style = RickMortyTextStyles.AppTitle, color = NeonGreen, textAlign = TextAlign.Center)
                    Text("DIMENSION C-137 EXPLORER", style = RickMortyTextStyles.SplashSubtitle, color = TextSecondary, textAlign = TextAlign.Center)
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Text("SYNCING MULTIVERSE...", style = RickMortyTextStyles.LoadingLabel, color = TextSecondary)
                Box(
                    modifier = Modifier.width(200.dp).height(6.dp).clip(RoundedCornerShape(3.dp)).background(BorderMuted)
                ) {
                    Box(
                        modifier = Modifier.width(120.dp).fillMaxHeight().clip(RoundedCornerShape(3.dp)).background(NeonGreen)
                    )
                }
            }
        }
    }
}
