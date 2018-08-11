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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions

import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeUp
import android.support.test.espresso.action.ViewActions.swipeDown
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.v7.widget.RecyclerView
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType.TYPE_CAR
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType.TYPE_TRUCK
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType.TYPE_WALK
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransporType.TYPE_BICYCLE
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem
import com.here.msdkuiapp.espresso.impl.views.map.useractions.MapActions
import com.here.msdkuiapp.espresso.impl.views.route.matchers.RouteMatchers
import com.here.msdkuiapp.espresso.impl.views.route.utils.RouteData
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerBarMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarChooseWaypointTitle
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onDatePickerOkButton
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onOptionPanel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerTransportPanel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointList
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointLocationLabel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onTravelTimePanel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.utils.WaypointData

/**
 *  Route planner panel actions
 */
object RoutePlannerActions {

    /**
     * Select waypoint item on route planner and on map
     */
    fun selectWaypoint(waypointData: WaypointData): WaypointData {
        waypointData.run {
            // Get current waypoint location information
            val waypoint = CoreMatchers.getTextView(onPlannerWaypointLocationLabel(waypointItem))
            // Select and check that waypoint item on route planner selected and visible
            RoutePlannerActions.tapWaipointOnPlanner(waypointItem).checkWaypoinLocationLabel(waypoint)
            // Select the first waypoint on map
            MapActions.waitForMapViewEnabled().tapOnMap(waypointData)
            // Tap on tick to confirm the first waypoint selection
            return waypointData.copy(waypoint = RoutePlannerBarActions.waitForLocationDisplayed().tapOnTickButton())
        }
    }

    /**
     * Update waypoint item & route information list
     */
    fun updateRouteInformation(waypointData: WaypointData, routeData: RouteData) {
        // Update waypoint route planner item
        val waypoint = selectWaypoint(waypointData)
        // Expand route planner panel to update waypoints
        RoutePlannerBarActions.waitForRoutePlannerCollapsed()
                .waitRouteDescriptionEnabled()
                .tapOnRighArrowButton()
                .waitForRoutePlannerExpanded()
        // Check route data updated in route list
        RouteMatchers.withUpdatedRouteData(routeData)
        // Check location of the waypoint item is correspond to selected on map view
        RoutePlannerMatchers.checkWaypointLocationLable(waypoint)
    }

    /**
     * Tap on waypoint item on route planner
     */
    fun tapWaipointOnPlanner(waipointItem: WaypointItem): RoutePlannerBarMatchers {
        waipointItem.run {
            // Tap on waypoint item on Route planner if displayed
            onPlannerWaypointList.check(matches(isDisplayed()))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(value, click()))
            // Check that choose waypoint text is displayed
            onPlannerBarChooseWaypointTitle.check(matches(isDisplayed()))
        }
        return RoutePlannerBarMatchers
    }

    /**
     * Tap on image option icon to open available routing options
     */
    fun tapOnOptionImage(): RoutePlannerBarMatchers {
        onOptionPanel.check(matches(isDisplayed())).perform(click())
        return RoutePlannerBarMatchers
    }

    /**
     * Tap on travel time pane to open date picker
     */
    fun tapOnTravelTimePanel(): RoutePlannerMatchers {
        onTravelTimePanel.check(matches(isDisplayed())).perform(click())
        return RoutePlannerMatchers
    }

    /**
     * Tap on date picker 'OK' button
     */
    fun tapOnDatePickerOKButton(): RoutePlannerActions {
        onDatePickerOkButton.check(matches(isDisplayed())).perform(click())
        return this
    }

    /**
     * Select transportation mode type on the panel
     */
    fun selectTransportMode(transportType: TransporType): RoutePlannerBarActions {
        when (transportType) {
            TYPE_CAR -> tapOnTransportMode(TYPE_CAR)
            TYPE_TRUCK -> tapOnTransportMode(TYPE_TRUCK)
            TYPE_WALK -> tapOnTransportMode(TYPE_WALK)
            TYPE_BICYCLE -> tapOnTransportMode(TYPE_BICYCLE)
        }
        return RoutePlannerBarActions
    }

    private fun tapOnTransportMode(transportMode: TransporType) {
        onPlannerTransportPanel(transportMode).perform(click())
    }

    /**
     * Swipe Up waypoints list on route panel
     */
    fun swipeUpWaypointsList(): RoutePlannerActions {
        onPlannerWaypointList.perform(swipeUp())
        return this
    }

    /**
     * Swipe Down waypoints list on route panel
     */
    fun swipeDownWaypointsList(): RoutePlannerActions {
        onPlannerWaypointList.perform(swipeDown())
        return this
    }

    /**
     * Check and Scroll Up & Down waypoints list on route planner
     */
    fun scrollUpAndDownWaypointsList(waypointData: WaypointData): RoutePlannerActions {
        // Swipe up waypoint list in case waypoint item is not visible
        if (!RoutePlannerMatchers.isVisibleWaypoint(waypointData)) swipeUpWaypointsList()
        // Check that waypoint item became visible after swiping
        RoutePlannerMatchers.checkWaypointLocationLable(waypointData)
        // Swipe down waypoint list in case waypoint item is not visible
        if (!RoutePlannerMatchers.isVisibleWaypoint(WaypointData())) swipeDownWaypointsList()
        // Check that waypoint item became visible after swiping
        RoutePlannerMatchers.checkWaypointLocationLable(WaypointData())
        return this
    }
}