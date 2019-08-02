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

package com.here.msdkuiapp.common

import androidx.fragment.app.Fragment
import com.here.msdkuiapp.common.mapselection.WaypointSelectionFragment
import com.here.msdkuiapp.common.routepreview.RoutePreviewFragment
import com.here.msdkuiapp.guidance.*
import com.here.msdkuiapp.position.LocationPermissionFragment
import com.here.msdkuiapp.routing.*

/**
 * Factory class to create fragments.
 */
object FragmentFactory {

    /**
     * Creates fragments for given fragment class.
     *
     * @param fragmentClass fragment class to be created.
     */
    fun <T : Fragment> create(fragmentClass: Class<T>): Fragment =
            with(fragmentClass) {
                when {
                // routing
                    isAssignableFrom(RoutePlannerFragment::class.java) -> {
                        RoutePlannerFragment.newInstance()
                    }
                    isAssignableFrom(WaypointSelectionFragment::class.java) -> {
                        WaypointSelectionFragment.newInstance()
                    }
                    isAssignableFrom(RouteDescriptionListFragment::class.java) -> {
                        RouteDescriptionListFragment.newInstance()
                    }
                    isAssignableFrom(OptionPanelFragment::class.java) -> {
                        OptionPanelFragment.newInstance()
                    }
                    isAssignableFrom(SubOptionPanelFragment::class.java) -> {
                        SubOptionPanelFragment.newInstance()
                    }
                    isAssignableFrom(RoutingInstructionsFragment::class.java) -> {
                        RoutingInstructionsFragment.newInstance()
                    }

                    isAssignableFrom(RouteManeuverListFragment::class.java) -> {
                        RouteManeuverListFragment.newInstance()
                    }

                    isAssignableFrom(RoutePreviewFragment::class.java) -> {
                        RoutePreviewFragment.newInstance()
                    }

                // guidance
                    isAssignableFrom(GuidanceManeuverFragment::class.java) -> {
                        GuidanceManeuverFragment.newInstance()
                    }
                    isAssignableFrom(GuidanceWaypointSelectionFragment::class.java) -> {
                        GuidanceWaypointSelectionFragment.newInstance()
                    }
                    isAssignableFrom(LocationPermissionFragment::class.java) -> {
                        LocationPermissionFragment.newInstance()
                    }
                    isAssignableFrom(GuidanceStreetLabelFragment::class.java) -> {
                        GuidanceStreetLabelFragment.newInstance()
                    }
                    isAssignableFrom(GuidanceNextManeuverFragment::class.java) -> {
                        GuidanceNextManeuverFragment.newInstance()
                    }
                    isAssignableFrom(GuidanceEstimatedArrivalFragment::class.java) -> {
                        GuidanceEstimatedArrivalFragment.newInstance()
                    }
                    isAssignableFrom(GuidanceSpeedFragment::class.java) -> {
                        GuidanceSpeedFragment.newInstance()
                    }
                    isAssignableFrom(GuidanceSpeedLimitFragment::class.java) -> {
                        GuidanceSpeedLimitFragment.newInstance()
                    }
                    else ->
                        throw IllegalArgumentException("Please extend fragment factory create with class: ${fragmentClass.name}")
                }
            }
}