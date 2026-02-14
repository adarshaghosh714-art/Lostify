package com.example.lostify.navigation

import android.app.Application
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.lostify.data.HomeViewModel
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

        // ---------------- HOME SCREEN ----------------
        composable(route = NavRoutes.Home.route) {

            val homeViewModel: HomeViewModel = viewModel()
            val itemList by homeViewModel.items.collectAsState()

            HomeScreen(
                navController = navController,
                itemList = itemList
            )
        }

        // ---------------- ADD ITEM SCREEN ----------------
        composable(route = NavRoutes.AddItem.route) {

            val context = LocalContext.current
            val application = context.applicationContext as android.app.Application

            val addItemViewModel: AddItemViewModel = viewModel(
                factory = AddItemViewModelFactory(application)
            )

            AddItemScreen(
                navController = navController,
                viewModel = addItemViewModel
            )
        }

        // ---------------- DETAILS SCREEN ----------------
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
                navController = navController,
                itemId = itemId
            )
        }
    }
}
