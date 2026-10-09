package com.example.panchang.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.panchang.ui.AppScreen
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.MaroonPrimary
import com.example.ui.theme.SaffronSecondary

data class MenuItemData(
    val screen: AppScreen,
    val titleHindi: String,
    val icon: ImageVector
)

val APP_MENU_ITEMS = listOf(
    MenuItemData(AppScreen.HOME, "🏠 होम (Home)", Icons.Default.Home),
    MenuItemData(AppScreen.CALENDAR, "📅 कैलेंडर (Calendar)", Icons.Default.CalendarMonth),
    MenuItemData(AppScreen.VRAT_FESTIVALS, "🪔 व्रत एवं त्योहार (Vrat & Festivals)", Icons.Default.Festival),
    MenuItemData(AppScreen.DAILY_PANCHANG, "🕉️ दैनिक पंचांग (Daily Panchang)", Icons.Default.WbSunny),
    MenuItemData(AppScreen.SETTINGS, "⚙️ सेटिंग्स (Settings)", Icons.Default.Settings),
    MenuItemData(AppScreen.ABOUT, "ℹ️ ऐप के बारे में (About App)", Icons.Default.Info)
)

@Composable
fun AppDrawerContent(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    onCloseDrawer: () -> Unit
) {
    ModalDrawerSheet(
        modifier = Modifier
            .widthIn(max = 310.dp)
            .testTag("app_navigation_drawer"),
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        // Drawer Header with Sacred Logo
        Surface(
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(MaroonPrimary)
                        .border(1.5.dp, GoldAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ॐ",
                        color = GoldAccent,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "हिंदू पंचांग",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "दैनिक पंचांग, तिथि एवं त्योहार सेवा",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation Items
        APP_MENU_ITEMS.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationDrawerItem(
                label = {
                    Text(
                        text = item.titleHindi,
                        fontSize = 14.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                selected = isSelected,
                onClick = {
                    onCloseDrawer()
                    onNavigate(item.screen)
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 2.dp)
                    .testTag("menu_item_${item.screen.name.lowercase()}"),
                shape = RoundedCornerShape(10.dp),
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Footer in drawer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "॥ धर्मो रक्षति रक्षितः ॥\nVersion 1.0.0",
                fontSize = 11.5.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
