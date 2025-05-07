package com.example.bookproject

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.bookproject.DataClass.RetrofitClient
import com.example.bookproject.DataClass.Story
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

@Composable
fun AllStoriesPage(navController: NavController) {
    val allStories = remember { mutableStateListOf<Story>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Fetch all stories
        coroutineScope.launch {
            try {
                val all = RetrofitClient.instance.getStories()
                allStories.clear()
                allStories.addAll(all)
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "All Stories",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // LazyVerticalGrid to display items in 2 columns
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),  // Define 2 columns in the grid
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(allStories) { story ->
                    BookCard(story = story) {
                        navController.navigate("details/${story.id}")
                    }
                }
            }
        }
    }
}
