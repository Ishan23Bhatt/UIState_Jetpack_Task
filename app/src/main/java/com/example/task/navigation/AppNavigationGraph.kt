package com.example.task.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.task.ui.screen.bookingservice.BookingServiceScreen
import com.example.task.ui.screen.cleanerservice.CleanerServiceScreen
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.task.ui.screen.checkout.CheckoutScreen
import com.example.task.ui.view.viewmodel.BookingServiceViewModel


const val NAVIGATION_HOST_ROUTE = "navigation_host_route"

@Composable
fun AppNavigationGraph() {
    val activity = LocalContext.current as Activity

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.CleanerServiceScreen.route,
        route = NAVIGATION_HOST_ROUTE,
    ) {
        composable(route = Screen.CleanerServiceScreen.route) {
            val itemsCount =
                navController.currentBackStackEntry?.savedStateHandle?.get<Int>("Item_Counts")
            CleanerServiceScreen(
                onNavigateToBack = {
                    if (!navController.navigateUp()) {
                        activity.finish()
                    }
                },
                onNavigateToBookingService = { navController.navigateToBookingScreen() },
                itemsCount = { itemsCount },
                onNavigateToCheckoutScreen = { navController.navigateToCheckoutScreen() }
            )
        }

        composable(route = Screen.BookingServiceScreen.route) {
            BookingServiceScreen(
                onNavigateToBack = {
                    navController.navigateUp()
                },
                onNavigateToCleanerServiceScreen = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("Item_Counts", it)
                    navController.navigateUp()
                },
            )
        }

        composable(route = Screen.CheckoutScreen.route) {
            CheckoutScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
            )
        }

    }
}