package com.danidev.apprickmorty.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.danidev.apprickmorty.data.AuthService
import com.danidev.apprickmorty.data.CloudinaryManager
import com.danidev.apprickmorty.ui.components.BottomNavBar
import com.danidev.apprickmorty.ui.components.NavTab
import com.danidev.apprickmorty.ui.theme.*

data class ProfileStat(val value: String, val label: String)
data class SettingItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String,
    val hasToggle: Boolean,
    val toggleDefault: Boolean = false
)

private val stats = listOf(
    ProfileStat("42", "Creations"),
    ProfileStat("1.2k", "Followers"),
    ProfileStat("312", "Following")
)

private val settingsItems = listOf(
    SettingItem(Icons.Default.ManageAccounts, "Edit Profile", hasToggle = false),
    SettingItem(Icons.Default.Notifications, "Notifications", hasToggle = true, toggleDefault = true),
    SettingItem(Icons.Default.DarkMode, "Dark Mode", hasToggle = true, toggleDefault = true),
    SettingItem(Icons.Default.Language, "Language", hasToggle = false),
    SettingItem(Icons.Default.Lock, "Privacy", hasToggle = false)
)

@Composable
fun ProfileScreen(
    authService: AuthService = AuthService(),
    onLogout: () -> Unit = {},
    onTabSelected: (NavTab) -> Unit = {}
) {
    val context = LocalContext.current
    val currentUser = authService.getCurrentUser()

    var name by remember { mutableStateOf(currentUser?.displayName ?: currentUser?.email?.substringBefore("@") ?: "RICK SANCHEZ") }
    var bio by remember { mutableStateOf("Dimension C-137. I build batteries. Don't touch my stuff.") }
    var avatarUrl by remember { mutableStateOf("https://rickandmortyapi.com/api/character/avatar/1.jpeg") }
    var isUploading by remember { mutableStateOf(false) }

    // Cargar datos guardados previamente desde Firestore
    LaunchedEffect(Unit) {
        authService.obtenerPerfil(
            onSuccess = { datos ->
                datos?.let {
                    if (it["nombre"] != null) name = it["nombre"].toString()
                    if (it["photoUrl"] != null && it["photoUrl"].toString().isNotEmpty()) {
                        avatarUrl = it["photoUrl"].toString()
                    }
                }
            },
            onError = { /* Mantener datos por defecto */ }
        )
    }

    // Launcher para abrir la galería del dispositivo
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            isUploading = true
            Toast.makeText(context, "Subiendo imagen a Cloudinary...", Toast.LENGTH_SHORT).show()

            // Subir a Cloudinary
            CloudinaryManager.subirFoto(
                uri = selectedUri,
                onSuccess = { url ->
                    avatarUrl = url
                    isUploading = false
                    // Guardar nueva URL en Firestore
                    authService.guardarPerfil(
                        nombre = name,
                        email = currentUser?.email ?: "",
                        photoUrl = url,
                        onSuccess = {
                            Toast.makeText(context, "¡Foto actualizada y guardada!", Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                onError = { error ->
                    isUploading = false
                    Toast.makeText(context, "Error al subir: $error", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = { BottomNavBar(activeTab = NavTab.PROFILE, onTabSelected = onTabSelected) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item { ProfileHeaderBar(onLogout = onLogout) }
            item {
                ProfileBlock(
                    name = name,
                    bio = bio,
                    avatarUrl = avatarUrl,
                    isUploading = isUploading,
                    onAvatarClick = { galleryLauncher.launch("image/*") }
                )
            }
            item { StatsRow() }
            item {
                Text(
                    "ACCOUNT SETTINGS",
                    style = RickMortyTextStyles.AccountSettingsHeader,
                    color = NeonGreen,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)
                )
            }
            items(settingsItems) { item ->
                SettingRow(item, modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp))
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun ProfileHeaderBar(onLogout: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("My Profile", style = RickMortyTextStyles.ProfileHeaderTitle, color = TextPrimary)
        IconButton(
            onClick = onLogout,
            modifier = Modifier.size(36.dp).clip(CircleShape).background(SearchInputBg).border(1.dp, NeonGreen, CircleShape)
        ) {
            Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Cerrar Sesión", tint = NeonGreen, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun ProfileBlock(
    name: String,
    bio: String,
    avatarUrl: String,
    isUploading: Boolean,
    onAvatarClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .shadow(elevation = 12.dp, shape = CircleShape, ambientColor = NeonGreen, spotColor = NeonGreen)
                .clip(CircleShape)
                .border(3.dp, NeonGreen, CircleShape)
                .clickable { onAvatarClick() },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Foto de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            if (isUploading) {
                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonGreen, modifier = Modifier.size(32.dp))
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(name, style = RickMortyTextStyles.ProfileName, color = TextPrimary, textAlign = TextAlign.Center)
            Text(bio, style = RickMortyTextStyles.ProfileBio, color = TextSecondary, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun StatsRow() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 24.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        stats.forEachIndexed { index, stat ->
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(stat.value, style = RickMortyTextStyles.StatValue, color = TextPrimary)
                    Text(stat.label, style = RickMortyTextStyles.StatLabel, color = TextSecondary)
                }
                if (index < stats.lastIndex) {
                    Box(modifier = Modifier.width(1.dp).height(24.dp).background(DividerColor))
                }
            }
        }
    }
}

@Composable
private fun SettingRow(item: SettingItem, modifier: Modifier = Modifier) {
    var checked by remember { mutableStateOf(item.toggleDefault) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(ShapeSettingsRow)
            .background(SettingsRowBg)
            .padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(28.dp).clip(ShapeIconWrap).background(IconWrapBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(14.dp))
            }
            Text(item.label, style = RickMortyTextStyles.SettingItemLabel, color = TextPrimary)
        }

        if (item.hasToggle) {
            Switch(
                checked = checked,
                onCheckedChange = { checked = it },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = NeonGreen,
                    checkedThumbColor = Color.White,
                    uncheckedTrackColor = Color(0xFF2A2E42),
                    uncheckedThumbColor = Color.White
                )
            )
        } else {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
        }
    }
}