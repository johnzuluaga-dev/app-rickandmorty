package com.danidev.apprickmorty.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Usa FontFamily.Default para compilar sin archivos .ttf adicionales.
 * Para usar las fuentes reales del diseño (Unbounded/Geist), descárgalas
 * de Google Fonts, colócalas en res/font/ y reemplaza estas dos líneas.
 */
val UnboundedFamily = FontFamily.Default
val GeistFamily = FontFamily.Default

object RickMortyTextStyles {
    val AppTitle = TextStyle(fontFamily = UnboundedFamily, fontWeight = FontWeight.Black, fontSize = 26.sp)
    val SplashSubtitle = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.Medium, fontSize = 12.sp)
    val LoadingLabel = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
    val WelcomeSub = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp)
    val WelcomeMain = TextStyle(fontFamily = UnboundedFamily, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
    val SearchPlaceholder = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp)
    val ChipText = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    val SectionTitle = TextStyle(fontFamily = UnboundedFamily, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
    val SeeAll = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
    val CardBadge = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.Bold, fontSize = 10.sp)
    val CardTitle = TextStyle(fontFamily = UnboundedFamily, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
    val CardAuthor = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.Normal, fontSize = 12.sp)
    val NavLabel = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.SemiBold, fontSize = 10.sp)
    val ProfileHeaderTitle = TextStyle(fontFamily = UnboundedFamily, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
    val ProfileName = TextStyle(fontFamily = UnboundedFamily, fontWeight = FontWeight.Black, fontSize = 16.sp)
    val ProfileBio = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.Normal, fontSize = 13.sp)
    val StatValue = TextStyle(fontFamily = UnboundedFamily, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
    val StatLabel = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.Normal, fontSize = 11.sp)
    val AccountSettingsHeader = TextStyle(fontFamily = UnboundedFamily, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
    val SettingItemLabel = TextStyle(fontFamily = GeistFamily, fontWeight = FontWeight.Medium, fontSize = 14.sp)
}

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = GeistFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)
