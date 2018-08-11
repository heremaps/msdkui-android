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
    enum class TransporType(val value: String) {
        // Transportation type content-desc
        TYPE_CAR("Transport type: Car"),
        TYPE_TRUCK("Transport type: Truck"),
        TYPE_WALK("Transport type: Walk"),
        TYPE_BICYCLE("Transport type: Bicycle")
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
}