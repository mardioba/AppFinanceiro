package com.example.appfinanceiro.util

object CategoryUtil {
    val categories = listOf(
        "Moradia",
        "Alimentação",
        "Transporte",
        "Lazer",
        "Saúde",
        "Educação",
        "Vestuário",
        "Outros"
    )

    fun isValidCategory(category: String): Boolean {
        return categories.contains(category)
    }

    fun getDefaultCategory(): String {
        return categories.first()
    }
} 