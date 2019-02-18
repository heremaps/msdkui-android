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

package com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.isRoot
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.espresso.matcher.ViewMatchers.withParent
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.view.View
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.isNumeric
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.Constants
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.matchers.DriveNavigationMatchers
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewDescription
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewSeeManoeuvresNaviBtn
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewStartNaviBtn
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewStartSimulationOkBtn
import com.here.msdkuiapp.espresso.impl.views.guidance.useractions.GuidanceActions
import com.here.msdkuiapp.guidance.GuidanceRouteSelectionActivity
import com.here.msdkuiapp.guidance.GuidanceRouteSelectionCoordinator
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * Drive Navigation & Overview related actions
 */
object DriveNavigationActions {

    /**
     * Tap on start navigation button to open guidance view
     */
    fun tapOnStartNavigationBtn(): GuidanceActions {
        onRouteOverviewStartNaviBtn.check(matches(isDisplayed())).perform(click())
        return GuidanceActions
    }

    /**
     * Start navigation simulation with defined [simulationSpeed]
     *
     * @param simulationSpeed simulation speed in m/s. 0 - fallback to default simulation speed
     */
    fun startNavigationSimulation(simulationSpeed: Long = 0): GuidanceActions {
        if (simulationSpeed > 0) {
            onRootView.perform(object: ViewAction {
                override fun getDescription(): String {
                    return "Set simulation speed to $simulationSpeed m/s. "
                }

                override fun getConstraints(): Matcher<View> {
                    return isRoot()
                }

                override fun perform(uiController: UiController?, view: View) {
                    ((view.context as GuidanceRouteSelectionActivity)
                            .coordinator as GuidanceRouteSelectionCoordinator)
                            .routePreviewFragment!!.presenter.simulationSpeed = simulationSpeed
                }

            })
        }
        onRouteOverviewStartNaviBtn.check(matches(isDisplayed())).perform(longClick())
        onRouteOverviewStartSimulationOkBtn.check(matches(isDisplayed())).perform(click())
        return GuidanceActions
    }

    /**
     * Wait for guidance view displayed on action bar
     */
    fun waitForGuidanceDescriptionDisplayed(): DriveNavigationMatchers {
        onRootView.perform(waitForCondition(onRouteOverviewDescription))
        return DriveNavigationMatchers
    }

    /**
     * Wait for guidance dashboard to expand
     */
    fun waitGuidanceDashBoardExpand(): DriveNavigationActions {
        onRootView.perform(waitForCondition(allOf(withText(R.string.msdkui_app_about), withParent(withParent(withId(R.id.items_list))))))
        return this
    }

    /**
     * Wait for guidance dashboard to collapse
     */
    fun waitGuidanceDashBoardCollapse(): DriveNavigationActions {
        onRootView.perform(waitForCondition(allOf(withText(R.string.msdkui_app_settings), withParent(withParent(withId(R.id.items_list))), not(isDisplayed()))))
        return this
    }

    /**
     * Wait for current speed to get value
     */
    fun waitCurrentSpeedValue(): DriveNavigationActions {
        onRootView.perform(waitForCondition(allOf(withId(R.id.guidance_current_speed_value), isNumeric())))
        return this
    }

    /**
     * Click "See maneuvers" button to show maneuvers list
     */
    fun tapOnSeeManeuversBtn(): GuidanceActions {
        onRouteOverviewSeeManoeuvresNaviBtn.check(matches(isDisplayed())).perform(click())
        return GuidanceActions
    }

    /**
     * Converts text from a given string to time.
     *
     * @param view view which is currently being worked on.
     * @param text string which will be converted.
     */
    fun getTimeFromText(view: View, text: String): LocalTime {
        return LocalTime.parse(text, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    }

    /**
     * Converts text from a given string to a float.
     *
     * @param view view which is currently being worked on.
     * @param text string which will be converted.
     */
    fun getNumberFromText(view: View, text: String): Float {
        return text.replace(Regex(Constants.ETA_PATTERN), "")
                .replace(Regex(","), ".").toFloat()
    }

    /**
     * Wait until ETA data is or is not being displayed
     * 
     * @param isDisplayed expect ETA to be displayed or not.
     */
    fun waitForETAData(isDisplayed: Boolean): DriveNavigationActions {
        val noData = "--"
        val etaData= listOf(R.id.eta, R.id.duration, R.id.distance)
        for (i in etaData)
            if (isDisplayed == false) {
                onRootView.perform(waitForCondition(CoreMatchers.withIdAndText(
                        i, noData), 250000, 2000))
            } else {
                onRootView.perform(CoreMatchers.waitForTextChange(
                        withId(i), noData, 2000, 100))
            }
        return this
    }
}