package com.app.freshworkstudio.ui.uiActivity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.app.freshworkstudio.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SplashScreenTest {


    @get: Rule
    val activity = ActivityScenarioRule(SplashScreen::class.java)


    @Test
    fun SplashScreenLaunched() {
        onView(withId(R.id.header_icon)).check(matches(isDisplayed()))
    }
}