package tn.esprit.videogen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import tn.esprit.videogen.network.generateVideo
import tn.esprit.videogen.screens.PromptInputScreen
import tn.esprit.videogen.screens.VideoListScreen
import tn.esprit.videogen.screens.VideoPlayerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    var videoUrls by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedVideoUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "prompt_screen") {
        composable("prompt_screen") {
            PromptInputScreen(onGenerateVideo = { prompt, onComplete ->
                generateVideo(
                    context = context,
                    prompt = prompt,
                    onSuccess = { url ->
                        videoUrls = videoUrls + url
                        onComplete()
                        navController.navigate("video_list_screen")
                    },
                    onError = { error ->
                        onComplete()
                        // Affichez une notification ou un message d'erreur ici si nécessaire
                    }
                )
            })
        }

        composable("video_list_screen") {
            VideoListScreen(
                videoUrls = videoUrls,
                onVideoSelected = { videoUrl ->
                    selectedVideoUrl = videoUrl
                    navController.navigate("video_player_screen")
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable("video_player_screen") {
            selectedVideoUrl?.let { videoUrl ->
                VideoPlayerScreen(videoUrl = videoUrl)
            } ?: navController.popBackStack()
        }
    }
}
