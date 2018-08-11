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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.isSelected
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getTextView
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onDatePicker
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerTransportPanel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointLocationLabel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.utils.WaypointData
import android.view.View
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkui.routing.WaypointList
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType.TYPE_CAR
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType.TYPE_BICYCLE
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType.TYPE_WALK
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType.TYPE_TRUCK
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerFromTextView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerToTextView
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.anyOf
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
    fun checkTransModeSelected(transportMode: TransporType): RoutePlannerActions {
        when (transportMode) {
            TYPE_CAR -> isTransportModeSelected(TYPE_CAR)
            TYPE_TRUCK -> isTransportModeSelected(TYPE_TRUCK)
            TYPE_WALK -> isTransportModeSelected(TYPE_WALK)
            TYPE_BICYCLE -> isTransportModeSelected(TYPE_BICYCLE)
        }
        return RoutePlannerActions
    }

    /**
     * Check transportation mode type is not selected on the panel
     */
    fun checkTransModeNotSelected(transportMode: TransporType): RoutePlannerActions {
        when (transportMode) {
            TYPE_CAR -> isTransportModeNotSelected(TYPE_CAR)
            TYPE_TRUCK -> isTransportModeNotSelected(TYPE_TRUCK)
            TYPE_WALK -> isTransportModeNotSelected(TYPE_WALK)
            TYPE_BICYCLE -> isTransportModeNotSelected(TYPE_BICYCLE)
        }
        return RoutePlannerActions
    }

    /**
     * Check date picker opened
     */
    fun checkDatePickerDisplayed(): RoutePlannerActions {
        onDatePicker.check(matches(isDisplayed()))
        return RoutePlannerActions
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
    fun checkWaypointLocationLable(waypointData: WaypointData): RoutePlannerMatchers {
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
            getTextView(onPlannerWaypointLocationLabel(waypointItem)).contentEquals(waypoint)
        }
    }

    /**
     * Check waypoint list order matches with giver waypoints [List]<[String]> on route planner
     */
    fun isWaypointsReversed(expectedWaypoints: List<String>): Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            var actualWaypoints = arrayListOf<String>()

            override fun matchesSafely(view: View): Boolean {
                val waypointAdapter = view as WaypointList
                actualWaypoints.addAll(extractWaypointNames(waypointAdapter.entries))
                return expectedWaypoints.filterIndexed { i, s -> !actualWaypoints[i].contentEquals(s) }.isEmpty()
            }

            private fun extractWaypointNames(waypoints: List<WaypointEntry>): List<String> {
                val waypointNames = arrayListOf<String>()
                waypoints.forEach { waypointNames.add(it.name) }
                return waypointNames
            }

            override fun describeTo(description: Description) {
                description.appendText("has waypoint list items order reversed: $actualWaypoints[]")
            }
        }
    }

    private fun isTransportModeSelected(transportMode: TransporType) {
        onPlannerTransportPanel(transportMode).check(matches(isSelected()))
    }

    private fun isTransportModeNotSelected(transportMode: TransporType) {
        onPlannerTransportPanel(transportMode).check(matches(not(isSelected())))
    }
}