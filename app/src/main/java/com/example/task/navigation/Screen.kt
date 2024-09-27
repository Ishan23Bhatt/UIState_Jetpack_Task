package com.example.task.navigation

import java.net.URLEncoder

sealed class Screen(val route: String, val name: String) {
    data object CleanerServiceScreen : Screen(route = "cleaner_service_screen", name = "CleanerServiceScreen")
    data object BookingServiceScreen : Screen(route = "booking_service_screen", name = "BookingServiceScreen")
    data object CheckoutScreen : Screen(route = "checkout_screen", name = "CheckoutScreen")

    fun buildArgsRoute(vararg args: Any) = buildString {
        append(route)
        args.forEach { arg ->
            append("/{$arg}")
        }
    }

    fun buildOptionalArgsRoute(vararg args: Any) = buildString {
        append(route.plus("?"))
        args.forEachIndexed { index, any ->
            if (index == args.size - 1) {
                append("$any={$any}")
            } else {
                append("$any={$any}&")
            }
        }
    }

    fun withArgs(vararg args: Any) = buildString {
        append(route)
        args.forEach { arg ->
            append("/$arg")
        }
    }

    fun withOptionalArgs(hashMap: HashMap<String, Any?>) = buildString {
        append(route.plus("?"))
        hashMap.entries.forEachIndexed { index, entry ->
            if (index == hashMap.size - 1) {
                append("${entry.key}=${entry.value}")
            } else {
                append("${entry.key}=${entry.value}&")
            }
        }
    }

    fun withOptionalArgsURL(hashMap: HashMap<String, Any?>): String {
        val encodedParams = hashMap.entries.joinToString("&") { (key, value) ->
            val encodedKey = URLEncoder.encode(key, "UTF-8")
            val encodedValue = URLEncoder.encode(value.toString(), "UTF-8")
            "$encodedKey=$encodedValue"
        }
        return encodedParams
    }
}
