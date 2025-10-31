package com.khetigaadi


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
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
class CartListActivityTest {

    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun splashActivityTest() {
        hiltRule.inject()
        Thread.sleep(10000)
        val homeMenuOptionView = onView(
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
        )
        homeMenuOptionView.perform(click())
        Thread.sleep(1000)
        val cardView = onView(
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
        )
        cardView.perform(click())
        Thread.sleep(1000)
        val materialButton = onView(
            allOf(
                withId(R.id.btnBuyNow), withText("Buy Now"),
                childAtPosition(
                    allOf(
                        withId(R.id.clView),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            1
                        )
                    ),
                    14
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val constraintLayout = onView(
            allOf(
                withId(R.id.cart_menu),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        2
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        constraintLayout.perform(click())

        val materialTextView = onView(
            allOf(
                withId(R.id.tvPlus), withText("+"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        5
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        val materialTextView2 = onView(
            allOf(
                withId(R.id.tvMinus), withText("-"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        5
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialTextView2.perform(click())

        val materialButton2 = onView(
            allOf(
                withId(R.id.btnContinue), withText("Continue"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.RelativeLayout")),
                        1
                    ),
                    6
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val materialRadioButton = onView(
            allOf(
                withId(R.id.radioOnline), withText("Pay Online"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    0
                )
            )
        )
        materialRadioButton.perform(scrollTo(), click())


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
