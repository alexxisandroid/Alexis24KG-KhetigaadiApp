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
class HomeFragmentTest {

    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    @Test
    fun homeFragmentTest() {
        hiltRule.inject()
        Thread.sleep(10000)
         onView(
             allOf(
                 withId(R.id.includePopularTractors)
             )
         ).perform(scrollTo())
         val materialButton = onView(
             allOf(
                 withId(R.id.btnIsOwner), withText("Click Here"),
                 childAtPosition(
                     childAtPosition(
                         withId(R.id.llIsOwner),
                         0
                     ),
                     4
                 ),
                 isDisplayed()
             )
         )
         materialButton.perform(click())

         pressBack()

         onView(
             allOf(
                 withId(R.id.rvList),
                 childAtPosition(
                     withId(R.id.includePopularTractors),
                     2
                 )
             )
         ).perform(actionOnItemAtPosition<ViewHolder>(1, click()))

         val tabView = onView(
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
         )
         tabView.perform(click())

         pressBack()
         onView(
             allOf(
                 withId(R.id.includeBrands)
             )
         ).perform(scrollTo())
         onView(
             allOf(
                 withId(R.id.tvViewMore), withText("Show All"),
                 childAtPosition(
                     allOf(
                         withId(R.id.includePopularTractors),
                         childAtPosition(
                             withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                             21
                         )
                     ),
                     3
                 ),
                 isDisplayed()
             )
         ).perform(click())
         Thread.sleep(2000)
         pressBack()
         onView(
             allOf(
                 withId(R.id.mcvBanner1)
             )
         ).perform(scrollTo())
         onView(
             allOf(
                 withId(R.id.rvList),
                 childAtPosition(
                     withId(R.id.includeBrands),
                     2
                 )
             )
         ).perform(actionOnItemAtPosition<ViewHolder>(1, click()))
         Thread.sleep(2000)
         pressBack()

         onView(
             allOf(
                 withText("Implements"),
                 childAtPosition(
                     allOf(
                         withId(R.id.chipGroup),
                         childAtPosition(
                             withId(R.id.includeBrands),
                             1
                         )
                     ),
                     1
                 ),
                 isDisplayed()
             )
         ).perform(click())

         onView(
             allOf(
                 withId(R.id.rvList),
                 childAtPosition(
                     withId(R.id.includeBrands),
                     2
                 )
             )
         ).perform(actionOnItemAtPosition<ViewHolder>(1, click()))

         pressBack()

         onView(
             allOf(
                 withId(R.id.tvLabel), withText("Brands"),
                 childAtPosition(
                     allOf(
                         withId(R.id.includeBrands),
                         childAtPosition(
                             withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                             22
                         )
                     ),
                     0
                 ),
                 isDisplayed()
             )
         ).perform(click())

         onView(
             allOf(
                 withId(R.id.tvViewMore), withText("Show All"),
                 childAtPosition(
                     allOf(
                         withId(R.id.includeBrands),
                         childAtPosition(
                             withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                             22
                         )
                     ),
                     3
                 ),
                 isDisplayed()
             )
         ).perform(click())

         onView(
             allOf(
                 withId(R.id.cvParent),
                 childAtPosition(
                     childAtPosition(
                         withId(R.id.llBrand),
                         0
                     ),
                     1
                 ),
                 isDisplayed()
             )
         ).perform(click())
         Thread.sleep(2000)
         pressBack()

         Thread.sleep(1000)
         pressBack()
         Thread.sleep(1000)
         onView(
             allOf(
                 withId(R.id.includeTractors)
             )
         ).perform(scrollTo())
         onView(
             allOf(
                 withId(R.id.rvList),
                 childAtPosition(
                     withId(R.id.includeTractors),
                     2
                 )
             )
         ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

         Thread.sleep(2000)

         pressBack()

         onView(
             allOf(
                 withId(R.id.tvViewMore), withText("Show All"),
                 childAtPosition(
                     allOf(
                         withId(R.id.includeTractors),
                         childAtPosition(
                             withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                             24
                         )
                     ),
                     3
                 ),
                 isDisplayed()
             )
         ).perform(click())

         Thread.sleep(2000)
         pressBack()
         Thread.sleep(1000)
         onView(
             allOf(
                 withId(R.id.includeStates)
             )
         ).perform(scrollTo())
         onView(
             allOf(
                 withId(R.id.rvList),
                 childAtPosition(
                     withId(R.id.includeStates),
                     2
                 )
             )
         ).perform(actionOnItemAtPosition<ViewHolder>(2, click()))

         pressBack()

         onView(
             allOf(
                 withId(R.id.tvViewMore), withText("Show All"),
                 childAtPosition(
                     allOf(
                         withId(R.id.includeStates),
                         childAtPosition(
                             withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                             25
                         )
                     ),
                     3
                 ),
                 isDisplayed()
             )
         ).perform(click())

         pressBack()
        onView(
            allOf(
                withId(R.id.includeSmallFarmMachinery)
            )
        ).perform(scrollTo())
        Thread.sleep(500)
        onView(
            allOf(
                withId(R.id.rvList),
                childAtPosition(
                    withId(R.id.includeSmallFarmMachinery),
                    2
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(2000)
        pressBack()
        onView(
            allOf(
                withId(R.id.includeCompare)
            )
        ).perform(scrollTo())
        onView(
            allOf(
                withId(R.id.rvList),
                childAtPosition(
                    withId(R.id.includeCompare),
                    2
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

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

        pressBack()

        onView(
            allOf(
                withText("Implements"),
                childAtPosition(
                    allOf(
                        withId(R.id.chipGroup),
                        childAtPosition(
                            withId(R.id.includeCompare),
                            1
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.rvList),
                childAtPosition(
                    withId(R.id.includeCompare),
                    2
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(2000)
        pressBack()
        onView(
            allOf(
                withId(R.id.includeNews)
            )
        ).perform(scrollTo())
        onView(
            allOf(
                withId(R.id.rvList),
                childAtPosition(
                    withId(R.id.includeNews),
                    2
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        pressBack()
        onView(
            allOf(
                withId(R.id.mcvBanner3)
            )
        ).perform(scrollTo())
        onView(
            allOf(
                withId(R.id.tvViewMore), withText("View all news"),
                childAtPosition(
                    allOf(
                        withId(R.id.includeNews),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            30
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(2000)
        pressBack()
        onView(
            allOf(
                withId(R.id.includeVideos)
            )
        ).perform(scrollTo())
        onView(
            allOf(
                withId(R.id.rvList),
                childAtPosition(
                    withId(R.id.includeVideos),
                    2
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(2000)
        pressBack()
        onView(
            allOf(
                withId(R.id.includeTestimonials)
            )
        ).perform(scrollTo())
        onView(
            allOf(
                withId(R.id.tvViewMore), withText("Show All Videos"),
                childAtPosition(
                    allOf(
                        withId(R.id.includeVideos),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            32
                        )
                    ),
                    3
                ),
                isDisplayed()
            )
        ).perform(click())
        Thread.sleep(2000)
        pressBack()

        onView(
            allOf(
                withId(R.id.rvList),
                childAtPosition(
                    withId(R.id.includeTestimonials),
                    2
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(
            allOf(
                withId(R.id.rvList),
                childAtPosition(
                    withId(R.id.includeTestimonials),
                    2
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(0, click()))

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
