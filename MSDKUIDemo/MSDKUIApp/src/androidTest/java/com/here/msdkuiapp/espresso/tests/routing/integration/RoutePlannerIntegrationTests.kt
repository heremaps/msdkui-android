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

package com.here.msdkuiapp.espresso.tests.routing.integration

import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import com.here.msdkuiapp.SplashActivity
import com.here.msdkuiapp.espresso.impl.annotation.IntegrationUITest
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.viewIsDisplayed
import com.here.msdkuiapp.espresso.impl.testdata.Constants
import com.here.msdkuiapp.espresso.impl.testdata.Constants.ROUTE_RESULT_1
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.TransportType
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem.WAYPOINT_1
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem.WAYPOINT_2
import com.here.msdkuiapp.espresso.impl.views.route.matchers.RouteMatchers
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteDescriptionList
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView.onRouteListItemDelayInformation
import com.here.msdkuiapp.espresso.impl.views.route.useractions.RouteActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.matchers.RoutePlannerMatchers
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.useractions.RoutePlannerBarActions
import com.here.msdkuiapp.espresso.impl.views.routeplanner.utils.WaypointData
import com.here.msdkuiapp.espresso.tests.TestBase
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

/**
 * Test for integration of RoutePlanner component with other components.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class RoutePlannerIntegrationTests: TestBase<SplashActivity>(SplashActivity::class.java) {

    private lateinit var waypoint1: WaypointData
    private lateinit var waypoint2: WaypointData

    @Before
    fun prepare() {
        waypoint1 = WaypointData(Constants.GEO_POINT_1, WAYPOINT_1)
        waypoint2 = WaypointData(Constants.GEO_POINT_2, WAYPOINT_2)
        CoreActions().enterRoutePlanner()
    }

    /**
     * Tap on waypoint item of the Route planner
     */
    @Test
    @IntegrationUITest
    fun testTapOnWaypointItem_shouldLoadWaypointSelectionFragment() {
        RoutePlannerActions.tapWaypointOnPlanner(WAYPOINT_1).checkWaypointDefaultLabel()
    }

    /**
     * Tap on Add/Plus button of the Route planner
     */
    @Test
    @IntegrationUITest
    fun testTapOnAdd_shouldLoadWaypointSelection() {
        RoutePlannerBarActions.waitForAddWaypointImageButton()
                .tapOnAddWaypointButton()
                .checkWaypointDefaultLabel()
    }

    /**
     * MSDKUI-566: Integration tests for Route Planner/Travel time panel
     */
    @Test
    @IntegrationUITest
    fun testRoutePlannerTimePanel_shouldOpenTimePicker() {
        RoutePlannerActions.tapOnTravelTimePanel()
        RoutePlannerMatchers.checkDatePickerDisplayed()
        RoutePlannerActions.tapOKButton()
    }

    /**
     * MSDKUI-564: Tap on swap button to reverse waypoints order items list
     */
    @Test
    @IntegrationUITest
    fun testTapOnSwap_shouldSwapItems() {
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waypoint item & add the selected location to waypoint list
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Wait routes description list and check routes results
        RouteActions.waitRouteDescriptionList()
        // Expand route planner panel to update waypoints
        RoutePlannerBarActions.waitForRoutePlannerCollapsed()
                .waitRouteDescriptionList()
                .waitForRightImageIconExpand()
                .tapOnRightArrowButton()
                .waitForRoutePlannerExpanded()
                .waitForSwapWaypointsImageButton()
                .tapOnSwapWaypointButton()
                .waitForRoutePlannerCollapsed()
                .waitRouteDescriptionList()
    }

    /**
     * Tap on Options image button to open route options
     */
    @Test
    @IntegrationUITest
    fun testTapOnSettings_shouldOpenPanel() {
        RoutePlannerActions.openOptionsPanel()
    }

    /**
     * MSDKUI-890 - Integration tests for Route Planner/Options
     */
    @Test
    @IntegrationUITest
    fun testRoutePlannerOptions_shouldDisplayForTransportModes() {
        enumValues<TransportType>().forEach {
            // Select transportation mode
            RoutePlannerActions.selectTransportMode(it)
            // Open Options panel for the selected transport mode and check settings
            RoutePlannerActions.openOptionsPanel()
                    .checkOptionsForTransportType(it)
                    .tapOnBackRoutePlannerButton()
        }
    }

    /**
     * MSDKUI-565: Integration tests for Route Planner/Transport Mode
     */
    @Test
    @IntegrationUITest
    fun testsForRoutePlannerTransportMode_shouldSelectItems() {
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waypoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Wait for panel collapsed and routes description list is visible
        RoutePlannerBarActions.waitForRoutePlannerCollapsed().waitRouteDescriptionList()
        // Check all existing transportation types
        enumValues<TransportType>().forEach {
            // Select next transportation type and check the type is selected
            RoutePlannerActions.selectTransportMode(it)
                    .waitForRoutePlannerCollapsed()
                    .waitRouteDescriptionEnabled()
            // Check first transportation image icon for the selected transportation type
            RouteMatchers.checkTransportIconDisplayed(it)
        }
    }

    /**
     * MSDKUI-567: Integration tests for Route Planner/Route List
     */
    @Test
    @IntegrationUITest
    fun testRoutePlannerRouteList_shouldOpenRouteDescriptionList() {
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waypoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Wait for panel collapsed and routes description list is visible
        RoutePlannerBarActions.waitForRoutePlannerCollapsed().waitRouteDescriptionEnabled()
        // Check that Route Description list exists
        onRouteDescriptionList.check(matches(isDisplayed()))
    }

    /**
     * MSDKUI-568 Integration tests for Route Planner/Route preview
     */
    @Test
    @IntegrationUITest
    fun testRoutePlannerRoutePreview() {
        // Select first waypoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waypoint item
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Wait for panel collapsed and routes description list is visible
        RoutePlannerBarActions.waitForRoutePlannerCollapsed().waitRouteDescriptionEnabled()
        // Tap on the first route item from route list to open route overview
        RouteActions.tapRouteItemOnDescList(ROUTE_RESULT_1)
                .waitRouteOverviewDisplayed()
                .checkRouteOverviewItemsDisplayed(viewIsDisplayed(onRouteListItemDelayInformation))
    }

    /**
     * MSDKUI-1194 Show maneuver list
     */
    @Test
    @IntegrationUITest
    fun showManeuverList() {
        enumValues<Constants.ScreenOrientation>().forEach {
            // Set screen orientation: PORTRAIT / LANDSCAPE
            CoreActions().changeOrientation(it)
            // Select first waypoint item
            RoutePlannerActions.selectWaypoint(waypoint1)
            // Select second waypoint item
            RoutePlannerActions.selectWaypoint(waypoint2)
            // Wait for panel collapsed and routes description list is visible
            RoutePlannerBarActions.waitForRoutePlannerCollapsed()
                    .waitRouteDescriptionEnabled()
            // Tap on the first route item from route list to open route overview
            RouteActions.tapRouteItemOnDescList(ROUTE_RESULT_1)
                    .waitRouteOverviewDisplayed()
            // Open maneuvers list
            RouteActions.tapOnSeeManeuverButton()
            // Go back to Route planner
            CoreActions().pressBackButton().pressBackButton()
        }
    }
}