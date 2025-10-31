package com.khetigaadi
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
//import com.khetigaadi.R
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
class ProfileActivityTest {
    @Rule(order = 0)
    @JvmField
    var hiltRule = HiltAndroidRule(this)
    @Rule(order = 1)
    @JvmField
    var mActivityScenarioRule = "" // ActivityScenarioRule(SplashActivity::class.java)
    @Before
    fun beforeAll() {
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
    }
    @Test
    fun `update_profile`() {  // withId(com.payu.otpparser.R.id.navigation_header_container),
        onView(
            allOf(
                withId(R.id.tvUpdateProfile), withText("Update your profile"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigation_header_container),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        ).perform(click())
        onView(
            allOf(
                withId(R.id.et_account_holder_name),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tvName),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click(), replaceText("Test1"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.et_account_holder_name),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.til_brand),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), replaceText("Test2"), closeSoftKeyboard())


        onView(
            allOf(
                withId(R.id.actGender), withText("Male"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilGender),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.etUserType),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilUserType),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.text_view),
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
        val etState = onView(
            allOf(
                withId(R.id.et_state),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.til_state),
                        0
                    ),
                    0
                )
            )
        )
        etState.perform(scrollTo())
        onView(
            allOf(
                withId(R.id.etBiography),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilBiography),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), replaceText("Test"))

        onView(
            allOf(
                withId(R.id.etBiography), withText("Test"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilBiography),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        ).perform(closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.etAwards),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilAwards),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), replaceText("Test"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.etCrops),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilCrops),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.text_view),
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

        onView(
            allOf(
                withId(R.id.tvOk), withText("Ok"),
                childAtPosition(
                    allOf(
                        withId(R.id.llButton),
                        childAtPosition(
                            withClassName(`is`("android.widget.RelativeLayout")),
                            3
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        ).perform(click())
        onView(
            allOf(
                withId(R.id.etLand),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilLand),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), replaceText("2"), closeSoftKeyboard())

        onView(
            allOf(
                withId(R.id.rb_yes),
                childAtPosition(
                    allOf(
                        withId(R.id.rg_tractor_owner),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            14
                        )
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.etBrand),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    16
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.text_view),
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

        onView(
            allOf(
                withId(R.id.rb_smart_phone_no),
                childAtPosition(
                    allOf(
                        withId(R.id.rg_mobile),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            18
                        )
                    ),
                    1
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.rb_smart_phone_yes),
                childAtPosition(
                    allOf(
                        withId(R.id.rg_mobile),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            18
                        )
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())
        etState.perform(scrollTo(), click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.mRecyclerView),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(
            allOf(
                withId(R.id.et_city),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.til_city),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())
        Thread.sleep(2000)
        onView(
            allOf(
                withId(R.id.mRecyclerView),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(
            allOf(
                withId(R.id.etTaluka),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.tilTaluka),
                        0
                    ),
                    0
                )
            )
        ).perform(scrollTo(), click())
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.mRecyclerView),
                childAtPosition(
                    withClassName(`is`("android.widget.RelativeLayout")),
                    1
                )
            )
        ).perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        Thread.sleep(1000)
        onView(
            allOf(
                withId(R.id.cbTerms),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    22
                )
            )
        ).perform(scrollTo(), click())

        onView(
            allOf(
                withId(R.id.btn_update), withText("Update"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.ScrollView")),
                        0
                    ),
                    24
                )
            )
        ).perform(scrollTo(), click())
    }
    private fun childAtPosition(parentMatcher:Matcher<View>,position:Int):Matcher<View>
    {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view:View):Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent) && view == parent.getChildAt(position)
            }
        }
    }
}
