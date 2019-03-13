/*
 * Copyright (C) 2017-2019 HERE Europe B.V.
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

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.*
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.MapEngine
import com.here.msdkuiapp.espresso.impl.core.CoreView.onLandingScreenList
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.Constants.DRIVE_NAVIGATION_ITEM
import com.here.msdkuiapp.espresso.impl.testdata.Constants.GEO_POINT_0
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ROUTE_PLANNER_ITEM
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ScreenOrientation
import com.here.msdkuiapp.espresso.impl.utils.ScreenOrientationUtils.Companion.getOrientation
import com.here.msdkuiapp.espresso.impl.utils.ScreenOrientationUtils.Companion.setOrientation
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationBarView.onDriveNavigationBarTitleView
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions.DriveNavigationBarActions
import com.here.msdkuiapp.espresso.impl.views.map.utils.MapData
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarRoutePlannerTitleView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerBarActions
import com.here.msdkuiapp.position.AppPositioningManager
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import java.util.concurrent.TimeoutException

/**
 * Core framework actions
 */
open class CoreActions {

    /**
     * Clicks the view without checking any constraint like visibility & others.
     */
    fun clickWithNoConstraint(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isEnabled()
            }

            override fun getDescription(): String {
                return "click view"
            }

            override fun perform(uiController: UiController, view: View) {
                view.performClick()
            }
        }
    }

    /**
     * An action that performs a swipe from center to [n] view heights
     * above or below the view center. n > 0 - swipe down, n < 0 - swipe up
     */
    fun swipeViewsUpDownFromCenter (n: Int): ViewAction {
        val nHeightsAboveBelowCenter = CoordinatesProvider { view ->
            val locationOnScreen = IntArray(2)
            view.getLocationOnScreen(locationOnScreen)
            val x: Float = locationOnScreen[0] + view.width / 2.0f
            val y: Float = locationOnScreen[1] + view.height / 2.0f + n * view.height.toFloat()
            floatArrayOf(x, y)
        }
        return actionWithAssertions(GeneralSwipeAction(
                Swipe.FAST,
                GeneralLocation.CENTER,
                nHeightsAboveBelowCenter,
                Press.FINGER
        ))
    }

    /**
     * Set date of [DatePicker] to tomorrow
     */
    fun setTomorrowDate(): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set Date to tomorrow"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf<View>(isAssignableFrom(DatePicker::class.java), isDisplayed())
            }

            override fun perform(uiController: UiController, view: View) {
                with(view as DatePicker) {
                    updateDate(year, month, dayOfMonth + 1)
                }
            }
        }
    }

    /**
     * Set time of [TimePicker] to one hour later
     */
    fun setTime1HourLater(): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set Time to 1 hour later"
            }

            override fun getConstraints(): Matcher<View> {
                return allOf<View>(isAssignableFrom(TimePicker::class.java), isDisplayed())
            }

            override fun perform(uiController: UiController, view: View) {
                with(view as TimePicker) {
                    hour += 1
                }
            }
        }
    }

    /**
     * Press on the back button.
     */
    fun pressBackButton(): CoreActions {
        onRootView.perform(pressBack())
        return this
    }

    /**
     * Tap on Route Planner heading item on landing screen
     */
    fun enterRoutePlanner(): CoreActions {
        // Tap on route planer item on landing screen if displayed
        onLandingScreenList.perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(ROUTE_PLANNER_ITEM, click()))
        // Check that route planner expanded
        RoutePlannerBarActions.waitForRoutePlannerExpanded()
        // Check default route planner title is displayed
        onPlannerBarRoutePlannerTitleView.check(matches(isDisplayed()))
        return this
    }

    /**
     * Tap on Driver Navigation heading item on landing screen
     */
    fun enterDriveNavigation(): CoreActions {
        // Tap on drive navigation item on landing screen if displayed
        onLandingScreenList.perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(DRIVE_NAVIGATION_ITEM, click()))
        // Check drive navigation view opened
        DriveNavigationBarActions.waitForDriveNavigationView()
        // Check default drive navigation title is displayed
        onDriveNavigationBarTitleView.check(matches(isDisplayed()))
        return this
    }

    /**
     * Change the value of the current activity
     * @param [orientation] new value of the screen orientation
     */
    fun changeOrientation(orientation: ScreenOrientation): CoreActions {
        if (getOrientation() != orientation.value) onRootView.perform(setOrientation(orientation))
        return this
    }

    /**
     * Inject coordinates into PositioningManager.
     *
     * @param off add or remove current location (default: false)
     * @param locationData latitude and longitude (default: HERE Berlin)
     */
    fun setCurrentLocation(off: Boolean = false, locationData: MapData = GEO_POINT_0): CoreActions {
        waitForMap()
        // Set or reset current position
        AppPositioningManager.getInstance().apply {
            if(off) stop()
            else
                customLocation = GeoCoordinate(locationData.lat, locationData.lng)
        }
        return this
    }


    /**
     * Wait for map engine to be initialized
     */
     private fun waitForMap() {
        with(System.currentTimeMillis()) {
            val endTime = this + 3000
            do {
                if(MapEngine.isInitialized())
                    return
            } while (this < endTime)
        }
        throw TimeoutException("Wait for MapEngine timed out")
    }
}