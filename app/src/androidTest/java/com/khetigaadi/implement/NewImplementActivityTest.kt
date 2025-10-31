package com.khetigaadi.implement


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
class NewImplementActivityTest {

    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun newImplementActivityTest() {
        hiltRule.inject()
        Thread.sleep(10000)
        onView(
            allOf(
                withId(R.id.cvImplements),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    12
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.llBrand)
            )
        ).perform(scrollTo())
        onView(
            allOf(
                withId(R.id.cvParent),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.llBrand),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.my_Recycler_view),
                childAtPosition(
                    withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    0
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.btnOnPrice), withText("Get Price"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.parent),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.cbAgree),
                withText("I agree that by clicking a 'Request' button below I am explicitly soliciting a call from Khetigaadi or its partners on my 'Mobile' in order to assist me with my tractor purchase."),
                childAtPosition(
                    allOf(
                        withId(R.id.llContainer),
                        childAtPosition(
                            withClassName(`is`("androidx.core.widget.NestedScrollView")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.riRequest),
                childAtPosition(
                    allOf(
                        withId(R.id.parent),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.btnRequestForDemo), withText("Request For Demo"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.parent),
                        2
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.cbAgree),
                withText("I agree that by clicking a 'Request' button below I am explicitly soliciting a call from Khetigaadi or its partners on my 'Mobile' in order to assist me with my tractor purchase."),
                childAtPosition(
                    allOf(
                        withId(R.id.llContainer),
                        childAtPosition(
                            withClassName(`is`("androidx.core.widget.NestedScrollView")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.riRequest),
                childAtPosition(
                    allOf(
                        withId(R.id.parent),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withText("Request"),
                childAtPosition(
                    allOf(
                        withId(R.id.riRequest),
                        childAtPosition(
                            withId(R.id.parent),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(1000)
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
