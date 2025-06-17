package com.example.appfinanceiro.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.appfinanceiro.R

object ConfirmationUtil {
    fun showConfirmation(
        context: Context,
        title: String,
        message: String,
        positiveButtonText: String = context.getString(R.string.yes),
        negativeButtonText: String = context.getString(R.string.no),
        onPositiveClick: () -> Unit
    ) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButtonText) { _, _ -> onPositiveClick() }
            .setNegativeButton(negativeButtonText, null)
            .show()
    }

    fun showConfirmation(
        context: Context,
        titleResId: Int,
        messageResId: Int,
        positiveButtonTextResId: Int = R.string.yes,
        negativeButtonTextResId: Int = R.string.no,
        onPositiveClick: () -> Unit
    ) {
        showConfirmation(
            context,
            context.getString(titleResId),
            context.getString(messageResId),
            context.getString(positiveButtonTextResId),
            context.getString(negativeButtonTextResId),
            onPositiveClick
        )
    }
} 