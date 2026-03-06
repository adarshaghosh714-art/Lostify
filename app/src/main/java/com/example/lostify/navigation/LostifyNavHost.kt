
package com.example.lostify.navigation


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.lostify.ui.screens.ItemDetailsScreen

import com.example.lostify.ui.theme.*
import com.example.lostify.data.LostItemViewModel


@Composable
fun LostifyNavHost(
    isFirstLaunch: Boolean,
    onOnboardingFinished: () -> Unit
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = if (isFirstLaunch)
            NavRoutes.Onboarding.route
        else
            NavRoutes.Home.route
    ) {

        // ---------------- ONBOARDING ----------------
        composable(route = NavRoutes.Onboarding.route) {

            OnBoardingScreen(
                onGetStartedClick = {

                    onOnboardingFinished()

                    navController.navigate(NavRoutes.Home.route) {
                        popUpTo(NavRoutes.Onboarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        // ---------------- HOME ----------------
        composable(route = NavRoutes.Home.route) {

            val homeViewModel: LostItemViewModel = viewModel()
            val itemList by homeViewModel.items.collectAsState()

            HomeScreen(
                navController = navController,
                itemList = itemList
            )
        }

        // ---------------- ADD ITEM ----------------
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

        // ---------------- DETAILS ----------------
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

