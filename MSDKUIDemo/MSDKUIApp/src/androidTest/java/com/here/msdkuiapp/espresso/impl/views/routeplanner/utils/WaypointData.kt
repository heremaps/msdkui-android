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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.utils

import com.here.msdkuiapp.espresso.impl.testdata.Constants.GEO_POINT_0
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem
import com.here.msdkuiapp.espresso.impl.testdata.RoutingTestData.WaypointItem.WAYPOINT_1
import com.here.msdkuiapp.espresso.impl.views.map.utils.MapData
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerFromText
import com.here.msdkuiapp.espresso.impl.views.routeplanner.screens.RoutePlannerView.onPlannerWaypointLocationText

data class WaypointData(
        var point: MapData = GEO_POINT_0,
        var waypointItem: WaypointItem = WAYPOINT_1,
        var waypoint: String = String.format("%s %s", onPlannerFromText, onPlannerWaypointLocationText))