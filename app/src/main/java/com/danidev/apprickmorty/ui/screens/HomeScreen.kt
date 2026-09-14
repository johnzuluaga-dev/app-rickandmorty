package com.danidev.apprickmorty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.danidev.apprickmorty.data.model.RickCharacter
import com.danidev.apprickmorty.ui.components.BottomNavBar
import com.danidev.apprickmorty.ui.components.NavTab
import com.danidev.apprickmorty.ui.theme.*

private val filterChips = listOf("All", "Alive", "Dead", "Unknown")

@Composable
fun HomeScreen(
    userName: String = "Rick",
    onTabSelected: (NavTab) -> Unit = {},
    viewModel: HomeViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var activeFilter by remember { mutableStateOf("All") }

    Scaffold(
        containerColor = BackgroundDark,
        bottomBar = { BottomNavBar(activeTab = NavTab.HOME, onTabSelected = onTabSelected) }
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item { HomeHeader(userName) }
            item { SearchSection(onSearch = { query -> viewModel.loadCharacters(query.ifBlank { null }) }) }
            item { ChipsRow(active = activeFilter, onSelect = { activeFilter = it }) }
            item { SectionTitleRow() }

            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    item {
                        Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = NeonGreen)
                        }
                    }
                }
                is HomeUiState.Error -> {
                    item { Text("Error: ${state.message}", color = TextSecondary, modifier = Modifier.padding(20.dp)) }
                }
                is HomeUiState.Success -> {
                    val filtered = state.characters.filter {
                        activeFilter == "All" || it.status.equals(activeFilter, ignoreCase = true)
                    }
                    items(filtered) { character ->
                        CharacterTrendingCard(character, modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp))
                    }
                }
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun HomeHeader(userName: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text("BIENVENIDO DEVUELTA", style = RickMortyTextStyles.WelcomeSub, color = TextSecondary)
            Text("BUENOS DIAS $userName", style = RickMortyTextStyles.WelcomeMain, color = TextPrimary)
        }
        Box(modifier = Modifier.size(48.dp).clip(CircleShape).border(2.dp, NeonGreen, CircleShape)) {
            AsyncImage(
                model = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                contentDescription = "Avatar de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun SearchSection(onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .clip(ShapeSearchInput)
                .background(SearchInputBg)
                .border(1.dp, BorderMuted, ShapeSearchInput)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(18.dp))
            androidx.compose.foundation.text.BasicTextField(
                value = query,
                onValueChange = { query = it; onSearch(it) },
                textStyle = RickMortyTextStyles.SearchPlaceholder.copy(color = TextPrimary),
                singleLine = true,
                cursorBrush = androidx.compose.ui.graphics.SolidColor(NeonGreen),
                modifier = Modifier.weight(1f),
                decorationBox = { inner ->
                    if (query.isEmpty()) {
                        Text("Search characters...", style = RickMortyTextStyles.SearchPlaceholder, color = TextSecondary)
                    }
                    inner()
                }
            )
        }
        Box(
            modifier = Modifier.size(46.dp).clip(ShapeSearchInput).background(SearchInputBg).border(1.dp, NeonGreen, ShapeSearchInput),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Tune, contentDescription = "Filtros", tint = NeonGreen, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun ChipsRow(active: String, onSelect: (String) -> Unit) {
    LazyRow(
        modifier = Modifier.padding(horizontal = 20.dp).padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(filterChips) { label ->
            val isActive = label == active
            Box(
                modifier = Modifier
                    .clip(ShapeChip)
                    .then(if (isActive) Modifier.background(NeonGreen) else Modifier.border(1.dp, NeonGreen, ShapeChip))
                    .clickable { onSelect(label) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(label, style = RickMortyTextStyles.ChipText, color = if (isActive) ChipActiveText else NeonGreen)
            }
        }
    }
}

@Composable
private fun SectionTitleRow() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Trending Creations", style = RickMortyTextStyles.SectionTitle, color = TextPrimary)
        Text("See all", style = RickMortyTextStyles.SeeAll, color = NeonGreen)
    }
}

@Composable
private fun CharacterTrendingCard(character: RickCharacter, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().height(170.dp).clip(ShapeCard)) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Box(modifier = Modifier.fillMaxSize().background(CardOverlay))

        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(ShapeBadge)
                        .background(BadgeBg)
                        .border(1.dp, NeonGreen, ShapeBadge)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(character.status.uppercase(), style = RickMortyTextStyles.CardBadge, color = NeonGreen)
                }
                Box(
                    modifier = Modifier.size(28.dp).clip(CircleShape).background(HeartHitboxBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorito", tint = NeonGreen, modifier = Modifier.size(14.dp))
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(character.name, style = RickMortyTextStyles.CardTitle, color = TextPrimary)
                Text(character.species, style = RickMortyTextStyles.CardAuthor, color = TextSecondary)
            }
        }
    }
}
