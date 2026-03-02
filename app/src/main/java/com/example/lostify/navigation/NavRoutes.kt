package com.example.lostify.navigation

sealed class NavRoutes(val route: String) {
object Onboarding : NavRoutes("onboarding")
    object Home : NavRoutes("home")
    object AddItem : NavRoutes("add")

    object Detail : NavRoutes("detail/{itemId}") {
        fun passItemId(itemId: String): String {
            return "detail/$itemId"
        }
    }
}
