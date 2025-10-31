package com.khetigaadi.compare


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class CompareTractorActivityTest {

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
                withId(R.id.btnIsOwner)
            )
        ).perform(ViewActions.scrollTo())
        onView(
            allOf(
                withId(R.id.tvMainMenuViewMore), withText("View more"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    19
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(700)
        onView(
            allOf(
                withId(R.id.cvCompare),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    14
                ),
                isDisplayed()
            )
        ).perform(click())
    }

    @Test
    fun poplar_tr_compare_list() {
        Thread.sleep(3000)
        onView(
            allOf(
                withId(R.id.rvPopularTractors),
                childAtPosition(
                    withId(R.id.recycleViewRelativePopularTractors),
                    2
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(2000)
        onView(
            allOf(
                withContentDescription("Specification"),
                childAtPosition(
                    childAtPosition(
                        allOf(withId(R.id.tabs), withContentDescription("KhetiGaadi")),
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
                withContentDescription("Images"),
                childAtPosition(
                    childAtPosition(
                        allOf(withId(R.id.tabs), withContentDescription("KhetiGaadi")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        Espresso.pressBack()
        Thread.sleep(1000)
        Espresso.pressBack()
        Thread.sleep(1000)
        Espresso.pressBack()
    }

    @Test
    fun compare_tr_by_brand_model_tr_selection() {
        onView(
            allOf(
                withId(R.id.cvTractor1),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.cardview.widget.CardView")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(3000)
        onView(
            allOf(
                withId(R.id.newTractorSearchRes),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(1, click()))
        Thread.sleep(2000)
        onView(
            allOf(
                withId(R.id.newTractorSearchRes),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(2000)
        onView(
            allOf(
                withId(R.id.cvTractor2),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withClassName(Matchers.`is`("androidx.cardview.widget.CardView")),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(2000)
        onView(
            allOf(
                withId(R.id.newTractorSearchRes),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(2000)
        onView(
            allOf(
                withId(R.id.newTractorSearchRes),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(2000)
        onView(
            allOf(
                withId(R.id.btnCompare),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        /* onView(
             allOf(
                 withText("Compare"),
                 childAtPosition(
                     allOf(
                         withId(R.id.btnCompare),
                         childAtPosition(
                             ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                             1
                         )
                     ),
                     0
                 ),
                 isDisplayed()
             )
         ).perform(click())
         Thread.sleep(3000)*/
        onView(
            allOf(
                withContentDescription("Specification"),
                childAtPosition(
                    childAtPosition(
                        allOf(withId(R.id.tabs), withContentDescription("KhetiGaadi")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(1000)
        Espresso.pressBack()

        Espresso.pressBack()

        Espresso.pressBack()

        Espresso.pressBack()
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
