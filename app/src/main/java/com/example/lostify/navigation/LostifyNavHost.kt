package com.example.lostify.navigation

import android.app.Application
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.lostify.data.LostItemViewModel
import com.example.lostify.ui.theme.*

@Composable
fun LostifyNavHost(
    isFirstLaunch: Boolean,
    isUserLoggedIn: Boolean,
    onOnboardingFinished: () -> Unit
) {

    val navController = rememberNavController()

    // Decide start destination
    val startDestination = when {
        isFirstLaunch -> NavRoutes.Onboarding.route
        isUserLoggedIn -> NavRoutes.Home.route
        else -> NavRoutes.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {


        composable(route = NavRoutes.Onboarding.route) {

            OnBoardingScreen(
                onGetStartedClick = {

                    onOnboardingFinished()

                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(NavRoutes.Onboarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }


        composable(route = NavRoutes.Login.route) {


            LoginScreen(
                navController = navController
            )
        }


        composable(route = NavRoutes.SignUp.route) {

            SignUpScreen(
                navController = navController
            )
        }

        composable(route = NavRoutes.Home.route) {

            val homeViewModel: LostItemViewModel = viewModel()
            val itemList by homeViewModel.items.collectAsState()

            HomeScreen(
                navController = navController,
                itemList = itemList
            )
        }


        composable(route = NavRoutes.AddItem.route) {

            val context = LocalContext.current
            val application = context.applicationContext as Application

            val addItemViewModel: AddItemViewModel = viewModel(
                factory = AddItemViewModelFactory(application)
            )

            AddItemScreen(
                navController = navController,
                viewModel = addItemViewModel
            )
        }


        composable(
            route = NavRoutes.Detail.route,
            arguments = listOf(
                navArgument("itemId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val itemId = backStackEntry.arguments?.getString("itemId")

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(NavRoutes.Home.route)
            }

            val sharedViewModel: LostItemViewModel = viewModel(parentEntry)

            if (itemId == null) {
                Text("Invalid item")
                return@composable
            }

            ItemDetailsScreen(
                navController = navController,
                itemId = itemId,
                viewModel = sharedViewModel
            )
        }
    }
}