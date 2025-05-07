package com.example.bookproject

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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

@Composable
fun LibraryPage(navController: NavController) {
    val recommendedBooks = remember { mutableStateListOf<Story>() }
    val continueReadingBooks = remember { mutableStateListOf<Story>() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        // Fetch recommended stories
        coroutineScope.launch {
            try {
                val recommended = RetrofitClient.instance.getRecommendedStories()
                recommendedBooks.clear()
                recommendedBooks.addAll(recommended)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Fetch liked stories
        coroutineScope.launch {
            try {
                val liked = RetrofitClient.instance.getStories()
                continueReadingBooks.clear()
                continueReadingBooks.addAll(liked)
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
                text = "Library",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Text(
                text = "Recommended ",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                color = Color(0xFFBB86FC),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            BookLazyRow(books = recommendedBooks) { selectedBook ->
                navController.navigate("details/${selectedBook.id}")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Row to place "Read" and "See All" on the same line, aligning vertically
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // Align vertically in the center
            ) {
                Text(
                    text = "Read",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                    color = Color(0xFFBB86FC),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // "See All" link on the right side
                Text(
                    text = "See All",
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                    color = Color(0xFFBB86FC),
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .clickable {
                            navController.navigate("allStories") // Navigate to the all stories screen
                        }
                )
            }

            // Display only the first 10 books from the continueReadingBooks list
            BookLazyRow(books = continueReadingBooks.take(10)) { selectedBook ->
                navController.navigate("details/${selectedBook.id}")
            }
        }
    }
}


@Composable
fun BookLazyRow(books: List<Story>, onBookClick: (Story) -> Unit) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(books) { book ->
            BookCard(story = book, onClick = { onBookClick(book) })
        }
    }
}

@Composable
fun BookCard(story: Story, onClick: () -> Unit) {
    var isFavorite by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() }
            .padding(8.dp)
            .background(Color(0xFF2D2A44), shape = RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray.copy(alpha = 0.5f))
        ) {
            val imageUrl = "http://10.0.2.2:3000${story.imageUrl}"

            val painter = rememberAsyncImagePainter(model = imageUrl)

            Image(
                painter = painter,
                contentDescription = "Story Cover",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Heart icon toggle
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .clickable { isFavorite = !isFavorite },
                tint = Color(0xFFFF4081) // Match the interface design with this color
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = story.title,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            maxLines = 2,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
