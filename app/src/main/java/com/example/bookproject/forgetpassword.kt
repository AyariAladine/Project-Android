import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bookproject.DataClass.ForgotPasswordRequest
import com.example.bookproject.DataClass.ForgotPasswordResponse
import com.example.bookproject.DataClass.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ForgetPassword(navController: NavController) {
    val email = remember { mutableStateOf("") }
    val isEmailValid = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    fun validateEmail(input: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding()
    ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(color = Color(0xFF1D182F)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enter your email to reset your password",
                    color = Color(0xFFE1E1E1),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                        isEmailValid.value = validateEmail(it)
                    },
                    placeholder = { Text("Email") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = if (isEmailValid.value) Color.Green else Color.White,
                        unfocusedIndicatorColor = if (isEmailValid.value) Color.Green else Color.LightGray
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .padding(bottom = 20.dp)
                )

                Button(
                    onClick = {
                        if (isEmailValid.value) {
                            coroutineScope.launch(Dispatchers.IO) {
                                val request = ForgotPasswordRequest(email.value)
                                RetrofitClient.instance.forgotPassword(request).enqueue(object : Callback<ForgotPasswordResponse> {
                                    override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                                        if (response.isSuccessful) {

                                        } else {
                                            println("Error: ${response.errorBody()?.string()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable) {
                                        println("Failure: ${t.message}")
                                    }
                                })
                            }
                        }
                        navController.navigate("confirm")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D5FB2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Password", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        }

}
