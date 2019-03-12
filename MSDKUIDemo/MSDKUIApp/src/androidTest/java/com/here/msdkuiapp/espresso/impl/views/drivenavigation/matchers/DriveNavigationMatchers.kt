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

package com.here.msdkuiapp.espresso.impl.views.drivenavigation.matchers

import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.util.HumanReadables
import android.view.View
import android.widget.TextView
import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkui.routing.RouteUtil
import com.here.msdkuiapp.R
import com.here.msdkuiapp.common.Util
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getColorById
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.hasTextColor
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.isNumeric
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withColorFromDrawable
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIdAndTag
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIdAndText
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIdAndTextColor
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.Constants
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewDescriptionView
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewDestination
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewSeeManoeuvresNaviBtn
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewStartNaviBtn
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions.DriveNavigationActions.getNumberFromText
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.useractions.DriveNavigationActions.getTimeFromText
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceDashBoardAbout
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceDashBoardCurrentSpeed
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceDashBoardCurrentSpeedUnit
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceDashBoardCurrentSpeedValue
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceDashBoardSettings
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceNextManeuverDistanceInfo
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceNextManeuverDot
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceNextManeuverIcon
import com.here.msdkuiapp.espresso.impl.views.guidance.screens.GuidanceView.onGuidanceNextManeuverStreetNameInfo
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteListItemArrival
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteListItemDelayInformation
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteListItemDetails
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteListItemDuration
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerToText
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.util.concurrent.TimeoutException


object DriveNavigationMatchers {

    /**
     * Check interval is very short because we don't want to skip any quick maneuvers.
     */
    private const val MANEUVER_PANEL_TEST_CHECK_INTERVAL = 30L

    /**
     * Timeout for one maneuver is so long, because its hard to predict possible longest maneuver.
     */
    private const val MANEUVER_PANEL_TEST_SINGLE_MANEUVER_TIMEOUT = 10 * 60 * 1000L // 10 minutes

    /**
     * Check Route Description items on guidance route overview
     */
    fun checkRouteOverviewDescription(): DriveNavigationMatchers {
        // Check route arrival time on route description
        onRouteOverviewDescriptionView.check(matches(withRouteDescription(onRouteListItemArrival)))
        // Check route duration time on route description
        onRouteOverviewDescriptionView.check(matches(withRouteDescription(onRouteListItemDuration)))
        // Check route details on route description
        onRouteOverviewDescriptionView.check(matches(withRouteDescription(onRouteListItemDetails)))
        // Check route traffic delay information on route description
        onRouteOverviewDescriptionView.check(matches(withRouteDescription(onRouteListItemDelayInformation)))
        return this
    }

    /**
     * Check Route Description items displayed
     */
    fun checkRouteOverviewInfoDisplayed(): DriveNavigationMatchers {
        // Check destination (as address)
        onRouteListItemDetails.check(matches(isDisplayed()))
        // Check Estimation Time Arrival
        onRouteListItemArrival.check(matches(isDisplayed()))
        // Check Traffic warnings
        onRouteListItemDelayInformation.check(matches(isDisplayed()))
        // Check Route Duration
        onRouteListItemDuration.check(matches(isDisplayed()))
        // Check Route Short description
        onRouteOverviewSeeManoeuvresNaviBtn.check(matches(isDisplayed()))
        // Check Start Navigation Button
        onRouteOverviewStartNaviBtn.check(matches(isDisplayed()))

        return this
    }

    /**
     * Check Route Destination location address on guidance route overview
     */
    fun checkRouteOverviewDestination(location: String): DriveNavigationMatchers {
        val locationFormat = String.format("%s %s", onPlannerToText, location)
        onRouteOverviewDestination.check(matches(withText(locationFormat)))
        return this
    }

    /**
     * Check Maneuver Panel data: address and maneuver icon tag (icon resource id).
     */
    fun checkManeuverPanelData(address: String, iconTag: Int): DriveNavigationMatchers {
        val addressVisible = !address.isEmpty()
        val iconVisible = iconTag != 0

        // Two separate checks are made because waitForCondition() works only with one view.
        onRootView.perform(waitForCondition(
                withIdAndText(R.id.infoView2, address.split("towards", ignoreCase = false)[0].trim()),
                isVisible = addressVisible,
                checkInterval = MANEUVER_PANEL_TEST_CHECK_INTERVAL,
                timeout = MANEUVER_PANEL_TEST_SINGLE_MANEUVER_TIMEOUT
        ))
        onRootView.perform(waitForCondition(
                withIdAndTag(R.id.maneuverIconView, iconTag),
                isVisible = iconVisible,
                checkInterval = MANEUVER_PANEL_TEST_CHECK_INTERVAL,
                timeout = MANEUVER_PANEL_TEST_SINGLE_MANEUVER_TIMEOUT
        ))
        return this
    }

    /**
     * Check Maneuver Panel is destination reached (by checking address text color).
     * Address value is needed to know whether view is visible or not.
     */
    fun checkManeuverPanelIsDestinationReached(address: String): DriveNavigationMatchers {
        val addressVisible = !address.isEmpty()
        onRootView.perform(waitForCondition(
                withIdAndTextColor(R.id.infoView2, getColorById(com.here.msdkui.R.attr.colorAccentLight)),
                isVisible = addressVisible,
                checkInterval = MANEUVER_PANEL_TEST_CHECK_INTERVAL,
                timeout = MANEUVER_PANEL_TEST_SINGLE_MANEUVER_TIMEOUT
        ))
        return this
    }

    /**
     * Check is Current Street view value different than currentValue param.
     */
    fun checkCurrentStreetViewValueChanged(currentValue: String) {
        onRootView.perform(waitForCondition(
                allOf(withId(R.id.guidance_current_street_text), not(withText(currentValue))),
                CoreMatchers.TIMEOUT_WAIT_DOUBLE_MILLIS
        ))
    }

    /**
     * Check if guidance dashboard is expanded or collapsed
     */
    fun checkGuidanceDashBoardExpanded(isExpanded: Boolean = true) {
        if (isExpanded) {
            onGuidanceDashBoardSettings.check(matches(isDisplayed()))
            onGuidanceDashBoardAbout.check(matches(isDisplayed()))
        } else {
            onGuidanceDashBoardSettings.check(matches(not(isDisplayed())))
            onGuidanceDashBoardAbout.check(matches(not(isDisplayed())))
        }
    }

    /**
     * Check Current speed being displayed, having numeric value and proper text color
     */
    fun checkCurrentSpeed(isOverSpeeding: Boolean, screenOrientation: Constants.ScreenOrientation): DriveNavigationMatchers {
        val expectedTextColorAttrIdPortrait =
                if(isOverSpeeding) R.attr.colorNegative
                else R.attr.colorForeground
        val expectedViewDrawableIdLandscape =
                if (isOverSpeeding) R.drawable.current_speed_warning_bg_item
                else R.drawable.current_speed_normal_bg_item
        // Colors are also different depending on screen orientation
        if (screenOrientation == Constants.ScreenOrientation.PORTRAIT)
        onGuidanceDashBoardCurrentSpeedValue.check(matches(allOf(isDisplayed(),
                isNumeric(), hasTextColor(expectedTextColorAttrIdPortrait))))
        else
            onGuidanceDashBoardCurrentSpeed.check(matches(allOf(isDisplayed(),
                    withColorFromDrawable(expectedViewDrawableIdLandscape))))
            onGuidanceDashBoardCurrentSpeedValue.check(matches(allOf(isDisplayed(),
                    isNumeric(), hasTextColor(R.attr.colorForegroundLight))))
        return this
    }

    /**
     * Check Speed units being displayed and having proper text color
     */
    fun checkSpeedUnitsDisplayed(isOverSpeeding: Boolean, screenOrientation: Constants.ScreenOrientation): DriveNavigationMatchers {
        val expectedTextColorAttrIdPortrait =
                if (isOverSpeeding) R.attr.colorNegative
                else R.attr.colorForeground
        // Colors are also different depending on screen orientation
        if (screenOrientation == Constants.ScreenOrientation.PORTRAIT)
        onGuidanceDashBoardCurrentSpeedUnit.check(matches(allOf(isDisplayed(),
                hasTextColor(expectedTextColorAttrIdPortrait))))
        else
            onGuidanceDashBoardCurrentSpeedUnit.check(matches(allOf(isDisplayed(),
                    hasTextColor(R.attr.colorForegroundLight))))
        return this
    }


    /**
     * Check if guidance next maneuver panel elements are displayed
     */
    fun checkNextManeuverPanelElementsDisplayed() {
        onGuidanceNextManeuverIcon.check(matches(isDisplayed()))
        onGuidanceNextManeuverDistanceInfo.check(matches(isDisplayed()))
        onGuidanceNextManeuverDot.check(matches(isDisplayed()))
        onGuidanceNextManeuverStreetNameInfo.check(matches(isDisplayed()))
    }

    /**
     * Waits and checks ETA data during guidance by reading it from dashboard,
     * converting it to measurable type and comparing it initial values.
     */
    fun waitForETAChanged(): ViewAction {
        val timeout: Long = 30000
        val checkInterval: Long = 1000
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isRoot()
            }

            override fun getDescription(): String {
                return "wait ETA data to update correctly during $timeout timeout."
            }

            override fun perform(uiController: UiController, view: View) {
                uiController.loopMainThreadUntilIdle()
                val startTime = System.currentTimeMillis()
                val endTime = startTime + timeout

                //  Save initial ETA data
                val initialETA = getTimeFromText(view, view.findViewById<TextView>(R.id.eta)
                        .text.toString())
                val initialDuration = getNumberFromText(view, view.findViewById<TextView>(R.id.duration)
                        .text.toString())
                val initialDistance = getNumberFromText(view, view.findViewById<TextView>(R.id.distance)
                        .text.toString())
                do {
                    uiController.loopMainThreadForAtLeast(checkInterval)
                    if (
                            // ETA should stay same all the time
                            (initialETA == (getTimeFromText(view, view.findViewById<TextView>(R.id.eta)
                                    .text.toString())) ||
                            // In some cases Initial ETA can differ from guidance ETA
                            initialETA.before(getTimeFromText(view, view.findViewById<TextView>(R.id.eta)
                                    .text.toString()))) &&
                            // Duration should decrease over time
                            initialDuration > getNumberFromText(view, view.findViewById<TextView>(R.id.duration)
                                    .text.toString()) &&
                            // Distance should decrease over time
                            initialDistance > getNumberFromText(view, view.findViewById<TextView>(R.id.distance)
                                    .text.toString())) {
                        return
                    }
                } while (startTime < endTime)

                // Timeout happens
                throw PerformException.Builder()
                        .withActionDescription(this.description)
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(TimeoutException())
                        .build()
            }
        }
    }

    /**
     * Check transportation icons correspond to icons in description and maneuver lists' items
     */
    private fun withRouteDescription(viewMatcher: ViewInteraction): Matcher<View> {

        return object : TypeSafeMatcher<View>() {

            var actualValues = arrayListOf<String>()
            val expectedValue = CoreMatchers.getText(viewMatcher).trim()

            override fun matchesSafely(view: View): Boolean {

                val context = view.context.applicationContext
                val route = (view as RouteDescriptionItem).route

                with(actualValues) {
                    add(RouteUtil.getArrivalTime(context, route, true).toString().trim())
                    add(RouteUtil.getTimeToArrive(context, route, true).toString().trim())
                    add(RouteUtil.getTrafficDelayed(context, route).toString().trim())
                    add(RouteUtil.getDetails(context, route, Util.getLocaleUnit()).toString().trim())
                    return single { s -> s == expectedValue }.isNotEmpty()
                }
            }

            override fun describeTo(description: Description) {
                description.appendText("With route description text: $expectedValue")
            }
        }
    }
}