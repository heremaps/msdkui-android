/*
 * Copyright (C) 2017-2018 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.here.msdkuiapp.espresso.impl.core

import android.support.test.InstrumentationRegistry
import android.support.test.espresso.PerformException
import android.support.test.espresso.ViewAction
import android.support.test.espresso.UiController
import android.support.test.espresso.FailureHandler
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isRoot
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.espresso.util.HumanReadables
import android.support.test.espresso.util.TreeIterables
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import com.here.msdkui.common.DateFormatterUtil
import com.here.msdkui.common.ThemeUtil
import com.here.msdkuiapp.espresso.impl.utils.CurrentActivityUtils
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.*
import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.TimeoutException

/**
 * Core framework matchers
 */
object CoreMatchers {

    private const val TIMEOUT_WAIT_SEC: Long = 60
    private const val TIMEOUT_DELAY_MILLIS: Long = 1500
    private val TIMEOUT_WAIT_MILLIS: Long = SECONDS.toMillis(TIMEOUT_WAIT_SEC)
    const val TIMEOUT_WAIT_DOUBLE_MILLIS: Long = 120000
    const val TIMEOUT_WAIT_TRIPLE_MILLIS: Long = 180000

    /**
     * Waits util given view is successfully matched OR timeout happens.
     * TODO: MSDKUI-496, MSDKUI-561 - remove this method when idling resource implemented
     *
     * @param viewMatcher view will be tested by this matcher.
     * @param timeout timeout of wait in milliseconds.
     * @param checkInterval delay between consecutive checks in milliseconds.
     * @param isVisible whether the checked view must be visible.
     * @param isEnabled whether the checked view must be enabled.
     * @param isSelected whether the checked view must be selected.
     */
    fun waitForCondition(viewMatcher: Matcher<View>, timeout: Long = TIMEOUT_WAIT_MILLIS,
                         checkInterval: Long = TIMEOUT_DELAY_MILLIS,
                         isVisible: Boolean = true, isEnabled: Boolean = false, isSelected: Boolean = false): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for a specific view during $timeout timeout."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + timeout
                do {
                    uiController.loopMainThreadForAtLeast(checkInterval)
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        if (isEnabled && viewMatcher.matches(child) && child.isEnabled) return
                        if (isSelected && viewMatcher.matches(child) && child.isSelected) return
                        if (isVisible && viewMatcher.matches(child)) return
                        if (!isVisible && !viewMatcher.matches(isDisplayed())) return
                    }
                } while (System.currentTimeMillis() < endTime)

                // timeout happens
                throw PerformException.Builder()
                        .withActionDescription(this.description)
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(TimeoutException())
                        .build()
            }
        }
    }

    /**
     * Waits util text of the view under [viewMatcher] changes OR timeout happens.
     */
    fun waitForTextChange(viewMatcher: Matcher<View>,
                          notWithText: String,
                          timeout: Long = TIMEOUT_WAIT_MILLIS,
                          checkInterval: Long = TIMEOUT_DELAY_MILLIS): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isRoot()
            }

            override fun getDescription(): String {
                return "wait for text in a specific view to change during $timeout timeout."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + timeout
                do {
                    uiController.loopMainThreadForAtLeast(checkInterval)
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        if (viewMatcher.matches(child) && (child as TextView).text != notWithText) {
                            return
                        }
                    }
                } while (System.currentTimeMillis() < endTime)

                // timeout happens
                throw PerformException.Builder()
                        .withActionDescription(this.description)
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(TimeoutException())
                        .build()
            }
        }
    }

    /**
     * Matches the view with index in case of espresso matches several views with withId or withText.
     */
    fun withIndex(matcher: Matcher<View>, index: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var currentIndex = 0

            override fun describeTo(description: Description) {
                description.appendText("with index: ")
                description.appendValue(index)
                matcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                return matcher.matches(view) && currentIndex++ == index
            }
        }
    }

    /**
     * Matches the size of list with given input size.
     */
    fun withListSize(size: Int): Matcher<in View>? {
        return object : TypeSafeMatcher<View>(), Matcher<View> {

            override fun matchesSafely(view: View): Boolean {
                return (view as RecyclerView).adapter!!.itemCount == size
            }

            override fun describeTo(description: Description) {
                description.appendText("RecyclerView should have $size items")
            }
        }
    }

    /**
     * Matches [TextView] with [date] text
     */
    fun withDateText(date: Date): Matcher<View>? {
        return withText(DateFormatterUtil.format(date))
    }

    /**
     * Get text from [TextView]
     * @return [String] text from [TextView], represented by [ViewInteraction]
     */
    fun getText(viewInteraction: ViewInteraction): String {
        var stringHolder = String()
        viewInteraction.perform(object : ViewAction {
            override fun getConstraints() = isAssignableFrom(TextView::class.java)

            override fun getDescription() = "Get text from view: "

            override fun perform(uiController: UiController, view: View) {
                val tv = view as TextView
                stringHolder = tv.text.toString()
            }
        })
        return stringHolder
    }

    /**
     * Get date from [DatePicker]
     * @return [Calendar] date from [DatePicker], represented by [ViewInteraction]
     */
    fun getDate(viewInteraction: ViewInteraction): Calendar {
        val date = Calendar.getInstance()
        viewInteraction.perform(object : ViewAction {
            override fun getConstraints() = isAssignableFrom(DatePicker::class.java)

            override fun getDescription() = "Get date from DatePicker: "

            override fun perform(uiController: UiController, view: View) {
                with (view as DatePicker) {
                    date.set(year, month, dayOfMonth)
                }
            }
        })
        return date
    }

    /**
     * Get time from [TimePicker]
     * @return [Calendar] time from [TimePicker], represented by [ViewInteraction]
     */
    fun getTime(viewInteraction: ViewInteraction): Calendar {
        val time = Calendar.getInstance()
        viewInteraction.perform(object : ViewAction {
            override fun getConstraints() = isAssignableFrom(TimePicker::class.java)

            override fun getDescription() = "Get time from TimePicker: "

            override fun perform(uiController: UiController, view: View) {
                with (view as TimePicker) {
                    time.set(Calendar.HOUR_OF_DAY, hour)
                    time.set(Calendar.MINUTE, minute)
                }
            }
        })
        return time
    }

    /**
     * Get [String] by given resource id
     * @return The [String] text
     */
    fun getTextById(resourceId: Int): String {
        return InstrumentationRegistry.getTargetContext().resources.getString(resourceId)
    }

    /**
     * Check visibility of the given [ViewInteraction] component
     */
    fun viewIsDisplayed(viewInteraction: ViewInteraction): Boolean {
        var isDisplayed = true
        viewInteraction.withFailureHandler(object : FailureHandler {
            override fun handle(error: Throwable, viewMatcher: Matcher<View>) {
                isDisplayed = false
            }
        }).check(matches(isDisplayed()))
        return isDisplayed
    }

    /**
     * Matches the tag of the view.
     */
    fun withTag(tag: Int): Matcher<in View>? {
        return object : TypeSafeMatcher<View>(), Matcher<View> {

            override fun matchesSafely(view: View): Boolean {
                if (view.tag != null && view.tag is Int) {
                    return view.tag == tag
                } else {
                    return false
                }
            }

            override fun describeTo(description: Description) {
                description.appendText("with tag: $tag")
            }
        }
    }

    /**
     * Matches the text color of the view.
     */
    fun withTextColor(color: Int): Matcher<in View>? {
        return object : TypeSafeMatcher<View>(), Matcher<View> {

            override fun matchesSafely(view: View): Boolean {
                if (view is TextView && color == view.currentTextColor) {
                    return true
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("with text color $color")
            }

        }
    }

    /**
     * Matches the id and the string text of the view.
     */
    fun withIdAndText(id: Int, text: String): Matcher<View> {
        return allOf(withId(id), withText(text))
    }

    /**
     * Matches the id and the resource id text of the view.
     */
    fun withIdAndText(id: Int, textResourceId: Int): Matcher<View> {
        return allOf(withId(id), withText(textResourceId))
    }

    /**
     * Matches the id and the tag of the view.
     */
    fun withIdAndTag(id: Int, tag: Int): Matcher<View> {
        return allOf(withId(id), withTag(tag))
    }

    /**
     * Matches the id and the color of the text view.
     */
    fun withIdAndTextColor(id: Int, color: Int): Matcher<View> {
        return allOf(withId(id), withTextColor(color))
    }

    /**
     * Get [Int] color by resource id
     * @return The [Int] color
     */
    fun getColorById(resourceId: Int): Int {
        return ThemeUtil.getColor(CurrentActivityUtils.currentActivity, resourceId)
    }
}