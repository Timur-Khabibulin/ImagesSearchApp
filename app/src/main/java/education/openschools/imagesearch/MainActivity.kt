package education.openschools.imagesearch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import education.openschools.imagesearch.ui.ImageDetailScreen
import education.openschools.imagesearch.ui.SearchScreen
import education.openschools.imagesearch.ui.SearchScreenViewModel
import education.openschools.imagesearch.ui.theme.ImageSearchTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImageSearchTheme {
                MainNavHost()
            }
        }
    }

    @Composable
    private fun MainNavHost() {
        val navController = rememberNavController()
        val viewModel: SearchScreenViewModel = hiltViewModel()
        NavHost(
            navController = navController,
            startDestination = "SearchScreen"
        ) {
            composable("SearchScreen") {
                SearchScreen.SearchScreen(
                    viewModel = viewModel,
                    onImageClick = {
                        navController.navigate("ImageDetailScreen/$it")
                    }
                )
            }
            composable(
                "ImageDetailScreen/{position}",
                arguments = listOf(navArgument("position") { type = NavType.IntType })
            ) { backStack ->
                val position = backStack.arguments?.getInt("position") ?: 0
                ImageDetailScreen.ImageDetailScreen(
                    viewModel = viewModel,
                    position = position,
                    onBack = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}
