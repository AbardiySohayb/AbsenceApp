    package com.example.absencemanager
    import android.content.Context
    import android.content.SharedPreferences
    import android.util.Log
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.border
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material3.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.font.Font
    import androidx.compose.ui.text.font.FontFamily
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.text.input.PasswordVisualTransformation
    import androidx.compose.ui.text.input.VisualTransformation
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import com.example.absencemanager.R



    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response

    @Composable
    fun LoginScreen(
        onLoginSuccess: (Int) -> Unit // Callback pour rediriger en cas de succès
    ) {
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val errorMessage = remember { mutableStateOf("") } // Pour afficher les messages d'erreur

        Surface(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            color = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 32.dp)
                    .padding(top = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Illustration
                Image(
                    painter = painterResource(id = R.drawable.professor_image),
                    contentDescription = null,
                    modifier = Modifier.width(299.dp).height(225.dp)
                )

                Spacer(modifier = Modifier.height(38.dp))

                // Titre
                Text(
                    text = "Welcome Back Dear Professor !",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B2E35),
                    modifier = Modifier.width(368.dp).height(23.dp)
                )

                Spacer(modifier = Modifier.height(33.dp))

                // Instructions
                Text(
                    text = "Please enter your academic email and password.",
                    fontSize = 16.sp,
                    color = Color(0xFF8C8C8C),
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Champ email
                TextFieldWithIcon(
                    placeholder = "Academic email",
                    iconId = R.drawable.email_icon,
                    value = username.value,
                    onValueChange = { username.value = it }
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Champ mot de passe
                TextFieldWithIcon(
                    placeholder = "Password",
                    iconId = R.drawable.password_icon,
                    isPassword = true,
                    value = password.value,
                    onValueChange = { password.value = it }
                )

                Spacer(modifier = Modifier.height(33.dp))

                // Bouton de connexion
                Button(
                    onClick = {
                        performLogin(
                            username.value,
                            password.value,
                            onLoginSuccess,
                            errorMessage
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF665215),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.height(56.dp).width(348.dp)
                ) {
                    Text(
                        text = "Login",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Affichage des erreurs
                if (errorMessage.value.isNotEmpty()) {
                    Text(
                        text = errorMessage.value,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

    @Composable
    fun TextFieldWithIcon(
        placeholder: String,
        iconId: Int,
        isPassword: Boolean = false,
        value: String,
        onValueChange: (String) -> Unit
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF8C8C8C)
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = Color(0xFF2A454E)
                )
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = Color(0xFF2A454E),
                    shape = RoundedCornerShape(14.dp)
                )
                .height(59.dp)
                .width(348.dp),
            shape = RoundedCornerShape(14.dp)
        )
    }


    fun performLogin(
        username: String,
        password: String,
        onLoginSuccess: (userId: Int) -> Unit,  // Ajouter un paramètre pour transmettre l'ID utilisateur
        errorMessage: MutableState<String>
    ) {
        val apiService = RetrofitClient.apiService
        val loginRequest = models.LoginRequest(username = username, password = password)

        apiService.login(loginRequest).enqueue(object : Callback<models.LoginResponse> {
            override fun onResponse(
                call: Call<models.LoginResponse>,
                response: Response<models.LoginResponse>
            ) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse?.message == "Login successful") {
                        // Passer l'ID de l'utilisateur à la fonction de succès
                        onLoginSuccess(loginResponse.user_id)
                    } else {
                        errorMessage.value = "Login failed. ${loginResponse?.error ?: ""}"
                    }
                } else {
                    errorMessage.value = "Invalid username or password."
                }
            }

            override fun onFailure(call: Call<models.LoginResponse>, t: Throwable) {
                errorMessage.value = "Network error: ${t.message}"
            }
        })
    }

    @Preview
    @Composable
    fun LoginScreenPreview() {
        var userId by remember { mutableStateOf<Int?>(null) }




            LoginScreen(onLoginSuccess = { id -> userId = id })
            Log.d("LoginScreenPreview", "Current userId: $userId")

    }
