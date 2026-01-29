package com.example.lostify.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lostify.ui.theme.AddItemScreen
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
            AddItemScreen(navController)
        }


        composable(
            route = NavRoutes.Detail.route,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments
                ?.getString("itemId")

            ItemDetailsScreen(itemId = itemId)
        }
    }
}
