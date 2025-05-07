import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.bookproject.DataClass.RetrofitClient
import com.example.bookproject.DataClass.VerifyCodeRequest
import com.example.bookproject.DataClass.VerifyCodeResponse

@Composable
fun ConfirmCodeScreen(navController: NavController) {
    var email by remember { mutableStateOf("") } // For the email input
    var code by remember { mutableStateOf("") }  // For the code input
    var isLoading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    // Logs to monitor changes in email and code inputs
    Scaffold(
        modifier = Modifier.systemBarsPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color(0xFF1D182F)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "Enter your Email and Verification Code",
                modifier = Modifier.padding(bottom = 16.dp),
                color = Color.White
            )

            // Email TextField
            OutlinedTextField(
                value = email,
                onValueChange = { newEmail ->
                    Log.d(
                        "ConfirmCodeScreen",
                        "Email changed to: $newEmail"
                    ) // Log email input changes
                    email = newEmail
                },
                label = { Text("Email", color = Color.White) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth(),
                isError = email.isEmpty(),
                textStyle = LocalTextStyle.current.copy(color = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Code TextField (4-digit verification code)
            OutlinedTextField(
                value = code,
                onValueChange = { newCode ->
                    Log.d(
                        "ConfirmCodeScreen",
                        "Code changed to: $newCode"
                    ) // Log code input changes
                    if (newCode.length <= 4) {
                        code = newCode
                    }
                },
                label = { Text("Verification Code", color = Color.White) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                isError = code.length != 4,  // Only allow 4 digits
                textStyle = LocalTextStyle.current.copy(color = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Verify Button
            Button(
                onClick = {
                    Log.d(
                        "ConfirmCodeScreen",
                        "Verification button clicked with Email: $email, Code: $code"
                    ) // Log button click
                    if (email.isNotEmpty() && code.length == 4) {
                        isLoading = true
                        Log.d(
                            "ConfirmCodeScreen",
                            "Sending API request with Email: $email, Code: $code"
                        )
                        // Make API call to verify email and code
                        coroutineScope.launch {
                            RetrofitClient.instance.verifyResetCode(VerifyCodeRequest(email, code))
                                .enqueue(object : Callback<VerifyCodeResponse> {
                                    override fun onResponse(
                                        call: Call<VerifyCodeResponse>,
                                        response: Response<VerifyCodeResponse>
                                    ) {
                                        isLoading = false
                                        if (response.isSuccessful) {
                                            val responseBody = response.body()
                                            if (responseBody != null) {
                                                if (responseBody.message == "Reset code verified") {
                                                    // Success: reset code verified
                                                    message = "Code verified successfully!"
                                                    navController.navigate("reset")  // Navigate to reset password screen
                                                } else {
                                                    // Failure: specific message from backend (e.g., invalid reset code)
                                                    message =
                                                        "Verification failed: ${responseBody.message}"
                                                }
                                            }
                                        } else {
                                            // API error: something went wrong with the server request
                                            message = "Error: ${response.message()}"
                                        }
                                    }

                                    override fun onFailure(
                                        call: Call<VerifyCodeResponse>,
                                        t: Throwable
                                    ) {
                                        isLoading = false
                                        message = "Error: ${t.localizedMessage}"
                                    }
                                })
                        }
                    } else {
                        message = "Please enter both your email and a 4-digit code."
                    }
                },
                enabled = email.isNotEmpty() && code.length == 4 && !isLoading // Ensure button is only enabled if fields are filled
            ) {
                Text(text = if (isLoading) "Verifying..." else "Verify Code", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Display message after verification
            if (message.isNotEmpty()) {
                Text(text = message, color = Color.White)
            }
        }
    }
}
