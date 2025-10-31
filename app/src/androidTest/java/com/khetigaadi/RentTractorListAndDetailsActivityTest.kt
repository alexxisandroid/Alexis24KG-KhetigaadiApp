package com.khetigaadi


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
class RentTractorListAndDetailsActivityTest {
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
        onView(
            allOf(
                withContentDescription("KhetiGaadi"),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbar),
                        childAtPosition(
                            withClassName(`is`("com.google.android.material.appbar.AppBarLayout")),
                            0
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())
        onView(
            allOf(
                withText("RENT"),
                isDisplayed()
            )
        ).perform(click())
      /*  val ivFavorites = onView(
            allOf(
                withId(R.id.ivFavorites),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withClassName(`is`("androidx.cardview.widget.CardView")),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        ivFavorites.perform(click())
        Thread.sleep(2000)
        ivFavorites.perform(click())*/

        onView(
            allOf(
                withId(R.id.filter_icon),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.containerView),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.rvBrands),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        ).perform(
            actionOnItemAtPosition<ViewHolder>(0, click()),
            actionOnItemAtPosition<ViewHolder>(1, click()),
            actionOnItemAtPosition<ViewHolder>(2, click())
        )


        onView(
            allOf(
                withId(R.id.rvHPs),
                childAtPosition(
                    withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                    2
                )
            )
        ).perform(
            actionOnItemAtPosition<ViewHolder>(0, click()),
            actionOnItemAtPosition<ViewHolder>(1, click())
        )

        onView(
            allOf(
                withId(R.id.btnApplyFilter), withText("Apply Filters"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                        1
                    ),
                    1
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
        val ivFavoritesDetails = onView(
            allOf(
                withId(R.id.ivFavorites),
                childAtPosition(
                    allOf(
                        withId(R.id.container),
                        childAtPosition(
                            withClassName(`is`("androidx.core.widget.NestedScrollView")),
                            0
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        ivFavoritesDetails.perform(click())

        Thread.sleep(1000)
        ivFavoritesDetails.perform(click())

        onView(
            allOf(
                withId(R.id.btnSendEnq), withText("Send Enquiry"),
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

        onView(
            allOf(
                withId(R.id.input_msg),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.input_layout_msg),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), replaceText("Test"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.riSendEnquiry),
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

        pressBack()
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(1000)
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
