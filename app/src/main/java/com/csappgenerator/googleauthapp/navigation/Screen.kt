package com.csappgenerator.googleauthapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen(route = "login_screen")
    object Profile : Screen(route = "profile_screen")
}
