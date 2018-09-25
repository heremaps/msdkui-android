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

package com.here.msdkuiapp.espresso.impl.testdata

import com.here.msdkui.R

/**
 * Routing test data related values
 */
class RoutingTestData {

    /**
     * Waypoint items on route planer
     * Note: Waypoints list can not display more then 4 items, max item = 3
     */
    enum class WaypointItem(val value: Int) {
        // Waypoint items on Route Planner
        WAYPOINT_1(0),
        WAYPOINT_2(1),
        WAYPOINT_3(2),
        WAYPOINT_4(3),
        WAYPOINT_5(3),
        WAYPOINT_6(3)
    }

    /**
     * Transportation types
     */
    enum class TransportType(val value: String, val imageIcon: Int) {
        // Transportation type content-desc
        TYPE_CAR("Transport type: Car", R.drawable.ic_drive),
        TYPE_TRUCK("Transport type: Truck", R.drawable.ic_truck_24),
        TYPE_WALK("Transport type: Walk", R.drawable.ic_walk_24),
        TYPE_BICYCLE("Transport type: Bicycle", R.drawable.ic_bike_24),
        TYPE_SCOOTER("Transport type: Scooter", R.drawable.ic_scooter)
    }

    /**
     * Remove waypoint buttons
     */
    enum class RemoveWaypointBtn(val value: String) {
        // Remove waypoint button content-desc
        REMOVE_WAYPOINT_BTN1("Remove waypoint button at index 1"),
        REMOVE_WAYPOINT_BTN2("Remove waypoint button at index 2"),
        REMOVE_WAYPOINT_BTN3("Remove waypoint button at index 3")
    }

    /**
     * Route types on Options panel
     */
    enum class RouteType(val value: Int, val item: Int) {
        TYPE_FASTEST(R.string.msdkui_fastest, 0),
        TYPE_SHORTEST(R.string.msdkui_shortest, 1),
        TYPE_BALANCED(R.string.msdkui_balanced, 2)
    }

    /**
     * Avoid traffic types on Options panel
     */
    enum class AvoidTrafficType(val value: Int, val item: Int) {
        TYPE_OFF(R.string.msdkui_disabled, 0),
        TYPE_BEST(R.string.msdkui_optimal, 1),
        TYPE_AVOID(R.string.msdkui_avoid_long_term_closures, 2)
    }

    /**
     * Tunnels allowed types on Options panel
     */
    enum class TunnelsAllowedType(val value: Int, val item: Int) {
        CATEGORY_B(R.string.msdkui_tunnel_cat_b, 0),
        CATEGORY_C(R.string.msdkui_tunnel_cat_c, 1),
        CATEGORY_D(R.string.msdkui_tunnel_cat_d, 2),
        CATEGORY_E(R.string.msdkui_tunnel_cat_e, 3),
        NOT_DEFINED(R.string.msdkui_undefined, 4)
    }

    /**
     * Test places for mocking location service
     */
    enum class TestPlace(val latitude: Double, val longitude: Double, val altitude: Float, val distanceAccuracy: Float) {
        TEST_PLACE_GERMANY_BERLIN(52.469717, 13.385392, 0f, 10f)
    }
}