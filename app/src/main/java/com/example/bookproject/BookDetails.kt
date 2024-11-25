package com.example.bookproject


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
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
            // Title
            Text(
                text = it.title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.dancingscriptregular)) // Custom calligraphic font
                ),
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            val imageUrl = "http://10.0.2.2:3000${it.imageUrl}"

            // Load the image using Coil
            val painter = rememberAsyncImagePainter(
                model = imageUrl,  // The URL pointing to the image
                placeholder = painterResource(id = R.drawable.books),  // Placeholder image while loading
                error = painterResource(id = R.drawable.book),  // Error image in case of failure
                fallback = painterResource(id = R.drawable.books)  // Fallback image if URL is null or empty
            )
            Image(
                painter = painter,
                contentDescription = "Story Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shadow(10.dp, RoundedCornerShape(16.dp))  // Add shadow for a nice lift effect
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Convert story content into paragraphs
            val paragraphs = it.content.split("\n")

            // State to track visibility of paragraphs
            val lazyListState = rememberLazyListState()

            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(paragraphs) { paragraph ->
                    // Track the position of each item
                    var isVisible = remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp) // Increased vertical padding for better spacing
                            .onGloballyPositioned { coordinates ->
                                val itemTop = coordinates.positionInRoot().y
                                val itemBottom = itemTop + coordinates.size.height
                                val visibleRegionTop = lazyListState.firstVisibleItemIndex
                                val visibleRegionBottom =
                                    lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.offset?.plus(
                                        lazyListState.layoutInfo.viewportSize.height
                                    ) ?: 0

                                // Check if the item is fully within the viewport
                                isVisible.value = itemTop >= 0 && itemBottom <= visibleRegionBottom
                            }
                    ) {
                        // Dynamically change text color based on visibility
                        Text(
                            text = paragraph,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.dancingscriptregular)), // Custom calligraphic font
                                fontSize = 30.sp,
                                lineHeight = 25.sp,
                                letterSpacing = 0.7.sp
                            ),
                            color = if (isVisible.value) Color.White else Color.Gray.copy(alpha = 0.6f),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    } ?: run {
        // Show loading state if story is not fetched yet
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Loading...", color = Color.White, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
