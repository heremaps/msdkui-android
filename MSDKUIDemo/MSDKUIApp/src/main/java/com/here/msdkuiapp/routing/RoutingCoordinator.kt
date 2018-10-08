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
import android.app.FragmentManager
import android.content.Context
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkui.routing.ManeuverDescriptionItem
import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseFragmentCoordinator
import com.here.msdkuiapp.common.mapselection.WaypointSelectionFragment
import com.here.msdkuiapp.map.MapFragmentWrapper

/**
 * Coordinator for managing (add, remove, show, hide etc) different views/panel for routing activity.
 */
class RoutingCoordinator(private val context: Context, fragmentManager: FragmentManager) :
        BaseFragmentCoordinator(fragmentManager),
        RoutePlannerFragment.Listener, MapFragmentWrapper.Listener, WaypointSelectionFragment.Listener, RouteDescriptionListFragment.Listener, ManeuverListFragment.Listener, OptionPanelFragment.Listener {

    private val mapFragment: MapFragmentWrapper?
        get() = fragmentManager.findFragmentById(R.id.mapfragment_wrapper) as? MapFragmentWrapper

    private var topFragment: Fragment? = null
        get() = getFragment(R.id.route_top_container)

    private val plannerFragment: RoutePlannerFragment?
        get() = topFragment as? RoutePlannerFragment

    /**
     * Starts the map engine.
     */
    fun start() {
        topFragment ?: addFragment(R.id.route_top_container, RoutePlannerFragment::class.java)
        mapFragment?.start(this::onEngineInit)
    }

    private fun onEngineInit() {
        if (topFragment is RoutePlannerFragment) {
            // enable traffic by default
            mapFragment?.traffic = plannerFragment!!.trafficMode == Route.TrafficPenaltyMode.OPTIMAL
            mapFragment?.onTouch(false)
        }
    }

    override fun onEntryClicked(index: Int, current: WaypointEntry) {
        setVisible(R.id.route_bottom_container, false)
        setVisible(R.id.mapfragment_wrapper, true)
        mapFragment?.clearMap()
        val selectionFragments = addFragment(R.id.route_top_container, WaypointSelectionFragment::class.java, true)
        selectionFragments.updatePosition(index, current)
        // enable map touch event
        mapFragment?.onTouch(true, this)
    }

    override fun onWaypointSelected(index: Int?, current: WaypointEntry) {
        if (index != null) {
            onBackPressed()
            mapFragment?.onTouch(false)
            val fragment = plannerFragment
            setVisible(R.id.route_top_container, true)
            fragment?.updateWaypoint(index, current)
            fragment?.calculateRoute()
        }
    }

    override fun onRouteCalculated(routes: List<Route>) {
        removeFragment(R.id.route_mid_container)
        setVisible(R.id.mapfragment_wrapper, false)
        val fr = addFragment(R.id.route_bottom_container, RouteDescriptionListFragment::class.java, true)
        setVisible(R.id.route_bottom_container, true)
        val penalty = plannerFragment?.trafficMode
        fr.traffic = penalty == Route.TrafficPenaltyMode.OPTIMAL
        fr.updateRoutes(routes)
    }

    override fun onTitleChange(isRouteTitle: Boolean) {
        (getFragment(R.id.route_bottom_container) as? RouteDescriptionListFragment)?.updateTitle(isRouteTitle)
    }

    override fun onItemSelected(index: Int?, item: RouteDescriptionItem) {
        mapFragment?.renderRoute(item.route)
        startManeuver(item.route, item.isTrafficEnabled)
    }

    private fun startManeuver(route: Route, isTraffic: Boolean) {
        setVisible(R.id.route_top_container, false)
        setVisible(R.id.route_bottom_container, false)
        setVisible(R.id.mapfragment_wrapper, true)
        val maneuverListFragment = addFragment(R.id.route_mid_container, ManeuverListFragment::class.java, true)
        maneuverListFragment.updateRoute(route, isTraffic)
    }

    override fun onManeuverClicked(index: Int, maneuverDescriptionItem: ManeuverDescriptionItem) {
        maneuverDescriptionItem.maneuver?.boundingBox?.let {
            mapFragment?.zoomToBoundingBox(it)
        }
    }

    override fun zoomToRoute(withCustomBox: Boolean) {
        mapFragment?.zoomToRoute(withCustomBox)
    }

    override fun onPointSelectedOnMap(cord: GeoCoordinate) {
        // if current fragment is WaypointSelectionFragment, update co-ordinate
        (getFragment(R.id.route_top_container) as? WaypointSelectionFragment)?.updateCord(cord)
    }

    override fun onOptionPanelClicked(options: RouteOptions, dynamicPenalty: DynamicPenalty) {
        setVisible(R.id.route_top_container, false)
        setVisible(R.id.route_bottom_container, false)
        setVisible(R.id.mapfragment_wrapper, false)
        mapFragment?.onTouch(false)
        val optionPanelFragment = addFragment(R.id.route_mid_container, OptionPanelFragment::class.java, true)
        optionPanelFragment.dynamicPenalty = dynamicPenalty
        optionPanelFragment.routeOptions = options
        optionPanelFragment.listener = this
    }

    override fun openSubPanel(panelType: Panels, options: RouteOptions?) {
        val subPanel = addFragment(R.id.route_mid_container, SubOptionPanelFragment::class.java, true)
        subPanel.routeOptions = options
        subPanel.type = panelType
    }

    override fun trafficChanged(penalty: Route.TrafficPenaltyMode?) {
        penalty?.let {
            mapFragment?.traffic = it == Route.TrafficPenaltyMode.OPTIMAL
        }
    }

    override fun onBackPressed(): Boolean {
        var calculateRoute = false
        var backToHome = false
        val bottomFragment = getFragment(R.id.route_bottom_container)
        val midFragment = getFragment(R.id.route_mid_container)
        val topFragment = getFragment(R.id.route_top_container)

        when (topFragment) {
            is WaypointSelectionFragment -> {
                mapFragment?.clearMap()
                mapFragment?.onTouch(false)
                backToHome = true
                calculateRoute = true
            }
        }
        when (midFragment) {
            is OptionPanelFragment -> {
                midFragment.updateOptions()
                backToHome = true
                calculateRoute = true
            }
            is ManeuverListFragment -> backToHome = true
        }
        var backBlocked = false
        when (bottomFragment) {
            is RouteDescriptionListFragment -> {
                if (bottomFragment.isVisible) {
                    mapFragment?.clearMap()
                    plannerFragment?.reset()
                    removeFragment(R.id.route_bottom_container)
                    setVisible(R.id.mapfragment_wrapper, true)
                    backBlocked = true
                }
            }
        }

        if (backToHome) {
            setVisible(R.id.route_top_container, true)
            setVisible(R.id.route_bottom_container, true)
            setVisible(R.id.mapfragment_wrapper, true)
            plannerFragment?.updateActionBar()
        }
        val backProcessed = super.onBackPressed()
        if (calculateRoute) {
            plannerFragment?.calculateRoute()
        }
        return backBlocked || backProcessed
    }
}