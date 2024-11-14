package com.example.bookproject

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookproject.DataClass.LoginRequest
import com.example.bookproject.DataClass.LoginResponse
import com.example.bookproject.DataClass.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
@Composable
fun Login(navController: NavController) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }

    val isEmailValid = remember { mutableStateOf(false) }
    val isPasswordValid = remember { mutableStateOf(false) }

    fun validateEmail(input: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()
    }

    fun validatePassword(input: String): Boolean {
        val passwordRegex = Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#\$%^&*]).{8,}$")
        return passwordRegex.matches(input)
    }

    val context = LocalContext.current

    val googleSignInClient: GoogleSignInClient = remember {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_CLIENT_ID") // Replace with your actual client ID
            .requestEmail()
            .build()
        GoogleSignIn.getClient(context, gso)
    }

    // Launcher for Google Sign-In
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    .getResult(ApiException::class.java)
                val displayName = account?.displayName
                Toast.makeText(context, "Connected with Google: $displayName", Toast.LENGTH_SHORT).show()
            } catch (e: ApiException) {
                Toast.makeText(context, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to launch Google Sign-In
    fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }


    fun performLogin(context: Context) {
        if (isEmailValid.value && isPasswordValid.value) {
            val loginRequest = LoginRequest(email.value, password.value)

            RetrofitClient.instance.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        val token = loginResponse?.accessToken
                        val userId = loginResponse?.userId
                        val sharedPreferences = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                        sharedPreferences.edit().putString("token", token).apply()
                        Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show()
                        navController.navigate("home")
                    } else {
                        Toast.makeText(context, "Login Failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    Toast.makeText(context, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(context, "Please enter valid email and password", Toast.LENGTH_SHORT).show()
        }
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

            Row { Image(painter = painterResource(id = R.drawable.feather), contentDescription = "") }

            Spacer(modifier = Modifier.height(20.dp))
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
                            modifier = Modifier
                                .size(30.dp)
                                .padding(top = 4.dp)
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

            Row(
                modifier = Modifier
                    .padding(10.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(10.dp)),
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
                        isError = !isPasswordValid.value && password.value.isNotEmpty(),
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
                            modifier = Modifier
                                .size(30.dp)
                                .padding(top = 4.dp)
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    performLogin(context) // Call the performLogin function
                }) {
                    Text(text = "Sign In")
                }
                Button(onClick = { navController.navigate("signup") }) {
                    Text(text = "Sign Up")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.Absolute.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Forgot Password?",
                    color = Color.LightGray,
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable { navController.navigate("forget") }
                )
                Image(
                    painter = painterResource(id = R.drawable.star),
                    contentDescription = "",
                    Modifier.size(60.dp)
                )
            }
            Row {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = "",
                    Modifier
                        .size(60.dp)
                        .clickable { signInWithGoogle() }
                )
            }
            }
        }
    }
