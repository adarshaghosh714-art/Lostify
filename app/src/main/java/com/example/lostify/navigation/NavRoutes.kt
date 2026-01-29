package com.example.lostify.navigation

sealed class NavRoutes(val route: String) {

    object Home : NavRoutes("home")
    object AddItem : NavRoutes("add")

    object Detail : NavRoutes("detail/{itemId}") {
        fun createRoute(itemId: String): String {
            return "detail/$itemId"
        }
    }
}
