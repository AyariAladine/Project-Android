package tn.esprit.videogen.network

import android.content.Context
import android.util.Log
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

fun generateVideo(context: Context, prompt: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
    val url = "http://10.0.2.2:5000/generate_video" // URL pour l'accès au backend via l'émulateur
    val jsonBody = JSONObject().apply {
        put("prompt", prompt)
    }

    val request = JsonObjectRequest(
        Request.Method.POST, url, jsonBody,
        { response ->
            Log.d("GenerateVideo", "Response: $response") // Log pour vérifier la réponse
            val videoUrl = response.optString("video_url", null)
            if (videoUrl != null) {
                onSuccess(videoUrl)
            } else {
                onError("No video URL in response")
            }
        },
        { error ->
            val errorMessage = when (error) {
                is com.android.volley.TimeoutError -> "Request timed out"
                is com.android.volley.NoConnectionError -> "No internet connection"
                is com.android.volley.ServerError -> "Server error"
                else -> "Unknown error: ${error.message}"
            }
            Log.e("GenerateVideo", "Error: $errorMessage") // Log d'erreur pour faciliter le débogage
            onError(errorMessage)
        }
    ).apply {
        retryPolicy = DefaultRetryPolicy(
            120000, // 120 seconds timeout
            1, // Number of retries
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
    }

    // Ajout de la requête à la file d'attente de Volley
    Volley.newRequestQueue(context).add(request)
}
