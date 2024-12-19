package com.example.absencemanager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.absencemanager.R

val monaSans = FontFamily(Font(R.font.mona_sans_bold))
val monaMedium = FontFamily(Font(R.font.mona_sans_medium))

@Composable
fun SplashScreen(onGetStartedClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().height(917.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(top = 110.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.graduate_student),
                contentDescription = null,
                modifier = Modifier.height(468.dp).fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Welcome to Absence Manager!",
                fontSize = 23.sp,
                fontFamily = monaSans,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF1B2E35),
                modifier = Modifier.width(400.dp).height(23.dp).padding(horizontal = 3.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Track student attendance and measure their motivation in real-time with just a camera.",
                fontSize = 16.sp,
                fontFamily = monaMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color(0xFF7C7C7C),
                modifier = Modifier.height(52.dp).width(370.dp)
            )

            Spacer(modifier = Modifier.height(38.dp))

            Button(
                onClick = onGetStartedClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF665215),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.height(56.dp).fillMaxWidth(0.8f)
            ) {
                Text(
                    text = "Get started",
                    fontFamily = monaSans,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(onGetStartedClick = {})
}