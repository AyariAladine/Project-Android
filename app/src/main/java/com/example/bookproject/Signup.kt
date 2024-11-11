package com.example.bookproject

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookproject.DataClass.RetrofitClient
import com.example.bookproject.DataClass.SignUpRequest
import com.example.bookproject.DataClass.SignUpResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.ui.platform.LocalContext
@Composable
fun SignUp(navController: NavController) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val age = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val isNameValid = remember { mutableStateOf(false) }
    val isEmailValid = remember { mutableStateOf(false) }
    val isAgeValid = remember { mutableStateOf(false) }
    val isPasswordValid = remember { mutableStateOf(false) }
    fun validateName(input: String): Boolean = input.isNotBlank()
    fun validateEmail(input: String): Boolean = android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
    fun validateAge(input: String): Boolean = input.toIntOrNull()?.let { it in 1..120 } == true
    fun validatePassword(input: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*]).{8,}$")
        return passwordRegex.matches(input)
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF1D182F))
                .padding(paddingValues)
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row {
                Image(
                    painter = painterResource(id = R.drawable.feather),
                    contentDescription = "",
                    modifier = Modifier.size(200.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            // Name Row
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Name:",
                    color = Color.White,
                    modifier = Modifier.width(130.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    TextField(
                        value = name.value,
                        onValueChange = {
                            name.value = it
                            isNameValid.value = validateName(it)
                        },
                        placeholder = { Text(text = "Name") },
                        isError = !isNameValid.value && name.value.isNotEmpty(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = if (isNameValid.value) Color.Green else Color.White,
                            unfocusedIndicatorColor = if (isNameValid.value) Color.Green else Color.LightGray
                        ),
                        modifier = Modifier
                            .width(330.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    if (isNameValid.value) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Valid name",
                            tint = Color.Green,
                            modifier = Modifier.size(24.dp).padding(top = 4.dp)
                        )
                    } else if (name.value.isNotEmpty()) {
                        Text(
                            text = "Name cannot be empty",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Email Row
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Email:",
                    color = Color.White,
                    modifier = Modifier.width(130.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    TextField(
                        value = email.value,
                        onValueChange = {
                            email.value = it
                            isEmailValid.value = validateEmail(it)
                        },
                        placeholder = { Text(text = "Email") },
                        isError = !isEmailValid.value && email.value.isNotEmpty(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = if (isEmailValid.value) Color.Green else Color.White,
                            unfocusedIndicatorColor = if (isEmailValid.value) Color.Green else Color.LightGray
                        ),
                        modifier = Modifier
                            .width(330.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    if (isEmailValid.value) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Valid email",
                            tint = Color.Green,
                            modifier = Modifier.size(24.dp).padding(top = 4.dp)
                        )
                    } else if (email.value.isNotEmpty()) {
                        Text(
                            text = "Invalid Email",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Age Row
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Age:",
                    color = Color.White,
                    modifier = Modifier.width(130.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    TextField(
                        value = age.value,
                        onValueChange = {
                            age.value = it
                            isAgeValid.value = validateAge(it)
                        },
                        placeholder = { Text(text = "Age") },
                        isError = !isAgeValid.value && age.value.isNotEmpty(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = if (isAgeValid.value) Color.Green else Color.White,
                            unfocusedIndicatorColor = if (isAgeValid.value) Color.Green else Color.LightGray
                        ),
                        modifier = Modifier
                            .width(330.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    if (isAgeValid.value) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Valid age",
                            tint = Color.Green,
                            modifier = Modifier.size(24.dp).padding(top = 4.dp)
                        )
                    } else if (age.value.isNotEmpty()) {
                        Text(
                            text = "Enter a valid age (1-120)",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Password Row
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Password:",
                    color = Color.White,
                    modifier = Modifier.width(130.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    TextField(
                        value = password.value,
                        onValueChange = {
                            password.value = it
                            isPasswordValid.value = validatePassword(it)
                        },
                        placeholder = { Text(text = "Password") },
                        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible.value = !passwordVisible.value
                            }) {
                                Icon(
                                    imageVector = if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (passwordVisible.value) "Hide password" else "Show password"
                                )
                            }
                        },
                        isError = !isPasswordValid.value && password.value.isNotEmpty(),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = if (isPasswordValid.value) Color.Green else Color.White,
                            unfocusedIndicatorColor = if (isPasswordValid.value) Color.Green else Color.LightGray
                        ),
                        modifier = Modifier
                            .width(330.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    if (isPasswordValid.value) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = "Valid password",
                            tint = Color.Green,
                            modifier = Modifier.size(24.dp).padding(top = 4.dp)
                        )
                    } else if (password.value.isNotEmpty()) {
                        Text(
                            text = "Invalid Password.",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.padding(20.dp))
            val context = LocalContext.current
            Button(onClick = {
                if (isEmailValid.value && isPasswordValid.value) {
                    signUpUser(
                        name.value,
                        email.value,
                        password.value,
                        context, // Pass the current context
                        navController
                    )
                } else {
                    Toast.makeText(context, "Please fill in all fields correctly", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text(text = "Sign Up")
            }
        }
    }
}
fun signUpUser(name:String,email: String, password: String, context: Context, navController: NavController) {
    val request = SignUpRequest(name,email, password) // No name or age

    RetrofitClient.instance.signUp(request).enqueue(object : Callback<SignUpResponse> {
        override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.success == true) {
                    Toast.makeText(context, "Sign up failed:", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(context, "Sign up successful! ${body?.message}", Toast.LENGTH_LONG).show()
                    navController.navigate("login")
                }
            } else {
                Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
            Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_LONG).show()
        }
    })
}
