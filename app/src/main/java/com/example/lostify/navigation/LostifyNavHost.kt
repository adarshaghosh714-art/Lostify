package com.example.lostify.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lostify.data.LostItemRepository
import com.example.lostify.data.LostifyDatabase
import com.example.lostify.ui.theme.AddItemScreen
import com.example.lostify.ui.theme.AddItemViewModel
import com.example.lostify.ui.theme.AddItemViewModelFactory
import com.example.lostify.ui.theme.HomeScreen
import com.example.lostify.ui.theme.ItemDetailsScreen

@Composable
fun LostifyNavHost() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {


        composable(NavRoutes.Home.route) {
            HomeScreen(navController)
        }


        composable(NavRoutes.AddItem.route) {
        val context = LocalContext.current
            val db = LostifyDatabase.getDatabase(context)

            val repository = LostItemRepository(db.lostItemDao())

            val viewModel :AddItemViewModel = viewModel(
                factory = AddItemViewModelFactory(repository)
            )
            AddItemScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        composable(
            route = NavRoutes.Detail.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val itemId = backStackEntry.arguments?.getString("itemId")

            if (itemId == null) {

                Text("Invalid item")
                return@composable
            }

            ItemDetailsScreen(
                navController=navController,
                itemId = itemId)
        }

    }
}
