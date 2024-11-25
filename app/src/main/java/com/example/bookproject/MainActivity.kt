package com.example.bookproject

import ConfirmCodeScreen
import ForgetPassword
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.example.bookproject.ui.theme.BookProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            1
        )
        setContent {
            BookProjectTheme {
                AppNavigation()
            }
        }
    }
}





@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        bottomBar = {
            val currentRoute by navController.currentBackStackEntryAsState()
            if (currentRoute?.destination?.route in listOf("home", "listen", "watch", "profile", "Pen")) {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            composable("login") {
                Login(navController)
            }
            composable("signup") {
                SignUp(navController)
            }
            composable("forget") {
                ForgetPassword(navController)
            }
            composable("confirm") {
                ConfirmCodeScreen(navController)
            }
            composable("home") {
                LibraryPage(navController)
            }
            composable("listen") {
                ListenPage()
            }
            composable("watch") {
                WatchPage()
            }
            composable("profile") {
                ProfilePage()
            }
            composable("Pen") {
                Create(navController)
            }
            composable(
                "details/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val storyId = backStackEntry.arguments?.getString("id") ?: ""
                BookDetailsScreen(navController, storyId)
            }
            composable("allStories") {
                AllStoriesPage(navController)
            }

        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute by navController.currentBackStackEntryAsState()

    NavigationBar(
        containerColor = Color(0xFF1D182F), // Background color matching the app's theme
        contentColor = Color.Gray // White content for visibility
    ) {
        BottomNavItems.forEach { navItem ->
            NavigationBarItem(
                icon = {
                    // Determine whether to show a GIF (highlighted) or static drawable
                    if (currentRoute?.destination?.route == navItem.route) {
                        // Display highlighted GIF for the selected item
                        AsyncImage(
                            model = navItem.selectedGif,
                            contentDescription = navItem.title,
                            modifier = Modifier.size(28.dp)
                        )
                    } else {
                        // Display static drawable for the unselected item
                        Image(
                            painter = painterResource(id = navItem.unselectedGif),
                            contentDescription = navItem.title,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = navItem.title,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (currentRoute?.destination?.route == navItem.route) {
                                Color(0xFFBB86FC) // Highlight color for selected item
                            } else {
                                Color.LightGray // Default color for unselected items
                            }
                        )
                    )
                },
                selected = currentRoute?.destination?.route == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        // Avoid building up a large back stack
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Composable
fun ListenPage() {
    Text("Listen Page", modifier = Modifier.fillMaxSize(), style = MaterialTheme.typography.headlineMedium)
}

@Composable
fun WatchPage() {
    Text("Watch Page", modifier = Modifier.fillMaxSize(), style = MaterialTheme.typography.headlineMedium)
}

@Composable
fun ProfilePage() {
    Text("Profile Page", modifier = Modifier.fillMaxSize(), style = MaterialTheme.typography.headlineMedium)
}



// Data class for Bottom Navigation items
data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedGif: Int,   // Resource ID for selected GIF
    val unselectedGif: Int  // Resource ID for unselected GIF
)



// List of Bottom Navigation items
val BottomNavItems = listOf(
    BottomNavItem("Library", "home", R.drawable.openbook, R.drawable.openbook1),
    BottomNavItem("Listen", "listen", R.drawable.music, R.drawable.music1),
    BottomNavItem("Create", "Pen", R.drawable.quill, R.drawable.quill1),
    BottomNavItem("Watch", "watch", R.drawable.videocamera, R.drawable.videocamera1),
    BottomNavItem("Profile", "profile", R.drawable.user, R.drawable.user1)
)





