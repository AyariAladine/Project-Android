package com.example.bookproject

import ConfirmCodeScreen
import ForgetPassword
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Headphones
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Tv
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
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
                Create()
            }
            composable(
                "details/{id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) { backStackEntry ->
                val storyId = backStackEntry.arguments?.getString("id") ?: ""
                BookDetailsScreen(navController ,storyId = storyId)
            }

        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute by navController.currentBackStackEntryAsState()

    NavigationBar(
        containerColor = Color(0xFF1D182F), // Background color matching the app's theme
        contentColor = Color.White // White content for visibility
    ) {
        BottomNavItems.forEach { navItem ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (currentRoute?.destination?.route == navItem.route) {
                            navItem.selectedIcon
                        } else {
                            navItem.unselectedIcon
                        },
                        contentDescription = navItem.title,
                        modifier = Modifier.size(28.dp) // Icon size for better visibility
                    )
                },
                label = {
                    Text(
                        navItem.title,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold, // Bold font for the labels
                            color = if (currentRoute?.destination?.route == navItem.route) Color(0xFF76FF03) else Color.LightGray
                        )
                    )
                },
                selected = currentRoute?.destination?.route == navItem.route,
                onClick = {
                    navController.navigate(navItem.route) {
                        // To avoid building up a large back stack
                        launchSingleTop = true
                        restoreState = true
                    }
                },
             // Light gray for unselected items
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
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

// List of Bottom Navigation items
val BottomNavItems = listOf(
    BottomNavItem("Library", "home", Icons.Filled.Book, Icons.Outlined.Book),
    BottomNavItem("Listen", "listen", Icons.Filled.Headphones, Icons.Outlined.Headphones),
    BottomNavItem("Create", "Pen", Icons.Filled.Create, Icons.Outlined.Create),
    BottomNavItem("Watch", "watch", Icons.Filled.Tv, Icons.Outlined.Tv),
    BottomNavItem("Profile", "profile", Icons.Filled.Person, Icons.Outlined.Person)
)




