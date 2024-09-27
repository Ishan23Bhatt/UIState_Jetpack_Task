package com.example.task.navigation

import androidx.navigation.NavController

fun NavController.navigateToBookingScreen() = navigate(Screen.BookingServiceScreen.route)
fun NavController.navigateToCleanerServiceScreen() = navigate(Screen.CleanerServiceScreen.route)
fun NavController.navigateToCheckoutScreen() = navigate(Screen.CheckoutScreen.route)