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
 
 

package com.here.msdkuiapp

import com.here.android.mpa.routing.Route
import com.here.msdkui.guidance.GuidanceManeuverData
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.base.BaseContract

/**
 * GuidanceContracts for presenters to communicate with views.
 */
class GuidanceContracts {

    /**
     *  ManeuverPanel for GuidanceManeuverPanelFragmentPresenter communicate with GuidanceManeuverPanelFragment.
     */
    interface ManeuverPanel : BaseContract<ManeuverPanel> {
        /**
         * To be called when maneuver data is available.
         *
         * @param data GuidanceManeuverData
         */
        fun onManeuverData(data: GuidanceManeuverData)

        /**
         * To be called when destination is reached.
         */
        fun onDestinationReached()
    }

    /**
     * Contracts for guidance route preview.
     */
    interface RoutePreview : BaseContract<RoutePreview> {

        /**
         * To be called when ui needs to be populated with [WaypointEntry] & [Route].
         *
         * @param entry [WaypointEntry]
         * @param route [Route] to be rendered on map.
         */
        fun populateUI(entry: WaypointEntry, route: Route, listVisible: Boolean)

        /**
         * To be called when routing is failed.
         *
         * @param reason reason for failure.
         */
        fun routingFailed(reason: String)

        /**
         * To be called when showing/hiding route maneuvers list
         *
         * @param listVisible true if route maneuvers list is visible, false otherwise.
         */
        fun toggleSteps(listVisible: Boolean)
    }

    /**
     * Contracts for waypoint selection in guidance.
     */
    interface GuidanceWaypointSelection : BaseContract<GuidanceWaypointSelection> {

        /**
         * Indicates to update UI.
         *
         * @param textValue textValue needed to update UI
         * @param withColor true if color of UI needs to be changed.
         */
        fun onUiUpdate(textValue: String, withColor: Boolean = false, rightIconVisible: Boolean = false)
    }
}

/**
 * Contracts for shared components among different modules.
 */
class CommonContracts {

    /**
     * Contract for waypoint selection.
     */
    interface WaypointSelection : BaseContract<WaypointSelection> {

        /**
         * Right icon is clicked.
         *
         * @param index index of [WaypointEntry].
         * @param entry [WaypointEntry].
         */
        fun onRightIconClicked(index: Int?, entry: WaypointEntry?)

        /**
         * Ui update needed.
         *
         * @param value value needed to update UI
         * @param withColor true if color of UI needs to be changed.
         */
        fun onUiUpdate(value: String, withColor: Boolean)
    }
}