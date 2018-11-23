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

package com.here.msdkuiapp.espresso.impl.views.drivenavigation.matchers

import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import com.here.msdkui.routing.RouteUtil
import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getColorById
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIdAndTag
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIdAndText
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIdAndTextColor
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewDescriptionView
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewDestination
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewSeeManoeuvresNaviBtn
import com.here.msdkuiapp.espresso.impl.views.drivenavigation.screens.DriveNavigationView.onRouteOverviewStartNaviBtn
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescArrival
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescDelayInformation
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescDetails
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescDuration
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerToText
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object DriveNavigationMatchers {

    /**
     * Check transportation icons correspond to icons in description and maneuver lists' items
     */
    fun withRouteDescription(viewMatcher: ViewInteraction): Matcher<View> {

        return object : TypeSafeMatcher<View>() {

            var actualValues = arrayListOf<String>()
            val expectedValue = CoreMatchers.getTextView(viewMatcher).trim()

            override fun matchesSafely(view: View): Boolean {

                val context = view.context.applicationContext
                val route = (view as RouteDescriptionItem).route

                with(actualValues) {
                    add(RouteUtil.getArrivalTime(context, route,true).toString().trim())
                    add(RouteUtil.getTimeToArrive(context, route,true).toString().trim())
                    add(RouteUtil.getTrafficDelayed(context, route).toString().trim())
                    add(RouteUtil.getDetails(context, route).toString().trim())
                    return filter { s -> s == expectedValue }.single().isNotEmpty()
                }
            }

            override fun describeTo(description: Description) {
                description.appendText("With route description text: $expectedValue")
            }
        }
    }

    /**
     * Check Route Description items on guidance route overview
     */
    fun checkRouteOverviewDescription(): DriveNavigationMatchers {
        // Check route arrival time on route description
        onRouteOverviewDescriptionView.check(matches(withRouteDescription(onRouteDescArrival)))
        // Check route duration time on route description
        onRouteOverviewDescriptionView.check(matches(withRouteDescription(onRouteDescDuration)))
        // Check route details on route description
        onRouteOverviewDescriptionView.check(matches(withRouteDescription(onRouteDescDetails)))
        // Check route traffic delay information on route description
        onRouteOverviewDescriptionView.check(matches(withRouteDescription(onRouteDescDelayInformation)))
        return this
    }

    /**
     * Check Route Description items displayed
     */
    fun checkRouteOverviewInfoDisplayed(): DriveNavigationMatchers {
        // Check destination (as address)
        onRouteDescDetails.check(matches(isDisplayed()))
        // Check Estimation Time Arrival
        onRouteDescArrival.check(matches(isDisplayed()))
        // Check Traffic warnings
        onRouteDescDelayInformation.check(matches(isDisplayed()))
        // Check Route Duration
        onRouteDescDuration.check(matches(isDisplayed()))
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
     * Check interval is very short because we don't want to skip any quick maneuvers.
     */
    private const val MANEUVER_PANEL_TEST_CHECK_INTERVAL = 30L

    /**
     * Timeout for one maneuver is so long, because its hard to predict possible longest maneuver.
     */
    private const val MANEUVER_PANEL_TEST_SINGLE_MANEUVER_TIMEOUT = 10 * 60 * 1000L // 10 minutes

    /**
     * Check Maneuver Panel data: address and maneuver icon tag (icon resource id).
     */
    fun checkManeuverPanelData(address: String, iconTag: Int): DriveNavigationMatchers {
        val addressVisible = !address.isEmpty()
        val iconVisible = iconTag != 0

        // Two separate checks are made because waitForCondition() works only with one view.
        onRootView.perform(waitForCondition(
                withIdAndText(R.id.infoView2, address),
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

}