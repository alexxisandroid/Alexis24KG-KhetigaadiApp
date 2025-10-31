package com.khetigaadi.home


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
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SearchActivityTest {
    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun search_new_tractor() {
        hiltRule.inject()
        Thread.sleep(10000)
        val tvSearchView = onView(
            allOf(
                withId(R.id.tvSearch),
                withText("Search Tractors, Harvesters, Implements..."),
                isDisplayed()
            )
        )
        tvSearchView.perform(click())

        onView(
            allOf(
                withId(kr.co.prnd.readmore.R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(kr.co.prnd.readmore.R.id.search_plate),
                        childAtPosition(
                            withId(kr.co.prnd.readmore.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(replaceText("mahindra 555"), closeSoftKeyboard())
        Thread.sleep(5000)
        onView(
            allOf(withId(R.id.newTractorSearchRes))
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(5000)
        onView(
            allOf(
                withContentDescription("Specification"),
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

        onView(
            allOf(
                withContentDescription("Applications"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabs),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withContentDescription("Picture"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabs),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withContentDescription("Dealers"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tabs),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        ).perform(click())

        pressBack()
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(1000)
        pressBack()
        tvSearchView.check(matches(isDisplayed()))
    }

    @Test
    fun search_new_Imp() {
        hiltRule.inject()
        Thread.sleep(10000)
        val tvSearchView = onView(
            allOf(
                withId(R.id.tvSearch),
                withText("Search Tractors, Harvesters, Implements..."),
                isDisplayed()
            )
        )
        tvSearchView.perform(click())
        onView(
            allOf(
                withId(kr.co.prnd.readmore.R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(kr.co.prnd.readmore.R.id.search_plate),
                        childAtPosition(
                            withId(kr.co.prnd.readmore.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(replaceText("mahi"), closeSoftKeyboard())
        Thread.sleep(5000)
        onView(allOf(withId(R.id.chipImplements))).perform(click())
        onView(
            allOf(withId(R.id.newTractorSearchRes))
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(5000)
        onView(
            allOf(
                withId(R.id.tvName),
                withContentDescription("mahi"),
                isDisplayed()
            )
        )

        pressBack()
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(1000)
        pressBack()
        tvSearchView.check(matches(isDisplayed()))
    }

    @Test
    fun search_used_tractor() {
        hiltRule.inject()
        Thread.sleep(10000)
        val tvSearchView = onView(
            allOf(
                withId(R.id.tvSearch), withText("Search Tractors, Harvesters, Implements..."),
                isDisplayed()
            )
        )
        tvSearchView.perform(click())

        val searchAutoComplete = onView(
            allOf(
                withId(kr.co.prnd.readmore.R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(kr.co.prnd.readmore.R.id.search_plate),
                        childAtPosition(
                            withId(kr.co.prnd.readmore.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(replaceText("mahi"), closeSoftKeyboard())
        Thread.sleep(5000)
        val chip = onView(
            allOf(
                withId(R.id.chipUsedTractor), withText("Used Tractors"),
                childAtPosition(
                    allOf(
                        withId(R.id.chipGroup),
                        childAtPosition(
                            withId(R.id.hcFilterGroup),
                            0
                        )
                    ),
                    4
                )
            )
        )
        chip.perform(scrollTo(), click())

        onView(allOf(withId(R.id.newTractorSearchRes))).perform(
            actionOnItemAtPosition<ViewHolder>(
                0,
                click()
            )
        )
        Thread.sleep(5000)
        onView(
            (allOf(
                withId(R.id.tvTractorName)
            ))
        ).check(matches(isDisplayed()))
        onView(
            allOf(
                withId(R.id.btn_safety_tips), withText("Safety Tips"),
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
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.btnGetLoan), withText("Get Loan"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cvCapacitySpecification),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        pressBack()
        Thread.sleep(1000)
        pressBack()
    }

    @Test
    fun search_product() {
        hiltRule.inject()
        Thread.sleep(10000)
        val tvSearchView = onView(
            allOf(
                withId(R.id.tvSearch), withText("Search Tractors, Harvesters, Implements..."),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        tvSearchView.perform(click())

        val searchAutoComplete = onView(
            allOf(
                withId(kr.co.prnd.readmore.R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(kr.co.prnd.readmore.R.id.search_plate),
                        childAtPosition(
                            withId(kr.co.prnd.readmore.R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(replaceText("pump"), closeSoftKeyboard())
        Thread.sleep(5000)
        var text: String? = null
        onView(
            allOf(
                withId(R.id.newTractorSearchRes),
            )
        ).perform(
            actionOnItemAtPosition<ViewHolder>(0, click())
        )
        onView((allOf(withId(R.id.tvProductName)))).check(matches(isDisplayed()))
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
