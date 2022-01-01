package com.app.freshworkstudio.utils

import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener

/**
 *
 * Utility class used to easily animate Views. Most used for revealing or hiding Views.
 */
object AnimationUtils {

    @JvmStatic
    val ANIMATION_DURATION_SHORT = 250


    @TargetApi(21)
    @JvmStatic
    @JvmOverloads
    fun circleRevealView(
        view: View,
        duration: Int = ANIMATION_DURATION_SHORT,
        listenerAdapter: AnimatorListenerAdapter
    ) {
        // get the center for the clipping circle
        val cx = view.width
        val cy = view.height / 2

        // get the final radius for the clipping circle
        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        // create the animator for this view (the start radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
        anim.addListener(listenerAdapter)
        anim.duration = if (duration > 0) duration.toLong() else ANIMATION_DURATION_SHORT.toLong()

        // make the view visible and start the animation
        view.visibility = View.VISIBLE
        anim.start()
    }

    @TargetApi(21)
    @JvmStatic
    fun circleHideView(view: View, listenerAdapter: AnimatorListenerAdapter) {
        circleHideView(view, ANIMATION_DURATION_SHORT, listenerAdapter)
    }

    @TargetApi(21)
    @JvmStatic
    fun circleHideView(view: View, duration: Int, listenerAdapter: AnimatorListenerAdapter) {
        // get the center for the clipping circle
        val cx = view.width
        val cy = view.height / 2

        // get the initial radius for the clipping circle
        val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        // create the animation (the final radius is zero)
        val anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0f)

        // make the view invisible when the animation is done
        anim.addListener(listenerAdapter)

        anim.duration = if (duration > 0) duration.toLong() else ANIMATION_DURATION_SHORT.toLong()

        // start the animation
        anim.start()
    }
}

