package com.example.coffeeshop.ui.util

import com.example.coffeeshop.R

object DataSource {
    val ratingBarList = listOf(
        R.drawable.terrible,
        R.drawable.bad,
        R.drawable.okay,
        R.drawable.good,
        R.drawable.awesome
    )

    fun calculateTotal(price: Int,discount: Int): Int {
        return (price - (price*discount/100))
    }
}