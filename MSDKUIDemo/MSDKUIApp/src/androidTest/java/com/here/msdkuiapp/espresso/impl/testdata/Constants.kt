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

import android.content.pm.ActivityInfo
import android.support.test.espresso.action.Tap
import com.here.msdkuiapp.espresso.impl.views.map.utils.MapData

/**
 * General framework test data constants
 */
object Constants {

    /**
     * Landing screen heading items
     */
    const val ROUTE_PLANNER_ITEM = 0
    const val DRIVE_NAVIGATION_ITEM = 1

    /**
     * Route result list items
     */
    const val ROUTE_RESULT_1 = 0

    /**
     * Transport type Lorry
     */
    const val TYPE_LORRY = "Transport type: Lorry"

    /**
     * Options settings
     */
    const val CAR_AVOID_TRAFFIC = 1

    /**
     * Pattern for From: & To: waypoint on RoutePlanner
     */
    const val FROM_TO_PATTERN = "%1\$s"

    /**
     * Map related coordinates
     */
    private const val MAP_COORDINATE_A = 0.1
    private const val MAP_COORDINATE_B = 0.5
    private const val MAP_COORDINATE_C = 0.9
    private const val MAP_COORDINATE_D = 0.3
    private const val MAP_COORDINATE_E = 0.2

    /**
     * Map related points
     */
    val MAP_POINT_1 = MapData(MAP_COORDINATE_A, MAP_COORDINATE_E)
    val MAP_POINT_2 = MapData(MAP_COORDINATE_B, MAP_COORDINATE_A)
    val MAP_POINT_3 = MapData(MAP_COORDINATE_A, MAP_COORDINATE_C)
    val MAP_POINT_4 = MapData(MAP_COORDINATE_C, MAP_COORDINATE_C)
    val MAP_POINT_5 = MapData(MAP_COORDINATE_A, MAP_COORDINATE_D)
    val MAP_POINT_6 = MapData(MAP_COORDINATE_B, MAP_COORDINATE_B)
    val MAP_POINT_7 = MapData(MAP_COORDINATE_D, MAP_COORDINATE_D)

    /**
     * Activity screen value types
     */
    enum class ScreenOrientation(val value: Int) {
        PORTRAIT(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT),
        LANDSCAPE(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }

    /**
     * Touch screen tap action types
     */
    enum class Gestures(val value: Tap) {
        SINGLE_TAP(Tap.SINGLE),
        LONG_PRESS(Tap.LONG)
    }
}