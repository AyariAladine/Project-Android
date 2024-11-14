package com.example.bookproject

import ConfirmCodeScreen
import ForgetPassword
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.bookproject.ui.theme.BookProjectTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BookProjectTheme {
AppNavigation()            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
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
        composable("home"){
            Home(navController)
        }
        composable("reset"){
            ResetPass(navController)
        }
    }
}