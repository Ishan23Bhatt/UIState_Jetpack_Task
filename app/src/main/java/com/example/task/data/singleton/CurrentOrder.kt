package com.example.task.data.singleton

import com.example.task.data.model.Item0
import com.example.task.data.model.Specification

object CurrentOrder {
    var addToCartItemsList: MutableList<Item0> = mutableListOf()
    var totalAddToCartItemsList: MutableList<Item0> = mutableListOf()
    var checkoutList: MutableList<List<Specification>> = mutableListOf()
    var quantityList: MutableList<Int> = mutableListOf()
    var quantity: Int = 0
    var price: Double = 0.00
}