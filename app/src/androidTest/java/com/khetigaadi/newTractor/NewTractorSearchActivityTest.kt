package com.khetigaadi.newTractor


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.khetigaadi.R
import com.khetigaadi.SplashActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NewTractorSearchActivityTest {

    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun search_tr_by_name() {
        hiltRule.inject()
        Thread.sleep(10000)
        val homeMenuOptionView = onView(
            allOf(
                withId(R.id.cvNewTractor),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        homeMenuOptionView.perform(click())

        val appCompatEditText = onView(
            allOf(
                withId(R.id.edSearchTractorByName),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cvSearchByName),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        appCompatEditText.perform(click())

        onView(
            allOf(
                withId(R.id.edSearchTractorByBrand),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.containerView),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.edSearchTractorByBrand),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.containerView),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(replaceText("mahindra 255 di"), closeSoftKeyboard())
        Thread.sleep(5000)
        onView(
            allOf(
                withId(R.id.newTractorSearchRes),
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(1000)
        onView((allOf(withId(R.id.tvTractorName)))).check(matches(isDisplayed()))
        pressBack()
        Thread.sleep(1000)
        pressBack()
    }
    @Test
    fun search_new_tr_by_brand_and_model() {
        hiltRule.inject()
        Thread.sleep(10000)
        onView(
            allOf(
                withId(R.id.cvNewTractor),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        ).perform(click())
        onView(
            allOf(
                withId(R.id.btn2WD)
            )
        ).perform(scrollTo())
        onView(
            allOf(
                withId(R.id.et_brand),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cvBrandModel),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.text_view), withText("John Deere"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mRecyclerView),
                        1
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(5000)
        onView(withId(R.id.et_model))
            .perform(scrollTo())
            .check(matches(isDisplayed()));
        onView(
            allOf(
                withId(R.id.et_model),
                isDisplayed()
            )
        ).perform(scrollTo(), click())
        Thread.sleep(5000)
        onView(
            allOf(
                withId(R.id.text_view), withText("5038 D"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.mRecyclerView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(5000)
        onView((allOf(withId(R.id.tvNewTractorName)))).check(matches(isDisplayed()))
        pressBack()
    }

    @Test
    fun search_new_tr_by_hp() {
        hiltRule.inject()
        Thread.sleep(10000)
        onView(
            allOf(
                withId(R.id.cvNewTractor),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        ).perform(click())
        onView(
            allOf(
                withId(R.id.btn2WD)
            )
        ).perform(scrollTo())
        onView(
            allOf(
                withId(R.id.et_hp),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cvSearchByHP),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(scrollTo(), click())
        Thread.sleep(3000)
        val recyclerView = onView(
            allOf(
                withId(R.id.mRecyclerView),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("android.widget.RelativeLayout")),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(5, click()))
        Thread.sleep(5000)
        val recyclerView2 = onView(
            allOf(
                withId(R.id.my_Recycler_view),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    0
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(5000)
        val tabView = onView(
            allOf(
                ViewMatchers.withContentDescription("Specification"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        tabView.perform(click())
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(1000)
        pressBack()
    }

    @Test
    fun search_new_tr_by_WD() {
        hiltRule.inject()
        Thread.sleep(10000)
        onView(
            allOf(
                withId(R.id.cvNewTractor),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        ).perform(click())
        onView(
            allOf(
                withId(R.id.llBrand)
            )
        ).perform(scrollTo())
        onView(
            allOf(
                withId(R.id.btn2WD), withText("2WD"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cvDrive),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(scrollTo(), click())
        Thread.sleep(3000)
        onView(
            allOf(
                withId(R.id.my_Recycler_view),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    0
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(3000)
        pressBack()

        pressBack()

        pressBack()

        pressBack()
    }

    @Test
    fun search_new_tr_by_brand() {
        hiltRule.inject()
        Thread.sleep(10000)
        onView(
            allOf(
                withId(R.id.cvNewTractor),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        ).perform(click())
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
        ).perform(scrollTo(), click())
        Thread.sleep(3000)
        onView(
            allOf(
                withId(R.id.my_Recycler_view),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    0
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(5000)
        onView(
            allOf(
                ViewMatchers.withContentDescription("Specification"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())

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
