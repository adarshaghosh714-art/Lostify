package com.example.lostify.navigation

sealed class NavRoutes(val route: String) {

    object Login : NavRoutes("login")

    object Onboarding : NavRoutes("onboarding")

    object SignUp : NavRoutes("signup")

    object Home : NavRoutes("home")

    object AddItem : NavRoutes("add")

    object Detail : NavRoutes("detail/{itemId}") {

        fun passItemId(itemId: String): String {
            return "detail/$itemId"
        }

    }
}