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

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.CoordinatesProvider
import android.support.test.espresso.action.GeneralClickAction
import android.support.test.espresso.action.Press
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.v7.widget.RecyclerView
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import com.here.msdkuiapp.espresso.impl.core.CoreView.onLandingScreenList
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.Constants.DRIVE_NAVIGATION_ITEM
import com.here.msdkuiapp.espresso.impl.testdata.Constants.Gestures
import com.here.msdkuiapp.espresso.impl.testdata.Constants.Gestures.SINGLE_TAP
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ROUTE_PLANNER_ITEM
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ScreenOrientation
import com.here.msdkuiapp.espresso.impl.utils.ScreenOrientationUtils.Companion.setOrientation
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationBarView.onDriveNavigationBarTitleView
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions.DriveNavigationBarActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarRoutePlannerTitleView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerBarActions
import org.hamcrest.Matcher

/**
 * Core framework actions
 */
object CoreActions {

    /**
     * Single tap or Long Press on the view in given x & y percent.
     * @return [ViewAction]
     */
    fun tapIn(pctX: Double, pctY: Double, gestures: Gestures = SINGLE_TAP): ViewAction {
        return GeneralClickAction(
                gestures.value,
                CoordinatesProvider { view ->
                    val screenPos = IntArray(2)
                    view?.getLocationOnScreen(screenPos)
                    val x = view.width * pctX
                    val y = view.height * pctY
                    val screenX = (screenPos[0] + x).toFloat()
                    val screenY = (screenPos[1] + y).toFloat()
                    floatArrayOf(screenX, screenY)
                },
                Press.FINGER,
                InputDevice.SOURCE_TOUCHSCREEN,
                MotionEvent.TOOL_TYPE_FINGER)
    }

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
     * Press on the back button.
     */
    fun pressBackButton(): CoreActions {
        onRootView.perform(ViewActions.pressBack())
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
     * @param [ScreenOrientation] new value of the screen orientation
     */
    fun changeOrientation(orientation: ScreenOrientation): CoreActions {
        onRootView.perform(setOrientation(orientation))
        return this
    }
}