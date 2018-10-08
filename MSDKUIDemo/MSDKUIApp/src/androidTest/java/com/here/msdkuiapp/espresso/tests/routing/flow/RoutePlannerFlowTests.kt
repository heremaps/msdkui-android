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

package com.here.msdkuiapp.espresso.tests.routing.flow

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.SplashActivity
import com.here.msdkuiapp.espresso.impl.annotation.FunctionalUITest
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getTextView
import com.here.msdkuiapp.espresso.impl.testdata.Constants.MAP_POINT_2
import com.here.msdkuiapp.espresso.impl.testdata.Constants.MAP_POINT_3
import com.here.msdkuiapp.espresso.impl.testdata.Constants.MAP_POINT_4
import com.here.msdkuiapp.espresso.impl.testdata.Constants.MAP_POINT_5
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ROUTE_RESULT_1
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.RemoveWaypointBtn.REMOVE_WAYPOINT_BTN3
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_CAR
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_SCOOTER
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_BICYCLE
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_WALK
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.TYPE_TRUCK
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem.WAYPOINT_1
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem.WAYPOINT_2
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem.WAYPOINT_3
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem.WAYPOINT_4
import com.here.msdkuiapp.espresso.impl.views.map.screens.MapView.onMapFragmentWrapper
import com.here.msdkuiapp.espresso.impl.views.map.useractions.MapActions
import com.here.msdkuiapp.espresso.impl.views.map.utils.MapData
import com.here.msdkuiapp.espresso.impl.views.route.matchers.RouteMatchers
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescArrival
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescDealyInformation
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescDetails
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescDuration
import com.here.msdkuiapp.espresso.impl.views.route.useractions.RouteActions
import com.here.msdkuiapp.espresso.impl.views.route.useractions.RouteBarActions
import com.here.msdkuiapp.espresso.impl.views.route.utils.RouteData
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerMatchers.isWaypointsReversed
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointList
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerActions.updateRouteInformation
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerBarActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.utils.WaypointData
import com.here.msdkuiapp.espresso.tests.TestBase
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

/**
 * UI flow tests for routing
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RoutePlannerFlowTests: TestBase<SplashActivity>(SplashActivity::class.java) {

    private lateinit var waypoint1: WaypointData
    private lateinit var waypoint2: WaypointData

    @Before
    fun prepare() {
        waypoint1 = WaypointData(MapData.randMapPoint)
        waypoint2 = WaypointData(MapData.randMapPoint, WAYPOINT_2)
        CoreActions().enterRoutePlanner()
    }

    /**
     * MSDKUI-126: Select a waypoint on the map
     */
    @Test
    @FunctionalUITest
    fun testSelectAWaypointOnTheMap() {
        // Map view and Route planner are opened
        RoutePlannerBarActions.waitForRoutePlannerExpanded()
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Check that route planner does not collapsed
        RoutePlannerBarActions.waitForRoutePlannerExpanded()
        // Check that transportation type: 'Car' is selected by default
        RoutePlannerMatchers.checkTransModeSelected(TYPE_CAR)
    }

    /**
     * MSDKUI-133: Create a route with two waypoints
     */
    @Test
    @FunctionalUITest
    fun testCreateARouteWithTwoWaypoints() {
        // Map view and Route planner are opened
        RoutePlannerBarActions.waitForRoutePlannerExpanded()
        // Select first waipoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Check that route planner expanded
        RoutePlannerBarActions.waitForRoutePlannerExpanded()
        // Select second waipoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Check route planner collapsed
        RoutePlannerBarActions.waitForRoutePlannerCollapsed().waitRouteDescriptionList()
        // Check that transportation type: 'Car' is selected by default
        RoutePlannerMatchers.checkTransModeSelected(TYPE_CAR)
    }

    /**
     * MSDKUI-137: Select different transportation modes
     */
    @Test
    @FunctionalUITest
    fun testSelectDifferentTransportationModes() {
        // Select first waipoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waipoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Wait for panel collapsed and routes description list is visible
        RoutePlannerBarActions.waitForRoutePlannerCollapsed().waitRouteDescriptionList()
        // Check that transportation type: 'Car' is selected by default and others not selected
        RoutePlannerMatchers.checkTransModeSelected(TYPE_CAR)
                .checkTransModeNotSelected(TYPE_TRUCK)
                .checkTransModeNotSelected(TYPE_WALK)
                .checkTransModeNotSelected(TYPE_BICYCLE)
                .checkTransModeNotSelected(TYPE_SCOOTER)
        // Check all existing transportation types
        enumValues<TransportType>().forEach {
            // Select next transportation type and check the type is selected
            RoutePlannerActions.selectTransportMode(it)
                    .waitForRoutePlannerCollapsed()
                    .waitRouteDescriptionEnabled()
            // Check all transportation image icons for the selected transportation type
            RouteMatchers.checkAllTransportIconDisplayed(it)
        }
    }

    /**
     * MSDKUI-152: Maneuver description
     */
    @Test
    @FunctionalUITest
    fun testManeuverDescription() {
        // Select first waipoint item
        RoutePlannerActions.selectWaypoint(WaypointData())
        // Select second waipoint item
        RoutePlannerActions.selectWaypoint(WaypointData(MAP_POINT_5, WAYPOINT_2))
        // Wait for panel collapsed and routes description list is visible
        RoutePlannerBarActions.waitForRoutePlannerCollapsed().waitRouteDescriptionList()
        // Check route list information for all transportation types
        enumValues<TransportType>().forEach {
            // Select next transportation type
            RoutePlannerActions.selectTransportMode(it)
                    .waitForRoutePlannerCollapsed()
                    .waitRouteDescriptionEnabled()
            // Tap on the first route item from route list to open route overview
            RouteActions.tapRouteItemtOnDescList(ROUTE_RESULT_1)
                    .waitRouteOverviewDisplayed()
                    .checkManeuverResultList()
                    .tapOnBackArrowButton()
        }
    }

    /**
     * MSDKUI-150: Route description
     */
    @Test
    @FunctionalUITest
    fun testRouteDescription() {
        // Select first waipoint item
        RoutePlannerActions.selectWaypoint(WaypointData())
        // Select second waipoint item
        RoutePlannerActions.selectWaypoint(WaypointData(MAP_POINT_4, WAYPOINT_2))
        // Wait for panel collapsed and routes description list is visible
        RoutePlannerBarActions.waitForRoutePlannerCollapsed().waitRouteDescriptionList()
        // Check route list information for all transportation types
        enumValues<TransportType>().forEach {
            // Select next transportation type
            RoutePlannerActions.selectTransportMode(it)
                    .waitForRoutePlannerCollapsed()
                    .waitRouteDescriptionEnabled()
            // Wait routes description list and check routes results
            RouteActions.checkRouteResultsList(it).swipeDownRouteList()
            // Save information of the first route item list
            val routeData = saveRouteInformation(ROUTE_RESULT_1, it)
            // Tap on the first route item from route list to open route overview
            RouteActions.tapRouteItemtOnDescList(ROUTE_RESULT_1)
                    .waitRouteOverviewDisplayed()
                    .checkRouteOverviewItemsDisplayed(it)
//                    .withRouteOverviewData(routeData) // FIXME: MSDKUI-919
            RouteBarActions.tapOnBackArrowButton()
        }
    }

    /**
     * MSDKUI-140: Scroll route list state (multiple routes count)
     */
    @Test
    @FunctionalUITest
    fun testScrollRouteListStateMultipleRoutesCount() {
        // Select first waipoint item
        RoutePlannerActions.selectWaypoint(WaypointData())
        // Select second waipoint item
        RoutePlannerActions.selectWaypoint(WaypointData(MAP_POINT_4, WAYPOINT_2))
        // Wait for panel collapsed and routes description list is visible
        RoutePlannerBarActions.waitForRoutePlannerCollapsed().waitRouteDescriptionList()
        // Check route list information for all transportation types
        enumValues<TransportType>().forEach {
            // Select next transportation type
            RoutePlannerActions.selectTransportMode(it)
                    .waitForRoutePlannerCollapsed()
                    .waitRouteDescriptionEnabled()
            // Expand route planner panel to update waypoints
            RoutePlannerBarActions.waitForRightImageIconExpand()
                    .tapOnRightArrowButton()
                    .waitForRoutePlannerExpanded()
            // Check Scroll Up & Down routes results list
            RouteActions.scrollUpAndDownRouteList()
        }
    }

    /**
     * MSDKUI-145: Scroll waypoint list
     */
    @Test
    @FunctionalUITest
    fun testScrollWaypointList() {
        // Add waypoints to Route Planner list
        enumValues<WaypointItem>().forEach {
            // Skip two first waypoint items, because can't be add/remove
            when (it) { WAYPOINT_1, WAYPOINT_2 -> return@forEach }
            // Create waypoint to add on route planner
            val waypointData = MapData.run { WaypointData(randMapPoint, it) }
            // Tap on Add/Plus additional waypoint image button
            RoutePlannerBarActions.waitForAddWaypointImageButton().tapOnAddWaypointButton()
            // Select a waypoint on map
            MapActions.waitForMapViewEnabled().tapOnMap(waypointData)
            // Tap on tick to confirm the first waypoint selection
            val location = RoutePlannerBarActions.waitForLocationNotDisplayed()
                    .waitForRightImageIconCheck()
                    .tapOnTickButton()
            // Scroll Up & Down waypoints list in case of new waypoint item is not visible
            RoutePlannerActions.scrollUpAndDownWaypointsList(waypointData.copy(waypoint = location))
        }
    }

    /**
     * MSDKUI-148: Reverse waypoint items order
     */
    @Test
    @FunctionalUITest
    fun testReverseWaypointItemsOrder() {
        var waypointList = arrayListOf<String>()
        // Select first waipoint item & add the selected location to waypoint list
        waypointList.add(RoutePlannerActions.selectWaypoint(waypoint1).waypoint)
        // Select second waipoint item & add the selected location to waypoint list
        waypointList.add(RoutePlannerActions.selectWaypoint(waypoint2).waypoint)
        // Wait for planner collapsed and routes description list
        RoutePlannerBarActions.waitForRoutePlannerCollapsed().waitRouteDescriptionList()
        // Add two additional waypoints to waypoint list, select location and add to waypoint list
        enumValues<WaypointItem>().forEach {
            // Skip two first waypoint items, because can't be add/remove
            when (it) {
                WAYPOINT_3, WAYPOINT_4 -> {
                    // Expand route planner panel to update waypoints
                    RoutePlannerBarActions.waitForRoutePlannerCollapsed()
                            .waitRouteDescriptionEnabled()
                            .waitForRightImageIconExpand()
                            .tapOnRightArrowButton()
                            .waitForRoutePlannerExpanded()
                    // Create waypoint to add on route planner
                    val waypointData = WaypointData(MapData.randMapPoint, it)
                    // Tap on Add/Plus additional waypoint image button
                    RoutePlannerBarActions.waitForAddWaypointImageButton().tapOnAddWaypointButton()
                    // Select a waypoint on map
                    MapActions.waitForMapViewEnabled().tapOnMap(waypointData)
                    // Tap on tick to confirm the first waypoint selection
                    waypointList.add(RoutePlannerBarActions
                            .waitForLocationNotDisplayed()
                            .waitForRightImageIconCheck()
                            .tapOnTickButton())
                } else -> return@forEach
            }
        }
        // Expand route planner panel to update waypoints
        RoutePlannerBarActions.waitForRoutePlannerCollapsed()
                .waitRouteDescriptionEnabled()
                .waitForRightImageIconExpand()
                .tapOnRightArrowButton()
                .waitForRoutePlannerExpanded()
        // Reverse waypoints order in the waypoints list by click on swap button
        RoutePlannerBarActions.waitForSwapWaypointsImageButton()
                .tapOnSwapWaypointButton()
                .waitForRoutePlannerCollapsed()
                .waitRouteDescriptionEnabled()
                .waitForRightImageIconExpand()
                .tapOnRightArrowButton()
                .waitForRoutePlannerExpanded()
        // Check that waypoints order reversed
        onPlannerWaypointList.check(matches(isWaypointsReversed(waypointList.reversed())))
    }

    /**
     * MSDKUI-146: Add/Remove waypoints
     */
    @Test
    @FunctionalUITest
    fun testAddRemoveWaypoints() {
        // Tap on Add/Plus additional waypoint image button
        RoutePlannerBarActions.waitForAddWaypointImageButton()
                .tapOnAddWaypointButton()
                .checkWaypointDefaultLabel()
        // Select the first waypoint on map
        MapActions.waitForMapViewEnabled().tapOnMap(waypoint1)
        // Tap on tick to confirm the first waypoint selection
        RoutePlannerBarActions.waitForLocationNotDisplayed()
                .waitForRightImageIconCheck()
                .tapOnTickButton()
        // Check that third remove waypoint button is visible
        // And click on it to remove and check that the button remove
        RoutePlannerBarActions.waitForRoutePlannerExpanded()
                .tapOnRemoveWaypointButton(REMOVE_WAYPOINT_BTN3)
        // Check that route planner remove button dismissed
        RoutePlannerBarActions.waitForRoutePlannerExpanded()
    }

    /**
     * MSDKUI-317: Clear waypoints (back button)
     */
    @Test
    @FunctionalUITest
    fun testClearWaypointsBackButton() {
        // Map view is visible by default
        onMapFragmentWrapper.check(matches(isDisplayed()))
        // Map view and Route planner are opened and app title correctly displayed
        RoutePlannerBarActions.waitForRoutePlannerExpanded()
        // Check default waypoint items labels text and default transportation mode
        RoutePlannerMatchers.checkWaypointDefaultLabel(WAYPOINT_1)
                .checkWaypointDefaultLabel(WAYPOINT_2)
                .checkTransModeSelected(TYPE_CAR)
        // Select first waipoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waipoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Wait for planner collapsed and routes description list
        RoutePlannerBarActions.waitForRoutePlannerCollapsed().waitRouteDescriptionList()
        // Press device 'Back' button
        CoreActions().pressBackButton()
        // Default landing page view is displayed (the same state as freshly launched app)
        // Map view is visible by default
        onMapFragmentWrapper.check(matches(isDisplayed()))
        // Map view and Route planner are opened and app title correctly displayed
        RoutePlannerBarActions.waitForRoutePlannerExpanded()
        // Check 'Choose waypoint' default label value of the first waypoint item
        RoutePlannerMatchers.checkWaypointDefaultLabel(WAYPOINT_1)
                .checkWaypointDefaultLabel(WAYPOINT_2)
                .checkTransModeSelected(TYPE_CAR)
    }

    /**
     * MSDKUI-138: Update a waypoint and verify route changed
     */
    @Test
    @FunctionalUITest
    fun testUpdateAWaypointAndVerifyRouteChanged() {
        // Check route list information for all transportation types
        enumValues<TransportType>().forEach {
            // Select first waipoint item
            RoutePlannerActions.selectWaypoint(WaypointData())
            // Select second waipoint item
            RoutePlannerActions.selectWaypoint(WaypointData(MAP_POINT_2, WAYPOINT_2))
            // Wait for planner collapsed and routes description list
            RoutePlannerBarActions.waitForRoutePlannerCollapsed().waitRouteDescriptionList()
            // Select next transportation type
            RoutePlannerActions.selectTransportMode(it)
                    .waitForRoutePlannerCollapsed()
                    .waitRouteDescriptionEnabled()
                    .waitForRightImageIconExpand()
                    .tapOnRightArrowButton()
                    .waitForRoutePlannerExpanded()
            // Update and check route 1st planner item & route information list
            updateRouteInformation(WaypointData(MAP_POINT_4), saveRouteInformation(ROUTE_RESULT_1, it))
            // Update and check route 2n planner item & route information list
            updateRouteInformation(WaypointData(MAP_POINT_3, WAYPOINT_2), saveRouteInformation(ROUTE_RESULT_1, it))
            // Press device 'Back' button
            CoreActions().pressBackButton()
        }
    }

    /**
     * Save route information from routing list
     */
    private fun saveRouteInformation(routeItem: Int = ROUTE_RESULT_1, transportType: TransportType = TYPE_CAR): RouteData {
        val duration = getTextView(onRouteDescDuration(routeItem))
        val details = getTextView(onRouteDescDetails(routeItem))
        val arrival = getTextView(onRouteDescArrival(routeItem))
        when (transportType) {
            TYPE_CAR, TYPE_TRUCK -> return RouteData(
                    transportType, duration, details, arrival,
                    CoreMatchers.getTextView(onRouteDescDealyInformation(routeItem)))
            else -> print("there is no convenient transportation type")
        }
        return RouteData(transportType, duration, details, arrival)
    }
}