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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class ImplementSearchActivityTest {

    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Before
    fun beforeAll() {
        hiltRule.inject()
        Thread.sleep(10000)
        onView(
            allOf(
                withId(R.id.cvImplements), childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView), 0
                    ), 12
                ), isDisplayed()
            )
        ).perform(click())
    }

    @Test
    fun search_imp_by_name() {
        onView(
            allOf(
                withId(R.id.edSearchImpByName), childAtPosition(
                    childAtPosition(
                        withId(R.id.cvSearchByName), 0
                    ), 1
                ), isDisplayed()
            )
        ).perform(click())
        onView(
            allOf(
                withId(R.id.edSearchTractorByBrand), childAtPosition(
                    childAtPosition(
                        withId(R.id.containerView), 1
                    ), 0
                ), isDisplayed()
            )
        ).perform(replaceText("mahind"), closeSoftKeyboard())
        onView(
            allOf(
                withId(R.id.newTractorSearchRes), childAtPosition(
                    withClassName(`is`("android.widget.LinearLayout")), 1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(
            allOf(
                withId(R.id.btnOnPrice), withText("Get Price"), childAtPosition(
                    childAtPosition(
                        withId(R.id.parent), 2
                    ), 0
                ), isDisplayed()
            )
        ).perform(click())

        pressBack()

        pressBack()

        pressBack()

        pressBack()

        pressBack()
    }

    @Test
    fun search_imp_by_brand_and_type() {
        onView(
            allOf(
                withId(R.id.cvSearchByHP)
            )
        ).perform(scrollTo())
        onView(
            allOf(
                withId(R.id.et_brand), childAtPosition(
                    childAtPosition(
                        withId(R.id.cvBrandModel), 0
                    ), 1
                ), isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.text_view), withText("Maschio Gaspardo"), childAtPosition(
                    childAtPosition(
                        withId(R.id.mRecyclerView), 0
                    ), 1
                ), isDisplayed()
            )
        ).perform(click())
        Thread.sleep(3000)
        onView(
            allOf(
                withId(R.id.etType), childAtPosition(
                    childAtPosition(
                        withId(R.id.cvBrandModel), 0
                    ), 2
                ), isDisplayed()
            )
        ).perform(click())
        Thread.sleep(3000)
        onView(
            allOf(
                withId(R.id.text_view), withText("Rotary Tiller"), childAtPosition(
                    childAtPosition(
                        withId(R.id.mRecyclerView), 0
                    ), 1
                ), isDisplayed()
            )
        ).perform(click())
        Thread.sleep(3000)
        onView(
            allOf(
                withId(R.id.my_Recycler_view), childAtPosition(
                    withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")), 0
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(2000)
        pressBack()

        pressBack()

        pressBack()
    }

    @Test
    fun search_imp_by_hp() {
        onView(
            allOf(
                withId(R.id.tvBrandLabel)
            )
        ).perform(scrollTo())
        onView(
            allOf(
                withId(R.id.et_hp), childAtPosition(
                    childAtPosition(
                        withId(R.id.cvSearchByHP), 0
                    ), 1
                ), isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.mRecyclerView), childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")), 1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        onView(
            allOf(
                withId(R.id.my_Recycler_view), childAtPosition(
                    withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")), 0
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(
            allOf(
                withId(R.id.btnOnPrice), withText("Get Price"), childAtPosition(
                    childAtPosition(
                        withId(R.id.parent), 2
                    ), 0
                ), isDisplayed()
            )
        ).perform(click())

        pressBack()

        pressBack()

        pressBack()

        pressBack()
    }

    @Test
    fun search_imp_by_brand() {
        onView(
            allOf(
                withId(R.id.llBrand)
            )
        ).perform(scrollTo())
        val cardView = onView(
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
        )
        cardView.perform(scrollTo(), click())

        val recyclerView = onView(
            allOf(
                withId(R.id.my_Recycler_view),
                childAtPosition(
                    withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    0
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        val materialButton = onView(
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
        )
        materialButton.perform(click())

        pressBack()

        pressBack()

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
                return parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(
                    position
                )
            }
        }
    }
}
