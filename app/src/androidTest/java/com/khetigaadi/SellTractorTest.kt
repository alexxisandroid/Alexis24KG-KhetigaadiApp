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
class SellTractorTest {

    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun sell_tractor() {
        hiltRule.inject()
        Thread.sleep(10000)
        val homeMenuOptionView = onView(
            allOf(
                withId(R.id.cvSellTractor),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    9
                ),
                isDisplayed()
            )
        )
        homeMenuOptionView.perform(click())

        onView(
            allOf(
                withId(R.id.cvParent),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.llBrand),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.cv),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.llModel),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.iv_first),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.mRecyclerView),
                childAtPosition(
                    withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(3, click()))

        onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(com.yalantis.ucrop.R.id.ucrop_photobox),
                        childAtPosition(
                            withId(android.R.id.content),
                            0
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(com.yalantis.ucrop.R.id.menu_crop), withContentDescription("Crop"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.iv_second),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        1
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.mRecyclerView),
                childAtPosition(
                    withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(3, click()))

        onView(
            allOf(
                withId(com.yalantis.ucrop.R.id.menu_crop), withContentDescription("Crop"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.toolbar),
                        2
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.btn_image_next), withText("Continue"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.cv),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.llYear),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.cv),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.llMonth),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.cv),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.llHour),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.btn_noc_no),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        1
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.btn_rc_not_avail),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        1
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.btn_more_owner),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        1
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.cv_document_next),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.btn_poor_engine),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.btn_poor_tyre),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.cv_condition_next),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.appcompat.widget.LinearLayoutCompat")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.et_registration_no),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    1
                )
            )
        ).perform(scrollTo(), replaceText("test46"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.et_expected_price),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    3
                )
            )
        ).perform(scrollTo(), replaceText("25"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.et_about_us),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    5
                )
            )
        ).perform(scrollTo(), replaceText("test"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.cv_update_sell_tractor),
                childAtPosition(
                    allOf(
                        withId(R.id.parent),
                        childAtPosition(
                            withId(R.id.fragmentContainerView),
                            0
                        )
                    ),
                    1
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
