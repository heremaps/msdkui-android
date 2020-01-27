/*
 * Copyright (C) 2017-2020 HERE Europe B.V.
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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers

import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import android.view.View
import com.here.msdkui.routing.*
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getText
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onCancelButton
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onDatePicker
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onOkButton
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerFromTextView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerToTextView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerTransportPanel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointLocationLabel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onTimePicker
import com.here.msdkuiapp.espresso.impl.views.routeplanner.utils.WaypointData
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Route planner panel matchers
 */
object RoutePlannerMatchers {

    /**
     * Check transportation mode type is selected on the panel
     */
    fun checkTransModeSelected(transportMode: TransportType): RoutePlannerMatchers {
        onPlannerTransportPanel(transportMode).check(matches(isSelected()))
        return this
    }

    /**
     * Check transportation mode type is not selected on the panel
     */
    fun checkTransModeNotSelected(transportMode: TransportType): RoutePlannerMatchers {
        onPlannerTransportPanel(transportMode).check(matches(not(isSelected())))
        return this
    }

    /**
     * Check date picker opened
     */
    fun checkDatePickerDisplayed(): RoutePlannerMatchers {
        onDatePicker.check(matches(isDisplayed()))
        return this
    }

    /**
     * Check time picker opened
     */
    fun checkTimePickerDisplayed(): RoutePlannerMatchers {
        onTimePicker.check(matches(isDisplayed()))
        return this
    }

    /**
     * Check 'OK' button visible
     */
    fun checkOkButtonDisplayed(): RoutePlannerMatchers {
        onOkButton.check(matches(isDisplayed()))
        return this
    }

    /**
     * Check 'Cancel' button visible
     */
    fun checkCancelButtonDisplayed(): RoutePlannerMatchers {
        onCancelButton.check(matches(isDisplayed()))
        return this
    }

    /**
     * Check default waypoint location label text view on route planner
     */
    fun checkWaypointDefaultLabel(item: WaypointItem): RoutePlannerMatchers {
        onPlannerWaypointLocationLabel(item).check(matches(anyOf(onPlannerFromTextView, onPlannerToTextView)))
        return this
    }

    /**
     * Check waypoint location label text view by given [String] on route planner
     */
    fun checkWaypointLocationLabel(waypointData: WaypointData): RoutePlannerMatchers {
        waypointData.run {
            onPlannerWaypointLocationLabel(waypointItem).check(matches(withText(waypoint)))
        }
        return this
    }

    /**
     * Check waypoint location matches with given [WaypointData] on route planner
     */
    fun isVisibleWaypoint(waypointData: WaypointData): Boolean {
        return waypointData.run {
            getText(onPlannerWaypointLocationLabel(waypointItem)).contentEquals(waypoint)
        }
    }

    /**
     * Check waypoint list order matches with giver waypoints [List]<[String]> on route planner
     */
    fun isWaypointsReversed(expectedWaypoints: List<String>): Matcher<View> {
        return object: TypeSafeMatcher<View>() {

            var actualWaypoints = arrayListOf<String>()

            override fun matchesSafely(view: View): Boolean {
                val waypointAdapter = view as WaypointList
                actualWaypoints.addAll(extractWaypointNames(waypointAdapter.entries))
                return expectedWaypoints.filterIndexed { i, s -> !actualWaypoints[i].contentEquals(s) }.isEmpty()
            }

            private fun extractWaypointNames(waypoints: List<WaypointEntry>): List<String> {
                val waypointNames = arrayListOf<String>()
                waypoints.forEach { waypointNames.add(it.stringLabel!!) }
                return waypointNames
            }

            override fun describeTo(description: Description) {
                description.appendText("Has waypoint list items order reversed: $actualWaypoints[]")
            }
        }
    }

    /**
     * Check transportation icons correspond to icons in description and maneuver lists' items
     */
    fun withDrawableIcons(expectedId: Int, item: Int? = null): Matcher<View> {

        return object : TypeSafeMatcher<View>() {

            var resourceName: String? = null

            override fun matchesSafely(view: View): Boolean {
                val resources = view.context.resources
                val expectedDrawable = resources.getDrawable(expectedId)
                resourceName = resources.getResourceEntryName(expectedId)

                return expectedDrawable != null && getActualId(view) == expectedId
            }

            override fun describeTo(description: Description) {
                description.appendText("With drawable from resource id: ")
                description.appendValue(expectedId)
                description.appendText(String.format("[ %s ]", resourceName))
            }

            private fun getActualId(view: View): Int {
                val applicationContext = view.context.applicationContext
                val route = if (item != null) (view as RouteDescriptionList).routes[item] else (view as RouteDescriptionItem).route

                return RouteUtil.getIcon(route)
            }
        }
    }
}