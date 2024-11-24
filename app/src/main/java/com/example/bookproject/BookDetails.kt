package com.example.bookproject


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookproject.DataClass.RetrofitClient
import com.example.bookproject.DataClass.Story
import kotlinx.coroutines.launch

@Composable
fun BookDetailsScreen(navController: NavController, storyId: String) {
    val story = remember { mutableStateOf<Story?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(storyId) {
        coroutineScope.launch {
            try {
                val fetchedStory = RetrofitClient.instance.getStoryDetails(storyId) // API call for story details
                story.value = fetchedStory
            } catch (e: Exception) {
                // Handle error
                e.printStackTrace()
            }
        }
    }

    story.value?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color(0xFF1D182F))
                .padding(16.dp)
        ) {
            Text(
                text = it.title,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            AsyncImage(
                model = it.imageUrl,
                contentDescription = "Story Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.5f))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = it.content, // Full content fetched from the API
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                color = Color.White
            )
        }
    } ?: run {
        // Show loading state
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading...", color = Color.White)
        }
    }
}
