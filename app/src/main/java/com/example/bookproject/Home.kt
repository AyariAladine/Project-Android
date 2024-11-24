package com.example.bookproject

import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.bookproject.DataClass.RetrofitClient
import com.example.bookproject.DataClass.Story
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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
                // Handle error
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
                // Handle error
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

            Text(
                text = "Read",
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                color = Color(0xFFBB86FC),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            BookLazyRow(books = continueReadingBooks) { selectedBook ->
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
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Gray.copy(alpha = 0.5f))
        ) {
            val context = LocalContext.current

            // Construct the image URL from your local server (e.g., localhost or a local IP)
            val imageUrl = "http://10.0.2.2:3000${story.imageUrl}"

            Log.d("ImageUrl", "Using URL: $imageUrl")

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
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = story.title,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 14.sp),
            maxLines = 2,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}