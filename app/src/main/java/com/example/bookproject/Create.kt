package com.example.bookproject

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.navigation.NavController
import com.example.bookproject.DataClass.RetrofitClient
import com.example.bookproject.DataClass.Story
import com.example.bookproject.DataClass.StoryRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import android.util.Log
import androidx.compose.ui.draw.shadow

@Composable
fun Create(navController: NavController) {
    var storyInput by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val backgroundColor = Color(0xFF1D182F)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        if (isLoading) {
            // Loading state with blur effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x99000000)) // semi-transparent dark overlay
            ) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Enhanced header image
                Image(
                    painter = painterResource(id = R.drawable.create),
                    contentDescription = "Header Image",
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .size(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )

                // Improved title with larger font size
                Text(
                    text = "Write Your Story",
                    color = Color.White,
                    style = TextStyle(fontSize = 26.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Enhanced text field with padding and shadow
                TextField(
                    value = storyInput,
                    onValueChange = { storyInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF282040))
                        .padding(16.dp)
                        .shadow(4.dp, RoundedCornerShape(12.dp)),
                    placeholder = {
                        Text(text = "Start typing your story here...", color = Color.Gray)
                    },
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                    singleLine = false,
                    maxLines = Int.MAX_VALUE
                )

                // Button with shadow and hover effect
                Button(
                    onClick = {
                        if (storyInput.isNotEmpty()) {
                            Log.d("Create", "User input: $storyInput")
                            isLoading = true
                            errorMessage = null
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    Log.d("Create", "Sending request to server...")
                                    val response = RetrofitClient.instance.createStory(StoryRequest(storyInput))
                                    Log.d("Create", "Server response: $response")
                                    launch(Dispatchers.Main) {
                                        if (response.error != null) {
                                            errorMessage = response.error
                                            Log.e("Create", "Error in response: ${response.error}")
                                        } else {
                                            response.story?.let {
                                                Log.d("Create", "Navigating to story details with ID: ${it.id}")
                                                navController.navigate("details/${it.id}")
                                            }
                                        }
                                        isLoading = false
                                    }
                                } catch (e: Exception) {
                                    Log.e("Create", "Connection error: ${e.message}", e)
                                    launch(Dispatchers.Main) {
                                        errorMessage = "Failed to connect: ${e.message}"
                                        isLoading = false
                                    }
                                }
                            }
                        } else {
                            Log.w("Create", "User tried to submit an empty input!")
                        }
                    },
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF5A4B8C))
                        .shadow(6.dp, RoundedCornerShape(12.dp)),
                    colors = ButtonDefaults.buttonColors(Color(0xFF5A4B8C))
                ) {
                    Text(text = "Make Your Story", color = Color.White, style = TextStyle(fontSize = 18.sp))
                }

                // Display error message
                if (errorMessage != null) {
                    Log.e("Create", "Error message displayed: $errorMessage")
                    Text(
                        text = errorMessage ?: "Unknown error",
                        color = Color.Red,
                        modifier = Modifier.padding(top = 16.dp),
                        style = TextStyle(fontSize = 16.sp)
                    )
                }
            }
        }
    }
}
