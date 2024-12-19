package com.example.absencemanager

import android.app.Activity
import android.util.Log
import android.view.ViewGroup
import android.view.WindowManager
import androidx.camera.core.CameraSelector
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.absencemanager.layout.monasemi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Font setup


data class ClassItem(
    val name: String,
    val studentsCount: Int,
    val lastUpdated: String
)

@Composable
fun MyClassesScreen(navController: NavController,userId:Int) {
    var isCameraOpen by remember { mutableStateOf(false) }
    val classrooms = remember { mutableStateOf<List<ClassItem>>(emptyList()) }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        coroutineScope.launch {
            RetrofitClient.apiService.getClassrooms(userId).enqueue(object :
                Callback<List<models.Classroom>> {
                override fun onResponse(
                    call: Call<List<models.Classroom>>,
                    response: Response<List<models.Classroom>>
                ) {
                    Log.d("Retrofit", "Response: ${response.body()}")
                    if (response.isSuccessful) {
                        classrooms.value = response.body()?.map {
                            ClassItem(
                                name = it.name,
                                studentsCount = it.student_count, // Example, adjust as needed
                                lastUpdated = "Yesterday" // Example, adjust as needed
                            )
                        } ?: emptyList()
                    } else {
                        errorMessage.value = "Error: ${response.message()}"
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<List<models.Classroom>>, t: Throwable) {
                    errorMessage.value = "API Failure: ${t.message}"
                    isLoading.value = false
                }
            })
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            if (isCameraOpen) {
                CameraView(
                    onClose = {
                        isCameraOpen = false
                        navController.navigate("studentList") // Navigation vers l'écran des étudiants
                    }
                )
            } else {
                // Top Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    // Search TextField with Icon
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = { Text("Search", color = Color.Gray, fontSize = 18.sp) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "Search Icon",
                                tint =  Color(0xFF2A454E),
                                modifier=Modifier.size(18.dp)
                            )
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(29.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.notification),
                        contentDescription = "Notification",
                        tint = Color(0xFF2A454E),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ellipse),
                        contentDescription = "Profile Image",
                        modifier = Modifier.size(40.dp)
                    )
                }

                // Title
                Text(
                    text = "My classes",
                    fontFamily = monaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFF2A454E),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // List of Classes
                classrooms.value.forEach { classItem ->
                    ClassCard(classItem,onScanClick = { isCameraOpen = true })
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun ClassCard(classItem: ClassItem,onScanClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(2.dp,Color(0xFF1B2E35)), // Border added
        modifier = Modifier
            .fillMaxWidth()
            .height(81.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Class Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = classItem.name,
                    fontFamily = monaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1B2E35)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Yellow and Brown Boxes
                // Yellow and Brown Boxes with Icons
                Row {
                    // Yellow Box with Students Icon
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFFFD700), RoundedCornerShape(18.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp).width(41.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.studentgraduate),
                                contentDescription = "Students Icon",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = classItem.studentsCount.toString(),
                                color = Color.White,
                                fontFamily = monaMedium,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Brown Box with Calendar Icon
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF665215), RoundedCornerShape(18.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp).width(106.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.calendar),
                                contentDescription = "Calendar Icon",
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = classItem.lastUpdated,
                                fontFamily = monaMedium,
                                color = Color.White,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            // Action Icon
            IconButton(onClick = { onScanClick() }) {
                Icon(
                    painter = painterResource(id = R.drawable.scan),
                    contentDescription = "Scan",
                    tint = Color(0xFF2A454E),
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun CameraView(onClose: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val coroutineScope = rememberCoroutineScope()
    var detectedStudents by remember { mutableStateOf<List<String>?>(null) }

    fun sendFrameToBackend(frame: ByteArray) {
        coroutineScope.launch(Dispatchers.IO) {
            try {
                val requestBody = MultipartBody.Part.createFormData(
                    "image",
                    "frame.jpg",
                    frame.toRequestBody("image/jpeg".toMediaTypeOrNull())
                )

                val response = RetrofitClient.apiService.detectStudent(requestBody)

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    detectedStudents = result?.detectedStudents
                } else {
                    println("Erreur serveur : ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println("Erreur réseau : ${e.message}")
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = androidx.camera.core.Preview.Builder().build()
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    val imageAnalysis = androidx.camera.core.ImageAnalysis.Builder()
                        .setBackpressureStrategy(androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()

                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(ctx),
                        { imageProxy ->
                            val buffer = imageProxy.planes[0].buffer
                            val bytes = ByteArray(buffer.remaining())
                            buffer.get(bytes)

                            sendFrameToBackend(bytes)

                            imageProxy.close()
                        }
                    )

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
                }, ContextCompat.getMainExecutor(ctx))
                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        // Bouton pour fermer la caméra
        IconButton(
            onClick = { onClose() },
            modifier = Modifier.align(Alignment.TopEnd).padding(16.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.scan),
                contentDescription = "Fermer la caméra",
                tint = Color.White
            )
        }

        // Afficher les résultats détectés
        detectedStudents?.let { students ->
            Column(modifier = Modifier.align(Alignment.Center)) {
                students.forEach { student ->
                    Text(text = "Étudiant détecté : $student", color = Color.White)
                }
            }
        }
    }
}

@Preview
@Composable
fun MyClassesScreenPreview() {
    //MyClassesScreen(2)
}