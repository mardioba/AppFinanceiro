package com.example.appfinanceiro.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtil {
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))

    fun format(value: Double): String {
        return currencyFormat.format(value)
    }

    fun parse(value: String): Double {
        return try {
            currencyFormat.parse(value)?.toDouble() ?: 0.0
        } catch (e: Exception) {
            0.0
        }
    }
} 