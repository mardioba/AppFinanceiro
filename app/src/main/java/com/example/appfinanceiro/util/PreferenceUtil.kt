package com.example.appfinanceiro.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object PreferenceUtil {
    private const val PREF_NAME = "app_preferences"
    private const val KEY_FIRST_RUN = "first_run"
    private const val KEY_USER_NAME = "user_name"
    private const val KEY_USER_EMAIL = "user_email"
    private const val KEY_DARK_MODE = "dark_mode"
    private const val KEY_NOTIFICATION_ENABLED = "notification_enabled"
    private const val KEY_CURRENCY = "currency"
    private const val KEY_LANGUAGE = "language"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isFirstRun(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_FIRST_RUN, true)
    }

    fun setFirstRun(context: Context, isFirstRun: Boolean) {
        getPreferences(context).edit {
            putBoolean(KEY_FIRST_RUN, isFirstRun)
        }
    }

    fun getUserName(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_NAME, null)
    }

    fun setUserName(context: Context, name: String) {
        getPreferences(context).edit {
            putString(KEY_USER_NAME, name)
        }
    }

    fun getUserEmail(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_EMAIL, null)
    }

    fun setUserEmail(context: Context, email: String) {
        getPreferences(context).edit {
            putString(KEY_USER_EMAIL, email)
        }
    }

    fun isDarkMode(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_DARK_MODE, false)
    }

    fun setDarkMode(context: Context, isDarkMode: Boolean) {
        getPreferences(context).edit {
            putBoolean(KEY_DARK_MODE, isDarkMode)
        }
    }

    fun isNotificationEnabled(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_NOTIFICATION_ENABLED, true)
    }

    fun setNotificationEnabled(context: Context, isEnabled: Boolean) {
        getPreferences(context).edit {
            putBoolean(KEY_NOTIFICATION_ENABLED, isEnabled)
        }
    }

    fun getCurrency(context: Context): String {
        return getPreferences(context).getString(KEY_CURRENCY, "BRL") ?: "BRL"
    }

    fun setCurrency(context: Context, currency: String) {
        getPreferences(context).edit {
            putString(KEY_CURRENCY, currency)
        }
    }

    fun getLanguage(context: Context): String {
        return getPreferences(context).getString(KEY_LANGUAGE, "pt") ?: "pt"
    }

    fun setLanguage(context: Context, language: String) {
        getPreferences(context).edit {
            putString(KEY_LANGUAGE, language)
        }
    }

    fun clearPreferences(context: Context) {
        getPreferences(context).edit {
            clear()
        }
    }
} 