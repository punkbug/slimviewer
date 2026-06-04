package com.slimviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.slimviewer.core.DocumentEngineRouter
import com.slimviewer.core.DocumentMetadata
import com.slimviewer.core.DocumentMetadataResolver
import com.slimviewer.engine.hwp.HwpViewerEngine
import com.slimviewer.engine.office.OfficeViewerEngine
import com.slimviewer.feature.home.HomeScreen
import com.slimviewer.feature.viewer.ViewerScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SlimViewerApp()
                }
            }
        }
    }
}

@Composable
fun SlimViewerApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    // Simple state-based metadata passing for now (can be improved with navigation arguments if needed)
    var currentMetadata by remember { mutableStateOf<DocumentMetadata?>(null) }
    
    val router = remember {
        DocumentEngineRouter(
            listOf(
                OfficeViewerEngine(),
                HwpViewerEngine()
            )
        )
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onFileSelected = { uri ->
                    val metadata = DocumentMetadataResolver.resolve(context, uri)
                    if (metadata != null) {
                        currentMetadata = metadata
                        navController.navigate("viewer")
                    }
                }
            )
        }
        composable("viewer") {
            currentMetadata?.let { metadata ->
                ViewerScreen(
                    metadata = metadata,
                    router = router,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
