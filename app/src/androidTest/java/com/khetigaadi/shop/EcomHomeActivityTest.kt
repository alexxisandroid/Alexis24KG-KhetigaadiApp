package com.khetigaadi.shop

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.khetigaadi.R
import com.khetigaadi.SplashActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class EcomHomeActivityTest {

    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun ecomHomeActivityTest() {
        hiltRule.inject()
        Thread.sleep(10000)
        onView(
            allOf(
                withId(R.id.cvShop),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    10
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.rvCategories),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

//        onView(
//            allOf(
//                withId(R.id.rvCategories),
//                childAtPosition(
//                    withId(R.id.fragmentContainer),
//                    0
//                )
//            )
//        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        pressBack()

        onView(
            allOf(
                withId(R.id.includeTrendingLarge),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        ).perform(click())

        pressBack()
        val rvProductsList = onView(
            allOf(
                withId(R.id.rvProductsList),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        )
        onView(allOf(withId(R.id.rvHomeList))).perform(scrollTo())


        pressBack()

        pressBack()
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}

