package com.khetigaadi


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
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
class AddressSelectionActivityTest {

    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun add_address() {
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
        Thread.sleep(2000)
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

        onView(
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
        ).perform(click())

        onView(
            allOf(
                withId(R.id.btnAddToCart), withText("Go to Cart"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.my_sticky_button),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.btnChangeAddress), withText("Change or add Address"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.btnAddAddress), withText("Add New"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.facebook.shimmer.ShimmerFrameLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.et_fname),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilFirstName),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), replaceText("Test"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.etLastName),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilLastName),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), replaceText("Test"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.etAddress),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilAddress),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), replaceText("test"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.etState),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilState),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.edSearch),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.parent),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(replaceText("maha"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.mRecyclerView),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(
            allOf(
                withId(R.id.etCity),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilCity),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.edSearch),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.parent),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(replaceText("pune"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.mRecyclerView),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(
            allOf(
                withId(R.id.etPincode),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilPinCode),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), replaceText("411047"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.etMobile),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilMobile),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), replaceText("7766455555"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.btnSaveAndContinue), withText("Save & Continue"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.facebook.shimmer.ShimmerFrameLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)

        onView(
            allOf(
                withId(R.id.ivTrash), withContentDescription("KhetiGaadi"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.cardview.widget.CardView")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())
    }

    @Test
    fun delete_address() {
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
        Thread.sleep(2000)
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

        onView(
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
        ).perform(click())

        onView(
            allOf(
                withId(R.id.btnAddToCart), withText("Go to Cart"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.my_sticky_button),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.btnChangeAddress), withText("Change or add Address"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.core.widget.NestedScrollView")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.rvAddressList),
                childAtPosition(
                    withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))


        onView(
            allOf(
                withId(R.id.tvDelete), withText("Delete"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.rvAddressList),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        ).perform(click())
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
