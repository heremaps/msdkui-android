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

import android.app.Fragment
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.ManeuverDescriptionItem
import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkuiapp.R
import com.here.msdkuiapp.appActionBar
import com.here.msdkuiapp.coordinator
import kotlinx.android.synthetic.main.maneuver.*

/**
 * Fragment to display Maneuver list.
 */
class ManeuverListFragment() : Fragment(), RoutingContracts.ManeuverList {

    private val presenter = ManeuverListPresenter()

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = ManeuverListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.maneuver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.run {
            onAttach(activity, this@ManeuverListFragment)
            updateActionBar(activity?.appActionBar)
            coordinatorListener = coordinator as? RoutingCoordinator
            makeUiDataReady()
            populateConfigChanges(activity.resources.configuration.orientation)
        }
    }

    /**
     * Updates route to populate [ManeuverListFragment].
     *
     * @param route given [Route]
     * @param withTraffic true if list should consider traffic data, false otherwise.
     */
    fun updateRoute(route: Route, withTraffic: Boolean) {
        presenter.updateRoute(route, withTraffic)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        newConfig?.run {
            presenter.populateConfigChanges(orientation)
        }
    }

    override fun updateConfigChanges(visible: Boolean) {
        route_item_container.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun onUiDataReady(isTraffic: Boolean, route: Route) {
        with(route_item) {
            isTrafficEnabled = isTraffic
            setSectionVisible(RouteDescriptionItem.Section.SECTION_BAR, false)
            this.route = route
            setOnClickListener({
                presenter.routeItemClicked(activity.resources.configuration.orientation)
            })
        }
        with(maneuver_route_list) {
            this.route = route
            setOnItemClickedListener(presenter.itemClickedListener)
        }
    }

    /**
     * Listener to handle events for [ManeuverListFragment].
     */
    interface Listener {

        /***
         * Callback to be called when maneuver is clicked.
         *
         * @param index index of clicked item.
         * @param maneuverDescriptionItem clicked item.
         */
        fun onManeuverClicked(index: Int, maneuverDescriptionItem: ManeuverDescriptionItem)

        /**
         * Zooms the map to show the route.
         *
         * @param withCustomBox if true, this will calculate a double height bounding box to zoom.
         */
        fun zoomToRoute(withCustomBox: Boolean = false)
    }
}
