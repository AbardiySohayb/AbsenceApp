package com.example.absencemanager.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.absencemanager.R

val monasemi = FontFamily(Font(R.font.mona_semibold))
@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String
) {
    val items = listOf(
        BottomNavItem("Home", R.drawable.home, R.drawable.home_selected, "home"),
        BottomNavItem("Profile", R.drawable.profile, R.drawable.profile, "profile"),
        BottomNavItem("", R.drawable.add, R.drawable.add, "add"),
        BottomNavItem("Classes", R.drawable.classroom, R.drawable.classroom_selected, "classes"),
        BottomNavItem("Settings", R.drawable.settings, R.drawable.settings, "settings")
    )

    NavigationBar(containerColor = Color.White, modifier = Modifier.height(70.dp).width(400.dp)) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id =item.iconId),
                        contentDescription = item.label,
                        modifier = Modifier
                            .size(if (item.route == "add") 52.dp else 34.dp).padding(if (item.route == "add") 5.dp else 2.dp),// Inner spacing
                        tint =  if (isSelected) {Color.White}  else if (item.route == "add"){Color.Unspecified} else { Color.Gray }  // White icon on selection
                    )
                },
                label = {
                    Text(
                        fontFamily = monasemi,
                        text = item.label,
                        color = if (isSelected) Color(0xFF665215) else Color.Gray
                    )
                },
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo("home") { inclusive = false }
                        }
                    }
                },
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White, // Explicit white color for selected icon
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color(0xFF665215),
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color(0xFF665215) // Remove underline
                )
            )
        }
    }

}
    data class BottomNavItem(
    val label: String,
    val iconId: Int,
    val selectedIconId: Int,
    val route: String
)



@Preview(showBackground = true)
@Composable
fun BottomNavigationBarPreview() {
    val navController = rememberNavController()
    BottomNavigationBar(navController = navController, currentRoute = "settings")
}
