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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions

import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.recyclerview.widget.RecyclerView
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForCondition
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem
import com.here.msdkuiapp.espresso.impl.views.map.useractions.MapActions
import com.here.msdkuiapp.espresso.impl.views.route.matchers.RouteMatchers
import com.here.msdkuiapp.espresso.impl.views.route.utils.RouteData
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerBarMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerOptionsMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView.onPlannerBarChooseWaypointTitle
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onDatePicker
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onOkButton
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onOptionPanel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerTransportPanel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerTransportPanelView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointList
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointLocationLabel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onTimePicker
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onTravelTimePanel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.utils.WaypointData

/**
 *  Route planner panel actions
 */
object RoutePlannerActions {

    /**
     * Select waypoint by given geo coordinates item on route planner and on map
     */
    fun selectWaypoint(waypointData: WaypointData): WaypointData {
        waypointData.run {
            // Get current waypoint location information
            val waypoint = CoreMatchers.getText(onPlannerWaypointLocationLabel(waypointItem))
            // Select and check that waypoint item on route planner selected and visible
            RoutePlannerActions.tapWaypointOnPlanner(waypointItem).checkWaypointLocationLabel(waypoint)
            // Select the first waypoint on map
            MapActions.waitForMapViewEnabled().tap(waypointData)
            // Tap on tick to confirm the first waypoint selection
            return waypointData.copy(
                    waypoint = RoutePlannerBarActions
                            .waitForLocationNotDisplayed()
                            .waitForRightImageIconCheck()
                            .tapOnTickButton()
            )
        }
    }

    /**
     * Update waypoint item & route information list
     */
    fun updateRouteInformation(waypointData: WaypointData, routeData: RouteData) {
        // Update waypoint route planner item
        val waypoint = selectWaypoint(waypointData)
        // Expand route planner panel to update waypoints
        RoutePlannerBarActions.waitForRightImageIconExpand()
                .tapOnRightArrowButton()
                .waitForRoutePlannerExpanded()
        // Check route data updated in route list
        RouteMatchers.withUpdatedRouteData(routeData)
        // Check location of the waypoint item is correspond to selected on map view
        RoutePlannerMatchers.checkWaypointLocationLabel(waypoint)
    }

    /**
     * Tap on waypoint item on route planner
     */
    fun tapWaypointOnPlanner(waypointItem: WaypointItem): RoutePlannerBarMatchers {
        waypointItem.run {
            // Tap on waypoint item on Route planner if displayed
            onPlannerWaypointList.check(matches(isDisplayed()))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(index, click()))
            // Check that choose waypoint text is displayed
            onPlannerBarChooseWaypointTitle.check(matches(isDisplayed()))
        }
        return RoutePlannerBarMatchers
    }

    /**
     * Tap on travel time pane to open date picker
     */
    fun tapOnTravelTimePanel(): RoutePlannerActions {
        onTravelTimePanel.check(matches(isDisplayed())).perform(click())
        return this
    }

    /**
     * Tap on date picker 'OK' button
     */
    fun tapOKButton(): RoutePlannerActions {
        onOkButton.check(matches(isDisplayed())).perform(click())
        return this
    }

    /**
     * Select transportation mode type on route planner
     */
    fun selectTransportMode(transportType: TransportType): RoutePlannerBarActions {
        tapOnTransportMode(transportType)
                .waitTransportModeSelected(transportType)
                .checkTransModeSelected(transportType)
        return RoutePlannerBarActions
    }

    /**
     * Open Options panel on route planner
     */
    fun openOptionsPanel(): RoutePlannerOptionsMatchers {
        tapOnOptionImageIcon()
                .waitForOptionsDisplayed()
                .checkOptionsDisplayed()
        return RoutePlannerOptionsMatchers
    }

    /**
     * Check and Scroll Up & Down waypoints list on route planner
     */
    fun scrollUpAndDownWaypointsList(waypointData: WaypointData): RoutePlannerActions {
        // Swipe up waypoint list in case waypoint item is not visible
        if (!RoutePlannerMatchers.isVisibleWaypoint(waypointData)) swipeUpWaypointsList()
        // Check that waypoint item became visible after swiping
        RoutePlannerMatchers.checkWaypointLocationLabel(waypointData)
        // Swipe down waypoint list in case waypoint item is not visible
        if (!RoutePlannerMatchers.isVisibleWaypoint(WaypointData())) swipeDownWaypointsList()
        // Check that waypoint item became visible after swiping
        RoutePlannerMatchers.checkWaypointLocationLabel(WaypointData())
        return this
    }

    /**
     * Drag one waypoint to another in waypoints list
     */
    fun dragWaypoint(from: WaypointItem, to: WaypointItem) {
        var n = to.index - from.index
        if (n < 0) n-- else n++
        RoutePlannerView.onPlannerWaypointReorder(from).perform(CoreActions().swipeViewsUpDownFromCenter(n))
    }

    /**
     * Set travel date to tomorrow
     */
    fun setTravelDateTomorrow(): RoutePlannerActions {
        onDatePicker.perform(CoreActions().setTomorrowDate())
        return this
    }

    /**
     * Set travel time to one hour later
     */
    fun setTravelTime1HourLater(): RoutePlannerActions {
        onTimePicker.perform(CoreActions().setTime1HourLater())
        return this
    }

    /**
     * Tap on image option icon to open available routing options
     */
    private fun tapOnOptionImageIcon(): RoutePlannerBarActions {
        onOptionPanel.check(matches(isDisplayed())).perform(click())
        return RoutePlannerBarActions
    }

    /**
     * Tap on transportation type image icon on route planner
     */
    private fun tapOnTransportMode(transportMode: TransportType): RoutePlannerActions {
        onPlannerTransportPanel(transportMode).perform(click())
        return this
    }

    /**
     * Swipe Up waypoints list on route panel
     */
    private fun swipeUpWaypointsList(): RoutePlannerActions {
        onPlannerWaypointList.perform(swipeUp())
        return this
    }

    /**
     * Swipe Down waypoints list on route panel
     */
    private fun swipeDownWaypointsList(): RoutePlannerActions {
        onPlannerWaypointList.perform(swipeDown())
        return this
    }

    /**
     * Wait for route description list becomes enabled on route panel
     */
    private fun waitTransportModeSelected(transportMode: TransportType): RoutePlannerMatchers {
        onRootView.perform(waitForCondition(onPlannerTransportPanelView(transportMode), isSelected = true))
        return RoutePlannerMatchers
    }
}