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

package com.here.msdkuiapp.espresso.tests.routing.flow

import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.here.msdkuiapp.R
import com.here.msdkuiapp.SplashActivity
import com.here.msdkuiapp.espresso.impl.annotation.FunctionalUITest
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getDate
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getText
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.getTime
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withDateText
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.viewIsDisplayed
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.waitForTextChange
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIndex
import com.here.msdkuiapp.espresso.impl.core.CoreView.onRootView
import com.here.msdkuiapp.espresso.impl.testdata.Constants.GEO_POINT_0
import com.here.msdkuiapp.espresso.impl.testdata.Constants.GEO_POINT_1
import com.here.msdkuiapp.espresso.impl.testdata.Constants.GEO_POINT_2
import com.here.msdkuiapp.espresso.impl.testdata.Constants.GEO_POINT_3
import com.here.msdkuiapp.espresso.impl.testdata.Constants.GEO_POINT_4
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ROUTE_RESULT_1
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.RemoveWaypointBtn.REMOVE_WAYPOINT_BTN3
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType.*
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem.*
import com.here.msdkuiapp.espresso.impl.views.map.useractions.MapActions
import com.here.msdkuiapp.espresso.impl.views.route.matchers.RouteMatchers
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteListItemArrival
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteListItemDelayInformation
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteListItemDetails
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteListItemDuration
import com.here.msdkuiapp.espresso.impl.views.route.useractions.RouteActions
import com.here.msdkuiapp.espresso.impl.views.route.useractions.RouteBarActions
import com.here.msdkuiapp.espresso.impl.views.route.utils.RouteData
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerMatchers.isWaypointsReversed
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerBarView
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onDatePicker
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointList
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointLocationLabel
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onRoutePlannerInstructions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onTimePicker
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onTravelDepartureDateTime
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerActions.updateRouteInformation
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerBarActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerOptionsActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.utils.WaypointData
import com.here.msdkuiapp.espresso.tests.TestBase
import com.here.msdkuiapp.routing.RoutingIdlingResourceWrapper
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import java.util.Collections
import java.util.Calendar.HOUR_OF_DAY
import java.util.Calendar.MINUTE
import kotlin.math.abs

/**
 * UI flow tests for routing
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RoutePlannerFlowTests : TestBase<SplashActivity>(SplashActivity::class.java) {

    private lateinit var waypoint1: WaypointData
    private lateinit var waypoint2: WaypointData
    private lateinit var waypoint3: WaypointData
    private lateinit var waypoint4: WaypointData

    @Before
    fun prepare() {
        waypoint1 = WaypointData(GEO_POINT_1, WAYPOINT_1)
        waypoint2 = WaypointData(GEO_POINT_2, WAYPOINT_2)
        waypoint3 = WaypointData(GEO_POINT_3, WAYPOINT_3)
        waypoint4 = WaypointData(GEO_POINT_4, WAYPOINT_4)
        CoreActions().enterRoutePlanner()
        // Register idling resource for route calculation
        RoutingIdlingResourceWrapper.register()
    }

    @After
    fun tearDown() {
        // Deregister idling resource for route calculation
        RoutingIdlingResourceWrapper.close()
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
        // Check that route planner does not collapse
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
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Check that route planner expanded
        RoutePlannerBarActions.waitForRoutePlannerExpanded()
        // Select second waypoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Check that transportation type: 'Car' is selected by default
        RoutePlannerMatchers.checkTransModeSelected(TYPE_CAR)
    }

    /**
     * MSDKUI-137: Select different transportation modes
     */
    @Test
    @FunctionalUITest
    fun testSelectDifferentTransportationModes() {
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waypoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
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
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waypoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Check route list information for all transportation types
        enumValues<TransportType>().forEach {
            // Select next transportation type
            RoutePlannerActions.selectTransportMode(it)
            RouteActions.tapRouteItemOnDescList(ROUTE_RESULT_1)
                    .waitRouteOverviewDisplayed()
                    .checkRouteOverviewItemsDisplayed(viewIsDisplayed(onRouteListItemDelayInformation))
            RouteActions.tapOnSeeManeuverButton()
            RouteMatchers.checkManeuverResultList()
                    .tapOnBackArrowButton()
        }
    }

    /**
     * MSDKUI-150: Route description
     */
    @Test
    @FunctionalUITest
    fun testRouteDescription() {
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waypoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Check route list information for all transportation types
        enumValues<TransportType>().forEach {
            // Select next transportation type
            RoutePlannerActions.selectTransportMode(it)
            // Wait routes description list and check routes results
            RouteActions.checkRouteResultsList(it).swipeDownRouteList()
            // Save information of the first route item list
            val routeData = getRouteInformation(ROUTE_RESULT_1, it)
            // Tap on the first route item from route list to open route overview
            RouteActions.tapRouteItemOnDescList(ROUTE_RESULT_1)
                    .waitRouteOverviewDisplayed()
                    .checkRouteOverviewItemsDisplayed(viewIsDisplayed(onRouteListItemDelayInformation))
//                   .withRouteOverviewData(routeData) // FIXME: MSDKUI-919
            RouteBarActions.tapOnBackArrowButton()
        }
    }

    /**
     * MSDKUI-140: Scroll route list state (multiple routes count)
     */
    @Test
    @FunctionalUITest
    fun testScrollRouteListStateMultipleRoutesCount() {
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waypoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Check route list information for all transportation types
        enumValues<TransportType>().forEach {
            // Select next transportation type
            RoutePlannerActions.selectTransportMode(it)
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
            when (it) {
                // Skip two first waypoint items, because can't be add/remove
                WAYPOINT_1, WAYPOINT_2 -> return@forEach
                else -> {
                    // Tap on Add/Plus additional waypoint image button
                    RoutePlannerBarActions.waitForAddWaypointImageButton().tapOnAddWaypointButton()
                    // Select a waypoint on map
                    val waypointData = WaypointData(it.mapData, it)
                    MapActions.waitForMapViewEnabled().tap(waypointData)
                    // Tap on tick to confirm the first waypoint selection
                    val location = RoutePlannerBarActions.waitForLocationNotDisplayed()
                            .waitForRightImageIconCheck()
                            .tapOnTickButton()
                    // Scroll Up & Down waypoints list in case of new waypoint item is not visible
                    RoutePlannerActions.scrollUpAndDownWaypointsList(waypointData.copy(waypoint = location))
                }
            }
        }
    }

    /**
     * MSDKUI-148: Reverse waypoint items order
     */
    @Test
    @FunctionalUITest
    fun testReverseWaypointItemsOrder() {
        val waypointList = arrayListOf<String>()
        // Select first waypoint item & add the selected location to waypoint list
        waypointList.add(RoutePlannerActions.selectWaypoint(waypoint1).waypoint)
        // Select second waypoint item & add the selected location to waypoint list
        waypointList.add(RoutePlannerActions.selectWaypoint(waypoint2).waypoint)
        // Add two additional waypoints to waypoint list, select location and add to waypoint list
        enumValues<WaypointItem>().forEach {
            // Skip two first waypoint items, because can't be add/remove
            when (it) {
                WAYPOINT_3, WAYPOINT_4 -> {
                    // Expand route planner panel to update waypoints
                    RoutePlannerBarActions.waitForRightImageIconExpand()
                            .tapOnRightArrowButton()
                            .waitForRoutePlannerExpanded()
                    // Create waypoint to add on route planner
                    val waypointData = if (it == WAYPOINT_3) waypoint3 else waypoint4
                    // Tap on Add/Plus additional waypoint image button
                    RoutePlannerBarActions.waitForAddWaypointImageButton().tapOnAddWaypointButton()
                    // Select a waypoint on map
                    MapActions.waitForMapViewEnabled().tap(waypointData)
                    // Tap on tick to confirm the first waypoint selection
                    waypointList.add(RoutePlannerBarActions
                            .waitForLocationNotDisplayed()
                            .waitForRightImageIconCheck()
                            .tapOnTickButton())
                }
                else -> return@forEach
            }
        }
        // Expand route planner panel to update waypoints
        RoutePlannerBarActions.waitForRightImageIconExpand()
                .tapOnRightArrowButton()
                .waitForRoutePlannerExpanded()
        // Reverse waypoints order in the waypoints list by click on swap button
        RoutePlannerBarActions.waitForSwapWaypointsImageButton()
                .tapOnSwapWaypointButton()
        RoutePlannerBarActions.waitForRightImageIconExpand()
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
        MapActions.waitForMapViewEnabled().tap(waypoint1)
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
        // Route planner instructions are visible by default
        onRoutePlannerInstructions.check(matches(isDisplayed()))
        // Map view and Route planner are opened and app title correctly displayed
        RoutePlannerBarActions.waitForRoutePlannerExpanded()
        // Check default waypoint items labels text and default transportation mode
        RoutePlannerMatchers.checkWaypointDefaultLabel(WAYPOINT_1)
                .checkWaypointDefaultLabel(WAYPOINT_2)
                .checkTransModeSelected(TYPE_CAR)
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waypoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Press device 'Back' button
        CoreActions().pressBackButton()
        // Default landing page view is displayed (the same state as freshly launched app)
        // Route planner instructions are visible by default
        onRoutePlannerInstructions.check(matches(isDisplayed()))
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
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Check route list information for all transportation types
        enumValues<TransportType>().forEach {
            // Select second waypoint item
            RoutePlannerActions.selectWaypoint(waypoint2)
            // Select next transportation type
            RoutePlannerActions.selectTransportMode(it)
            RoutePlannerBarActions.waitForRightImageIconExpand()
                    .tapOnRightArrowButton()
                    .waitForRoutePlannerExpanded()
            // Update and check route 1st planner item & route information list
            updateRouteInformation(WaypointData(GEO_POINT_0, WAYPOINT_2), getRouteInformation(ROUTE_RESULT_1, it))
        }
    }

    /**
     * MSDKUI-149: Set departure time
     */
    @Test
    @FunctionalUITest
    fun testSetDepartureTime() {
            // Select first waypoint item
            RoutePlannerActions.selectWaypoint(waypoint1)
            // Select second waypoint item
            RoutePlannerActions.selectWaypoint(waypoint2)
            // Save arrival time
            val arrivalTime = getRouteInformation().arrival
            // Open date picker
            RoutePlannerActions.tapOnTravelTimePanel()
            // Check DatePicker & buttons displayed
            RoutePlannerMatchers.checkDatePickerDisplayed().checkOkButtonDisplayed().checkCancelButtonDisplayed()
            // Change date
            RoutePlannerActions.setTravelDateTomorrow()
            // Save date
            val date = getDate(onDatePicker)
            // Tap 'OK'
            RoutePlannerActions.tapOKButton()
            // Check TimePicker & buttons displayed
            RoutePlannerMatchers.checkTimePickerDisplayed().checkOkButtonDisplayed().checkCancelButtonDisplayed()
            // Change time
            RoutePlannerActions.setTravelTime1HourLater()
            // Save time
            val time = getTime(onTimePicker)
            // Copy time to date
            date.set(HOUR_OF_DAY, time.get(HOUR_OF_DAY))
            date.set(MINUTE, time.get(MINUTE))
            // Tap 'OK'
            RoutePlannerActions.tapOKButton()
            // Check departure time
            onTravelDepartureDateTime.check(matches(withDateText(date.time)))
            // Check arrival time
            onRouteListItemArrival(ROUTE_RESULT_1).check(matches(not(withText(arrivalTime))))
    }

    /**
     * MSDKUI-153 Browse maneuver list
     */
    @Test
    @FunctionalUITest
    fun testBrowseManeuverList() {
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waypoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Tap on the first route item from route list to open route overview
        RouteActions.tapRouteItemOnDescList(ROUTE_RESULT_1)
                .waitRouteOverviewDisplayed()
        // Open maneuvers list and scroll it
        RouteActions.tapOnSeeManeuverButton()
                .scrollUpAndDownRouteManeuversList()
    }

    /**
     * MSDKUI-147 Reorder waypoints
     */
    @Test
    @FunctionalUITest
    fun testReorderWaypoints() {
        val waypointsList = arrayListOf<String>()
        // Select first waypoint item
        waypointsList.add(RoutePlannerActions.selectWaypoint(waypoint1).waypoint)
        // Select second waypoint item
        waypointsList.add(RoutePlannerActions.selectWaypoint(waypoint2).waypoint)
        // Expand panel
        RoutePlannerBarActions.waitForRightImageIconExpand()
                .tapOnRightArrowButton()
        // Tap on Add waypoint button
        RoutePlannerBarActions.waitForAddWaypointImageButton()
                .tapOnAddWaypointButton()
        // Select a waypoint on map
        MapActions.waitForMapViewEnabled().tap(waypoint3)
        // Confirm waypoint selection
        waypointsList.add(RoutePlannerBarActions
                .waitForLocationNotDisplayed()
                .waitForRightImageIconCheck()
                .tapOnTickButton())
        // Expand panel
        RoutePlannerBarActions.waitForRightImageIconExpand()
                .tapOnRightArrowButton()
        // By this point we have filled UI with 3 waypoints
        val waypoints = arrayOf(WAYPOINT_1, WAYPOINT_2, WAYPOINT_3)
        var draggedItemIndex: Int
        var toIndex: Int
        // Iterate waypoints collections to define waypoint pairs with different members to test all reorder options
        for (wp1 in waypoints) {
            for (wp2 in waypoints) {
                if (wp1 != wp2) {
                    // Drag waypoint
                    RoutePlannerActions.dragWaypoint(wp1, wp2)
                    // Reorder list in the same manner, as waypoints were reordered
                    draggedItemIndex = wp1.index
                    toIndex = wp2.index
                    if (abs(draggedItemIndex - toIndex) == 1) {
                        // In case of adjacent waypoints, just swap them
                        Collections.swap(waypointsList, draggedItemIndex, toIndex)
                    } else {
                        // In case of detached waypoints, first swap dragged item and "to" item
                        Collections.swap(waypointsList, draggedItemIndex, toIndex)
                        // Then correct "to" item position by swapping it with remaining item
                        Collections.swap(waypointsList, draggedItemIndex, 3 - draggedItemIndex - toIndex)
                    }
                    // Expand panel
                    RoutePlannerBarActions.waitForRightImageIconExpand()
                            .tapOnRightArrowButton()
                    // Check
                    for (wp in waypoints) {
                        onPlannerWaypointLocationLabel(wp).check(matches(withText(waypointsList[wp.index])))
                    }
                }
            }
        }
    }

    /**
     * MSDKUI-1646: Switch tunnel route options
     */
    @Test
    @FunctionalUITest
    fun testRouteOptionsRemoveTunnel() {
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(WaypointData(GEO_POINT_3, WAYPOINT_1))
        // Select second waypoint item
        RoutePlannerActions.selectWaypoint(WaypointData(GEO_POINT_0, WAYPOINT_2))
        // Save initial route details
        val routeDetails = getRouteInformation(ROUTE_RESULT_1).details
        // Switch off tunnel from route options
        RoutePlannerOptionsActions.tapSwitchTunnelOption()
        // Go back to route planner route view
        RoutePlannerBarView.onPlannerBackImageButton.check(matches(isDisplayed()))
                .perform(ViewActions.click())
                .perform(ViewActions.click())
        // Check that new route details differ from initial
        onRootView.perform(waitForTextChange(withIndex(withId(R.id.desc_details), ROUTE_RESULT_1),
                routeDetails as String))
    }

    /**
     * Get route information from routing list
     */
    private fun getRouteInformation(routeItem: Int = ROUTE_RESULT_1, transportType: TransportType = TYPE_CAR): RouteData {
        val duration = getText(onRouteListItemDuration(routeItem))
        val details = getText(onRouteListItemDetails(routeItem))
        val arrival = getText(onRouteListItemArrival(routeItem))
        return when (transportType) {
            TYPE_CAR, TYPE_TRUCK, TYPE_SCOOTER -> RouteData(
                    transportType, duration, details, arrival,
                    CoreMatchers.getText(onRouteListItemDelayInformation(routeItem)))
            else -> RouteData(transportType, duration, details, arrival)
        }
    }
}