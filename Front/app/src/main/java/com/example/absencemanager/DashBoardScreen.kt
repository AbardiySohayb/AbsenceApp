package com.example.absencemanager

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.absencemanager.layout.monasemi
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

@Composable
fun DashBoardScreen(userId: Int) {
    val dashboardData = remember { mutableStateOf<models.DashboardData?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        isLoading.value = true
        try {
            RetrofitClient.apiService.getDashboardData(userId).enqueue(object : Callback<models.DashboardData> {
                override fun onResponse(call: Call<models.DashboardData>, response: Response<models.DashboardData>) {
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()
                        Log.d("API Response", "Attribute1: ${data?.studentsCount}, Attribute2: ${data?.classesCount}")
                        dashboardData.value = data
                    } else {
                        Log.e("API Error", "Code: ${response.code()}, Message: ${response.message()}")
                        errorMessage.value = "Error: ${response.message()}"
                    }
                    isLoading.value = false
                }

                override fun onFailure(call: Call<models.DashboardData>, t: Throwable) {
                    Log.e("API Failure", "Message: ${t.message}")
                    errorMessage.value = "API Failure: ${t.message}"
                    isLoading.value = false
                }
            })
        } catch (e: Exception) {
            Log.e("Exception", e.message.toString())
            errorMessage.value = "Unexpected error occurred."
            isLoading.value = false
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
            .padding(16.dp)
    ) {
        DashboardHeader()
        Spacer(modifier = Modifier.height(24.dp))

        when {
            isLoading.value -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            errorMessage.value.isNotEmpty() -> {
                Text(
                    text = errorMessage.value,
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            dashboardData.value != null -> {
                StatisticsRow(dashboardData.value!!)
                Spacer(modifier = Modifier.height(12.dp))
                AttendanceRateCard(dashboardData.value!!.attendanceRate.toString())
                AttendanceIllustration()
                StartAttendanceButton()
            }
            else -> {
                Text(
                    text = "No data available",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

// Other Composables...

@Composable
fun DashboardHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Good Morning, Prof Othmane!",
                fontSize = 16.sp,
                fontFamily = monaSans,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1B2E35)
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Ready to take attendance?",
                fontSize = 16.sp,
                fontFamily = monasemi,
                color = Color(0xFF1B2E35)
            )
        }

        // Notification Icon
        Icon(
            painter = painterResource(id = R.drawable.notification),
            contentDescription = "Notification Icon",
            tint = Color(0xFF1B2E35),
            modifier = Modifier
                .size(32.dp)
                .clickable { /* Add Notification Action Here */ }
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Profile Placeholder
        Box(
            modifier = Modifier
                .size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ellipse),
                contentDescription = "Profile Placeholder",
                tint = Color.Unspecified,
                modifier = Modifier.size(66.dp)
            )
        }
    }
}

@Composable
fun StatisticsRow(dashboardData: models.DashboardData) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Add space between cards
    ) {
        StatisticsCard(
            title = "Classrooms",
            value = dashboardData.classesCount.toString(),
            icon = R.drawable.education,
            backgroundColor = Color(0xFF324755)
        )
        StatisticsCard(
            title = "Students",
            value = dashboardData.studentsCount.toString(),
            icon = R.drawable.studentgraduate,
            backgroundColor = Color(0xFFFFC107)
        )
    }
}

@Composable
fun StatisticsCard(title: String, value: String, icon: Int, backgroundColor: Color) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier.size(width = 184.dp, height = 78.dp).padding(0.dp)
    ) {
        Column(
            modifier = Modifier
                .height(78.dp).width(186.dp)
                .padding(12.dp),
        ) {
            // Title at the top
            Text(
                text = title,
                fontSize = 14.sp,
                fontFamily = monaSans,
                lineHeight = 10.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Icon and number horizontally aligned
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontFamily = monaMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun AttendanceRateCard(attendanceRate: String) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF665215)),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            // Title at the top
            Text(
                text = "Attendance Rate This Week:",
                fontSize = 16.sp,
                fontFamily = monaSans,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()){
                // Icon and Rate
                Icon(
                    painter = painterResource(id = R.drawable.analytics),
                    contentDescription = "Analytics Icon",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "$attendanceRate%",
                    fontSize = 28.sp,
                    fontFamily= monasemi,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    modifier=Modifier.padding(6.dp)
                )
            }
        }
    }
}

@Composable
fun AttendanceIllustration() {
    Image(
        painter = painterResource(id = R.drawable.scanstudent),
        contentDescription = "Attendance Illustration",
        modifier = Modifier,
        contentScale=ContentScale.FillWidth
    )
}

@Composable
fun StartAttendanceButton() {
    Button(
        onClick = { /* Action to Start Attendance */ },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF324755)),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .width(370.dp)
            .height(67.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.camera),
                contentDescription = "Scan Icon",
                tint = Color.White,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Start Attendance Scan",
                fontSize = 20.sp,
                fontFamily = monasemi,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
fun DashBoardScreenPreview() {
    DashBoardScreen(3)
}
