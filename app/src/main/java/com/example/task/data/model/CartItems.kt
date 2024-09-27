package com.example.task.data.model

import android.content.Context
import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

data class CartItems(
    val _id: String,
    val item_taxes: List<Int>,
    val name: List<String>,
    val price: Int,
    val specifications: List<Specification>

) {
    override fun toString(): String {
        return "CartItems(_id='$_id', item_taxes=$item_taxes, name=$name, price=$price, specifications=$specifications)"
    }
}
