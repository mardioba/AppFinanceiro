package com.example.appfinanceiro.util

import android.content.Context
import com.example.appfinanceiro.R
import com.google.android.material.snackbar.Snackbar
import android.view.View

object ErrorUtil {
    fun showError(context: Context, view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }

    fun showError(context: Context, view: View, messageResId: Int) {
        showError(context, view, context.getString(messageResId))
    }

    fun showError(context: Context, view: View, messageResId: Int, vararg args: Any) {
        showError(context, view, context.getString(messageResId, *args))
    }

    fun showError(context: Context, view: View, throwable: Throwable) {
        val message = when (throwable) {
            is IllegalArgumentException -> throwable.message ?: context.getString(R.string.error_saving)
            else -> context.getString(R.string.error_saving)
        }
        showError(context, view, message)
    }
} 