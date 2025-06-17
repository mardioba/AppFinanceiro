package com.example.appfinanceiro.util

object StringUtil {
    fun capitalize(text: String): String {
        if (text.isEmpty()) return text
        return text.substring(0, 1).uppercase() + text.substring(1).lowercase()
    }

    fun truncate(text: String, maxLength: Int): String {
        if (text.length <= maxLength) return text
        return text.substring(0, maxLength - 3) + "..."
    }

    fun isNullOrEmpty(text: String?): Boolean {
        return text == null || text.trim().isEmpty()
    }

    fun isNullOrBlank(text: String?): Boolean {
        return text == null || text.isBlank()
    }

    fun defaultIfEmpty(text: String?, defaultText: String): String {
        return if (isNullOrEmpty(text)) defaultText else text!!
    }

    fun defaultIfBlank(text: String?, defaultText: String): String {
        return if (isNullOrBlank(text)) defaultText else text!!
    }
} 