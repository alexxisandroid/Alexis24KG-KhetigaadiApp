package com.khetigaadi.login


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.khetigaadi.R
import com.khetigaadi.SplashActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginActivityTest {
    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun login_with_mobile_and_otp() {
        hiltRule.inject()
        onView(
            allOf(
                withId(R.id.et_mobile), isDisplayed()
            )
        ).perform(
            replaceText("7875114466"), closeSoftKeyboard()
        )

        onView(
            allOf(
                withId(R.id.btnLogin),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.input_code),
                isDisplayed()
            )
        ).perform(replaceText("9090"), closeSoftKeyboard())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.btnSubmit), withText("Submit"),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(5000)
        onView(withId(R.id.activity_main_rfab)).check(matches(isDisplayed()))


    }

}
