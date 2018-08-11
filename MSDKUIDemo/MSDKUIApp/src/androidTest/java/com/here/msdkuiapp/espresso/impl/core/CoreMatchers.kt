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
import android.support.test.espresso.util.HumanReadables
import android.support.test.espresso.util.TreeIterables
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.TimeoutException

/**
 * Core framework matchers
 */
object CoreMatchers {

    private const val TIMEOUT_WAIT_SEC: Long = 60
    private const val TIMEOUT_DELAY_MILLIS: Long = 1500
    private val TIMEOUT_WAIT_MILLIS: Long = SECONDS.toMillis(TIMEOUT_WAIT_SEC)

    /**
     * Waits util given view is visible OR timeout happens.
     * TODO: MSDKUI-496, MSDKUI-561 - remove this method when idling resource implemented
     */
    fun waitForCondition(viewMatcher: Matcher<View>, timeout: Long = TIMEOUT_WAIT_MILLIS,
                         isVisible: Boolean = true, isEnabled: Boolean = false): ViewAction {
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
                    uiController.loopMainThreadForAtLeast(TIMEOUT_DELAY_MILLIS)
                    for (child in TreeIterables.breadthFirstViewTraversal(view)) {
                        if (isEnabled && viewMatcher.matches(child) && child.isEnabled) {
                            return
                        }
                        if (isVisible && viewMatcher.matches(child)) {
                            return
                        }
                        if (!isVisible && !viewMatcher.matches(isDisplayed())) {
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
            internal var currentIndex = 0

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
                return (view as RecyclerView).adapter.itemCount == size
            }

            override fun describeTo(description: Description) {
                description.appendText("RecyclerView should have $size items")
            }
        }
    }

    /**
     * Matches the text view with given [ViewInteraction]
     */
    fun getTextView(viewInteraction: ViewInteraction): String {
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
     * @return The [String] text by given resource id
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
}