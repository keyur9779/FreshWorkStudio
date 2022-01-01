package com.app.freshworkstudio.ui.uiActivity

import android.content.Intent
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.app.freshworkstudio.R
import com.app.freshworkstudio.databinding.ActivitySplashScreenBinding
import com.app.freshworkstudio.utils.DataUtils

class SplashScreen : BaseActivity<ActivitySplashScreenBinding>(R.layout.activity_splash_screen) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Translates ImageView from current position to its original position, scales it from
        larger scale to its original scale.*/
        val translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale)
        translateScale.fillAfter = true

        with(binding) {
            translateScale.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    animation.setAnimationListener(null)
                    headerIcon.visibility = INVISIBLE
                    animation.cancel()
                    headerIcon.clearAnimation()
                    val intent = Intent(this@SplashScreen, MainActivity::class.java)
                    startActivity(
                        intent/*,
                        ActivityOptions.makeSceneTransitionAnimation(this@SplashScreen).toBundle()
                    */
                    )
                    finish()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })

            headerIcon.postDelayed(
                { headerIcon.startAnimation(translateScale) },
                DataUtils.delay.toLong()
            )
        }
    }
}