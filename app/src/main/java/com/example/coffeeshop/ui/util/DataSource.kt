package com.example.coffeeshop.ui.util

import android.annotation.SuppressLint
import com.example.coffeeshop.R
import java.text.DecimalFormat

object DataSource {
    val ratingBarList = listOf(
        R.drawable.terrible,
        R.drawable.bad,
        R.drawable.okay,
        R.drawable.good,
        R.drawable.awesome
    )

    fun calculateTotal(price: Double,discount: Int): Double {
        return (price - (price*discount/100))
    }

    @SuppressLint("DefaultLocale")
    fun formatTotal(totalPrice: Double): Double{
        val decimalFormat = DecimalFormat("#.##")
        val formattedPrice = decimalFormat.format(totalPrice)
        return formattedPrice.toDouble()
    }

}