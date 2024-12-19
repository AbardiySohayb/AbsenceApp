package com.example.absencemanager
import androidx.compose.foundation.BorderStroke
import com.example.absencemanager.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign


data class Student(val name: String, val status: String, val color: Color)

val students = listOf(
    Student("Moulay Noureddine", "Absent", Color(0xFF912039)),
    Student("Moulay Noureddine", "Present", Color(0xFF54E67E)),
    Student("Moulay Noureddine", "Present", Color(0xFF54E67E)),
    Student("Moulay Noureddine", "Absent", Color(0xFF912039))
)

@Composable
fun StudentListScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar with Search, Notification, and Profile
        TopSection(searchQuery, onSearchChange = { searchQuery = it })
        Spacer(modifier = Modifier.height(16.dp))

        // Title
        Text(
            text = "My students",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2A454E)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // List of students
        Column {
            students.filter { it.name.contains(searchQuery, ignoreCase = true) }.forEach { student ->
                StudentItem(student)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
@Composable
fun TopSection(searchQuery: String, onSearchChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search TextField
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search") },
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .weight(1f)
                .height(22.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Notification Icon PNG
        Image(
            painter = painterResource(id = R.drawable.notification), // Remplace par ton icône PNG
            contentDescription = "Notifications",
            modifier = Modifier
                .size(36.dp)
                .clickable { /* Handle Notification Click */ }
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Profile Placeholder Image PNG
        Image(
            painter = painterResource(id = R.drawable.ellipse), // Remplace par ton icône PNG
            contentDescription = "Profile",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .clickable { /* Handle Profile Click */ }
        )
    }
}


@Composable
fun StudentItem(student: Student) {
    Card(
        shape = RoundedCornerShape(17.dp),
        border = BorderStroke(2.dp,Color(0xFF1B2E35)), // Border added
        modifier = Modifier
            .fillMaxWidth()
            .clickable{ /* Handle item click */ }.height(81.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Image Placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Student Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = student.name,
                    fontFamily = monaSans,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(student.color)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = student.status,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            // View Details Button
            Button(
                modifier = Modifier
                    .height(25.dp),
                onClick = { /* Handle details click */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B6C33))
            ) {
                Text(
                    text = "Details",
                    color = Color.White,
                    fontSize = 10.sp
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewStudentListScreen() {
    StudentListScreen()
}