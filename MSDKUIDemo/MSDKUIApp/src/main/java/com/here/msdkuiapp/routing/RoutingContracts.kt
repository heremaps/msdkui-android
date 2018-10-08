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

package com.here.msdkuiapp.routing

import android.util.SparseArray
import android.view.View
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.base.BaseContract
import java.util.*

/**
 * Contracts to communicate wth respective presenters for routing modules.
 */
class RoutingContracts {

    /**
     * Interface to communicate with [RoutePlannerFragment] view.
     */
    interface RoutePlanner : BaseContract<RoutePlanner> {

        /**
         * Notifies routing is failed.
         *
         * @param reason reason for routing failed.
         */
        fun onRoutingFailed(reason: String)

        /**
         * Notifies waypoint list data is ready.
         *
         * @param entriesMap entries to be set in waypoint list.
         */
        fun onWaypointListDataReady(entriesMap: SparseArray<WaypointEntry>)

        /**
         * Notifies swap is ready.
         *
         * @param enable true if swap button needs to be enable, false otherwise.
         */
        fun onSwapReady(enable: Boolean)

        /**
         * Notifies TravelTime data is ready.
         *
         * @param time time to be set in panel.
         * @param timeType to be set in panel.
         */
        fun onTravelTimeDataReady(time: Date?, timeType: RouteOptions.TimeType?)

        /**
         * Update list for its visibility.
         *
         * @param listVisible true if list is visible, false otherwise.
         */
        fun updateList(listVisible: Boolean)
    }

    /**
     * Interface to communicate with [RouteDescriptionListFragment] view.
     */
    interface RouteDescriptionList : BaseContract<RouteDescriptionList> {

        /**
         * Notifies the data for RouteDescriptionListFragment.
         *
         * @param isTraffic true if traffic needs to be enabled, false otherwise.
         * @param route list of route for ui population.
         */
        fun onUiDataReady(isTraffic: Boolean, route: List<Route>)
    }

    /**
     * Interface to communicate with [ManeuverRouteDescriptionFragment] view.
     */
    interface ManeuverRouteDescription : BaseContract<ManeuverRouteDescription> {

        /**
         * Notifies the data for ManeuverRouteDescriptionFragment .
         *
         * @param item [RouteDescriptionItem] to populate the UI.
         */
        fun onUiDataReady(item: RouteDescriptionItem)
    }

    /**
     * Interface to communicate with [ManeuverListFragment] view.
     */
    interface ManeuverList : BaseContract<ManeuverList> {

        /**
         * Notifies the data for UI for ManeuverListFragment.
         *
         * @param isTraffic true if traffic needs to be enabled, false otherwise.
         * @param route list of route for ui population.
         */
        fun onUiDataReady(isTraffic: Boolean, route: Route)

        /**
         * Updates changes for config.
         *
         * @param visible true if list should be visible, false otherwise.
         */
        fun updateConfigChanges(visible: Boolean)
    }

    /**
     * Interface to communicate with [OptionPanelFragment] view.
     */
    interface OptionPanel : BaseContract<OptionPanel> {

        /**
         *  Notifies the data for UI is ready.
         *
         * @param views collection of views that need to be added.
         */
        fun onUiDataReady(views: Collection<View>)

        /**
         * Gets view for row.
         * @param id Id of view.
         * @return view.
         */
        fun getRowView(id: Int): View

        /**
         * Opens sub option panel.
         *
         * @param panelType type of [Panels].
         * @param routeOptions [RouteOptions] to populate the panel.
         */
        fun onSubPanelClicked(panelType: Panels, routeOptions: RouteOptions?)

        /**
         * Notifies traffic changes from option panel settings.
         *
         * @param trafficPenaltyMode current traffic [Route.TrafficPenaltyMode].
         */
        fun trafficChanged(trafficPenaltyMode: Route.TrafficPenaltyMode?)
    }

    /**
     * Interface to communicate with [SubOptionPanelFragment] view.
     */
    interface SubOptionPanel : BaseContract<SubOptionPanel> {

        /**
         * Notifies the data for UI is ready.
         *
         * @param view View to be populated.
         */
        fun onUiDataReady(view: View)
    }
}

