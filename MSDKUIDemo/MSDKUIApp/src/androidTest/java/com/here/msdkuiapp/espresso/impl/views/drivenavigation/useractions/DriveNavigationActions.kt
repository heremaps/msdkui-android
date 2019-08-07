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

import android.app.Activity
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import androidx.core.os.ConfigurationCompat
import android.view.View
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getTextById
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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

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
    fun startNavigationSimulation(simulationSpeed: Long = 0): DriveNavigationActions {
        if (simulationSpeed > 0) {
            onRootView.perform(object: ViewAction {
                override fun getDescription(): String {
                    return "Set simulation speed to $simulationSpeed m/s. "
                }

                override fun getConstraints(): Matcher<View> {
                    return isRoot()
                }

                override fun perform(uiController: UiController?, view: View) {
                    ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).apply {
                        with(iterator().next() as Activity) {
                            ((this as GuidanceRouteSelectionActivity)
                                    .coordinator as GuidanceRouteSelectionCoordinator)
                                    .routePreviewFragment?.presenter?.simulationSpeed = simulationSpeed
                        }
                    }
                }
            })
        }
        onRouteOverviewStartNaviBtn.check(matches(isDisplayed())).perform(longClick())
        onRouteOverviewStartSimulationOkBtn.check(matches(isDisplayed())).perform(click())
        return DriveNavigationActions
    }

    /**
     * Wait for guidance view displayed on action bar
     */
    fun waitForGuidanceDescriptionDisplayed(): DriveNavigationMatchers {
        onRootView.perform(waitForCondition(onRouteOverviewDescription))
        return DriveNavigationMatchers
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
    fun getTimeFromText(view: View, text: String): Date {
        // Possible used time formats on guidance ETA
        val dates = listOf("h:mm a", "hh:mm", "HH:mm")
        var date: Date? = null
        for (i in dates) {
            try {
               date = SimpleDateFormat(i, ConfigurationCompat
                        .getLocales(view.context.resources.configuration)[0])
                        .parse(text)
            } catch (e: ParseException) { }
            if (date != null)
                return date
        }
        throw ParseException("Date could not be parsed", 0)
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
        val noEtaData = getTextById(R.string.msdkui_value_not_available)
        if (!isDisplayed) {
            onRootView.perform(waitForCondition(CoreMatchers.withIdAndText(
                    R.id.eta, noEtaData), 250000, 2000))
            //When ETA status is confirmed check distance and duration immediately
            listOf(R.id.distance, R.id.duration).forEach {
                withId(it).matches(withText(noEtaData))
            }
        } else {
            listOf(R.id.eta, R.id.distance, R.id.duration).forEach {
                onRootView.perform(CoreMatchers.waitForTextChange(
                        withId(it), noEtaData, 2000, 100))
            }
        }
        return this
    }
}