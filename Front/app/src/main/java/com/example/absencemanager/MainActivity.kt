

package com.example.absencemanager

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.absencemanager.ui.theme.AbsenceManagerTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            AbsenceManagerTheme {
                NavHost(
                    navController = navController,
                    startDestination = "splash"
                ) {
                    composable("splash") { SplashScreen(onGetStartedClick = {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }) }
                    composable("login") { LoginScreen(onLoginSuccess = { userId ->
                        navController.navigate("main/$userId") { // Passe l'userId dans la route
                            popUpTo("login") { inclusive = true }
                        }
                    }) }

                    composable("main/{userId}") {backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0

                        PermissionScreen(
                            onPermissionGranted = {
                                navController.navigate("home/$userId") // Écran principal après autorisation
                            }
                        )
                    }

                    composable("home/{userId}"){backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId")?.toInt() ?: 0
                        MainScreen(userId)
                    }



                }
            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionScreen(onPermissionGranted: () -> Unit) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    val coroutineScope = rememberCoroutineScope()

    if (cameraPermissionState.status.isGranted) {
        // Si la permission est déjà accordée, on exécute la suite
        coroutineScope.launch {
            onPermissionGranted()
        }
    } else {
        // Affichage d'un écran demandant la permission
        CameraPermissionUI(
            onRequestPermission = { cameraPermissionState.launchPermissionRequest() },
            onPermissionDenied = {
                // Gérer les permissions refusées ici
            }
        )
    }
}

@Composable
fun CameraPermissionUI(onRequestPermission: () -> Unit, onPermissionDenied: () -> Unit) {
    // Interface utilisateur pour demander la permission
    androidx.compose.material3.Surface {
        Column(
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            modifier = androidx.compose.ui.Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            androidx.compose.material3.Text("Cette application nécessite la permission caméra.")
            androidx.compose.material3.Button(onClick = onRequestPermission) {
                androidx.compose.material3.Text("Autoriser")
            }
            androidx.compose.material3.Button(onClick = onPermissionDenied) {
                androidx.compose.material3.Text("Refuser")
            }
        }
    }
}
