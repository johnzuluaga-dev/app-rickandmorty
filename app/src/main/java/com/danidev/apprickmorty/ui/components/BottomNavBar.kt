package com.danidev.apprickmorty.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.danidev.apprickmorty.ui.theme.*

enum class NavTab { HOME, EXPLORE, SAVED, PROFILE }

@Composable
fun BottomNavBar(
    activeTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(BackgroundDark)
            .border(width = 1.dp, color = BorderMuted)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavItem(NavTab.HOME, "Home", Icons.Default.Home, activeTab, onTabSelected)
            NavItem(NavTab.EXPLORE, "Explore", Icons.Default.Explore, activeTab, onTabSelected)
            NavItem(NavTab.SAVED, "Saved", Icons.Default.BookmarkBorder, activeTab, onTabSelected)
            NavItem(NavTab.PROFILE, "Profile", Icons.Default.Person, activeTab, onTabSelected)
        }
        Box(modifier = Modifier.fillMaxWidth().height(24.dp), contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .width(134.dp)
                    .height(5.dp)
                    .clip(RoundedCornerShape(100.dp))
                    .background(HomeIndicator)
            )
        }
    }
}

@Composable
private fun NavItem(
    tab: NavTab,
    label: String,
    icon: ImageVector,
    activeTab: NavTab,
    onTabSelected: (NavTab) -> Unit
) {
    val isActive = tab == activeTab
    val tint = if (isActive) NeonGreen else TextSecondary

    Column(
        modifier = Modifier.width(64.dp).fillMaxHeight().clickable { onTabSelected(tab) },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.width(24.dp).height(2.dp).background(if (isActive) NeonGreen else Color.Transparent))
        Spacer(Modifier.height(4.dp))
        Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(20.dp))
        Spacer(Modifier.height(4.dp))
        Text(label, style = RickMortyTextStyles.NavLabel, color = tint)
    }
}
