package com.example.bookproject

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

@Composable
fun ForgetPassword(onDismissRequest: () -> Unit) {
    val email = remember { mutableStateOf("") }
    val isEmailValid = remember { mutableStateOf(false) }
    fun validateEmail(input: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(
                text = "Forgot Password?",
                color = Color(0xFFE1E1E1),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
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
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Handle password reset logic here
                    onDismissRequest()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7D5FB2))
            ) {
                Text("Reset Password", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Cancel", color = Color(0xFFB0B0B0))
            }
        },
        containerColor = Color(0xFF2D2A40),
        shape = RoundedCornerShape(12.dp)
    )
}
