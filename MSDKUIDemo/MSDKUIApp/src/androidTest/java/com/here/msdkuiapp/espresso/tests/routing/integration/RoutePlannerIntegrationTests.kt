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

import com.here.msdkuiapp.SplashActivity
import com.here.msdkuiapp.espresso.impl.annotation.IntegrationUITest
import com.here.msdkuiapp.espresso.impl.core.CoreActions
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem.WAYPOINT_1
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem.WAYPOINT_2
import com.here.msdkuiapp.espresso.impl.views.map.utils.MapData
import com.here.msdkuiapp.espresso.impl.views.route.useractions.RouteActions
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
        waypoint1 = WaypointData(MapData.randMapPoint)
        waypoint2 = WaypointData(MapData.randMapPoint, WAYPOINT_2)
        CoreActions.enterRoutePlanner()
    }

    /**
     * Tap on waypoint item of the Route planner
     */
    @Test
    @IntegrationUITest
    fun testTapOnWaypointItem_shouldLoadWaypointSelectionFragment() {
        RoutePlannerActions.tapWaipointOnPlanner(WAYPOINT_1).checkWaypointDefaultLabel()
    }

    /**
     * Tap on Add/Plus button of the Route planner
     */
    @Test
    @IntegrationUITest
    fun testTapOnAdd_shouldLoadWaypointSelection() {
        RoutePlannerBarActions.tapOnAddWaypointButton().checkWaypointDefaultLabel()
    }

    /**
     * Tap on Travel time panel to open Date picker
     */
    @Test
    @IntegrationUITest
    fun testTapOnTravelTime_shouldOpenPicker() {
        RoutePlannerActions.tapOnTravelTimePanel().checkDatePickerDisplayed().tapOnDatePickerOKButton()
    }

    /**
     * MSDKUI-564: Tap on swap button to reverse waypoints order items list
     */
    @Test
    @IntegrationUITest
    fun testTapOnSwap_shouldSwapItems() {
        // Select first waipoint item
        RoutePlannerActions.selectWaypoint(waypoint1)
        // Select second waipoint item & add the selected location to waypoint list
        RoutePlannerActions.selectWaypoint(waypoint2)
        // Wait routes description list and check routes results
        RouteActions.waitRouteDescriptionList()
        // Expand route planner panel to update waypoints
        RoutePlannerBarActions.waitForRoutePlannerCollapsed()
                .waitRouteDescriptionList()
                .tapOnRighArrowButton()
                .waitForRoutePlannerExpanded()
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
        RoutePlannerActions.tapOnOptionImage().checkOptionsDisplayed()
    }
}