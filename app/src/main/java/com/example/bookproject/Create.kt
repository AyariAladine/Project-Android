package com.example.bookproject

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Create() {
    // Define the background color
    val backgroundColor = Color(0xFF1D182F)

    // Main UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Write Your Story",
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            TextField(
                value = "",
                onValueChange = { /* Update the value here */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp) // Enough space for typing
                    .clip(RoundedCornerShape(12.dp)) // Rounded corners
                    .background(Color(0xFF282040)), // Lighter shade for the text box
                placeholder = {
                    Text(
                        text = "Start typing your story here...",
                        color = Color.Gray
                    )
                },
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
                singleLine = false,
                maxLines = Int.MAX_VALUE // Allow multiline text
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookAppEditor() {
    Create()
}
