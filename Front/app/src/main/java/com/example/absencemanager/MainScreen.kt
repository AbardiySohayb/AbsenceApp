package com.example.absencemanager

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.absencemanager.layout.BottomNavigationBar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun MainScreen(userId: Int) {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: "home"

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController, currentRoute = currentRoute)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") { DashBoardScreen(userId) }
            composable("profile") { ProfileScreen() }
            composable("add") { AddScreen() }
            composable("classes") { MyClassesScreen(navController=navController,userId) }
            composable("settings") { SettingsScreen() }
            composable("studentList") { StudentListScreen() } // Nouveau screen pour la liste des étudiants
        }
    }
}

@Composable
fun AddScreen() {
    TODO("Not yet implemented")
}

@Composable
fun SettingsScreen() {
    TODO("Not yet implemented")
}

@Composable
fun ProfileScreen() {
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {

    MainScreen(55)
}

