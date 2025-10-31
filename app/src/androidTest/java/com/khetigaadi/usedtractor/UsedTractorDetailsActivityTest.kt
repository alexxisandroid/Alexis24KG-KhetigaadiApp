package com.khetigaadi.usedtractor


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
class UsedTractorDetailsActivityTest {

    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)

    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(SplashActivity::class.java)

    /* @Rule
     @JvmField
     var mGrantPermissionRule =
         GrantPermissionRule.grant(
             "android.permission.CAMERA"
         )*/

    @Test
    fun used_tractor_detail_page() {
        hiltRule.inject()
        Thread.sleep(10000)
        onView(
            allOf(
                withId(R.id.cvOldTractor),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.scrollView),
                        0
                    ),
                    8
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
        Thread.sleep(2000)
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

        onView(
            allOf(
                withId(R.id.btnOldTractor), withText("Old Tractor"),
                childAtPosition(
                    allOf(
                        withId(R.id.toggleButton),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            1
                        )
                    ),
                    1
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.radio4To5), withText("4 - 5 Lac"),
                childAtPosition(
                    allOf(
                        withId(R.id.rgLoanAmount),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            3
                        )
                    ),
                    3
                )
            )
        ).perform(scrollTo(), click())

        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.ivFront),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    4
                )
            )
        ).perform(scrollTo(), click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.mRecyclerView),
                childAtPosition(
                    withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(2, click()))
        Thread.sleep(1000)
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
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.ivBack),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    6
                )
            )
        ).perform(scrollTo(), click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.mRecyclerView),
                childAtPosition(
                    withClassName(`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<ViewHolder>(2, click()))
        Thread.sleep(1000)
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
        Thread.sleep(1000)
       /* onView(
            allOf(
                withId(R.id.btnApply), withText("Apply for Loan"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    8
                )
            )
        ).perform(scrollTo(), click())*/
        pressBack()
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.btnCallNow), withText("Call Now"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cvCapacitySpecification),
                        0
                    ),
                    5
                ),
                isDisplayed()
            )
        ).perform(click())

         onView(
            allOf(
                withId(R.id.btnSellerDetails), withText("Contact Seller"),
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
                withId(R.id.cbAgree),
                withText("I agree that by clicking a 'Request' button below I am explicitly soliciting a call from Khetigaadi or its partners on my 'Mobile' in order to assist me with my tractor purchase."),
                childAtPosition(
                    allOf(
                        withId(R.id.llGetDealerDetails),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    1
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.cbAgree),
                withText("I agree that by clicking a 'Request' button below I am explicitly soliciting a call from Khetigaadi or its partners on my 'Mobile' in order to assist me with my tractor purchase."),
                childAtPosition(
                    allOf(
                        withId(R.id.llGetDealerDetails),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    1
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.cbAgree),
                withText("I agree that by clicking a 'Request' button below I am explicitly soliciting a call from Khetigaadi or its partners on my 'Mobile' in order to assist me with my tractor purchase."),
                childAtPosition(
                    allOf(
                        withId(R.id.llGetDealerDetails),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            0
                        )
                    ),
                    1
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.riSubmit),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.btnSubmit), withText("Request"),
                childAtPosition(
                    allOf(
                        withId(R.id.riSubmit),
                        childAtPosition(
                            withClassName(`is`("android.widget.RelativeLayout")),
                            2
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(click())

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

        pressBack()

        onView(
            allOf(
                withId(R.id.ivFavorites),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.ivFavorites),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        ).perform(click())

        onView(
            allOf(
                withId(R.id.ivShare),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cvCapacitySpecification),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())

        pressBack()

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
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
