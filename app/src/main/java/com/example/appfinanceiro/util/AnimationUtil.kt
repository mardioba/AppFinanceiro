package com.example.appfinanceiro.util

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.appfinanceiro.R

object AnimationUtil {
    fun fadeIn(view: View, duration: Long = 300) {
        view.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(view.context, android.R.anim.fade_in)
        animation.duration = duration
        view.startAnimation(animation)
    }

    fun fadeOut(view: View, duration: Long = 300) {
        val animation = AnimationUtils.loadAnimation(view.context, android.R.anim.fade_out)
        animation.duration = duration
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }
        })
        view.startAnimation(animation)
    }

    fun slideIn(view: View, duration: Long = 300) {
        view.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.slide_in)
        animation.duration = duration
        view.startAnimation(animation)
    }

    fun slideOut(view: View, duration: Long = 300) {
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.slide_out)
        animation.duration = duration
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                view.visibility = View.GONE
            }
        })
        view.startAnimation(animation)
    }
} 