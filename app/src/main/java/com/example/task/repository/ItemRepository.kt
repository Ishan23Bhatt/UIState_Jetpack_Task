package com.example.task.repository

import android.content.Context
import android.util.Log
import com.example.task.data.model.CartItems
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ItemRepository @Inject constructor(private val context: Context) {

    // Function to fetch and parse item data from JSON
    suspend fun getItemData(): CartItems {
        return withContext(Dispatchers.IO) {
            // Read the JSON file as a string
//            Log.d("asdf","item_data.json :: Called ")
            val jsonString = context.assets.open("item_data.json").bufferedReader().use { it.readText() }
            // Parse the JSON string into a CartItems object
            parseJson(jsonString)
        }
    }

    // Parsing JSON string into CartItems object using Gson
    private fun parseJson(jsonString: String): CartItems {
        return Gson().fromJson(jsonString, CartItems::class.java)
    }
}