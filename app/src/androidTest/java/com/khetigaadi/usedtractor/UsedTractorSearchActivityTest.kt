package com.khetigaadi.usedtractor
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.khetigaadi.R
import com.khetigaadi.SplashActivity
import com.khetigaadi.dagger.module.AppModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
@LargeTest
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UsedTractorSearchActivityTest {
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
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cvOldTractor),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.scrollView),
                        0
                    ),
                    8
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())
    }
    @Test
    fun search_used_tr_by_name() {
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.edSearchTractorByName),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.cvSearchByName),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.edSearchTractorByBrand),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.containerView),
                        1
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(
            ViewActions.replaceText("mahin"),
            ViewActions.closeSoftKeyboard()
        )
        Thread.sleep(5000)
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.newTractorSearchRes),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("android.widget.LinearLayout")),
                    1
                )
            )
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                1,
                ViewActions.click()
            )
        )

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.my_Recycler_view),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    0
                )
            )
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btnGetLoan), ViewMatchers.withText("Get Loan"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.cvCapacitySpecification),
                        0
                    ),
                    4
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())

        Espresso.pressBack()

        Espresso.pressBack()

        Espresso.pressBack()

        Espresso.pressBack()
    }
    @Test
    fun search_used_tr_by_brand_model() {
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tvMinPrice)
            )
        ).perform(ViewActions.scrollTo())
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.etBrand),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.cvBrandModel),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.edSearch),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.parent),
                        1
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(
            ViewActions.replaceText("mahin"),
            ViewActions.closeSoftKeyboard()
        )

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.text_view), ViewMatchers.withText("Mahindra"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.mRecyclerView),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.etModel),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.cvBrandModel),
                        0
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.edSearch),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.parent),
                        1
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.replaceText("255"), ViewActions.closeSoftKeyboard())

        Espresso.onView(
            Matchers.allOf(

                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.mRecyclerView),
                        0
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())
        Thread.sleep(3000)
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.my_Recycler_view),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    0
                )
            )
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Thread.sleep(2000)
        Espresso.pressBack()

        Espresso.pressBack()

        Espresso.pressBack()
    }
    @Test
    fun search_used_tr_by_price() {
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.tvHeading)
            )
        ).perform(ViewActions.scrollTo())

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.radio2To3), ViewMatchers.withText("2 - 3L"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.radioGroup),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            5
                        )
                    ),
                    1
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.scrollTo(),ViewActions.click())

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.radio3To5), ViewMatchers.withText("3 - 5L"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.radioGroup),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            5
                        )
                    ),
                    2
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.radioAbove5), ViewMatchers.withText("Above 5L"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.radioGroup),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            5
                        )
                    ),
                    3
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.radio10To2), ViewMatchers.withText("10k - 2L"),
                childAtPosition(
                    Matchers.allOf(
                        ViewMatchers.withId(R.id.radioGroup),
                        childAtPosition(
                            ViewMatchers.withClassName(Matchers.`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            5
                        )
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())

        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.btnSearchUsedTractor), ViewMatchers.withText("Search"),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.cvPrice),
                        0
                    ),
                    6
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())
        Thread.sleep(3000)
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.my_Recycler_view),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    0
                )
            )
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Thread.sleep(2000)
        Espresso.pressBack()

        Espresso.pressBack()
    }
    @Test
    fun search_used_tr_by_brand() {
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.llBrand)
            )
        ).perform(ViewActions.scrollTo())
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.cvParent),
                childAtPosition(
                    childAtPosition(
                        ViewMatchers.withId(R.id.llBrand),
                        0
                    ),
                    0
                ),
                ViewMatchers.isDisplayed()
            )
        ).perform(ViewActions.click())
        Thread.sleep(5000)
        Espresso.onView(
            Matchers.allOf(
                ViewMatchers.withId(R.id.my_Recycler_view),
                childAtPosition(
                    ViewMatchers.withClassName(Matchers.`is`("androidx.coordinatorlayout.widget.CoordinatorLayout")),
                    0
                )
            )
        ).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Thread.sleep(2000)

        Espresso.pressBack()

        Espresso.pressBack()
    }
    private fun childAtPosition(parentMatcher:Matcher<View>,position:Int):Matcher<View>
    {

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