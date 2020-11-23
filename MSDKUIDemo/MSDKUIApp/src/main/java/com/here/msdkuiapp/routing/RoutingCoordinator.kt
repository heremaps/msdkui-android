/*
 * Copyright (C) 2017-2020 HERE Europe B.V.
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

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.widget.TextView
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseFragmentCoordinator
import com.here.msdkuiapp.common.mapselection.WaypointSelectionFragment
import com.here.msdkuiapp.common.routepreview.RoutePreviewFragment

/**
 * Coordinator for managing (add, remove, show, hide etc) different views/panel for routing activity.
 */
class RoutingCoordinator(private val context: Context, fragmentManager: FragmentManager) :
        BaseFragmentCoordinator(fragmentManager), RoutePlannerFragment.Listener,
        WaypointSelectionFragment.Listener, RouteDescriptionListFragment.Listener,
        OptionPanelFragment.Listener, RoutePreviewFragment.Listener {

    private val topFragment: Fragment?
        get() = getFragment(R.id.route_top_container)

    private val bottomFragment: Fragment?
        get() = getFragment(R.id.route_bottom_container)

    private val plannerFragment: RoutePlannerFragment?
        get() = topFragment as? RoutePlannerFragment

    /**
     * Starts the map engine.
     */
    fun start() {
        topFragment ?: addFragment(R.id.route_top_container, RoutePlannerFragment::class.java)
        var isHome = false
        if(getFragment(R.id.route_mid_container) == null) {
            isHome = true
        }
        if (isHome) {
            setVisible(R.id.route_mid_container, false)
            bottomFragment
                    ?: addFragment(R.id.route_bottom_container, RoutingInstructionsFragment::class.java)
        } else {
            setVisible(R.id.route_bottom_container, false)
        }
    }

    override fun onEntryClicked(index: Int, current: WaypointEntry) {
        setVisible(R.id.route_bottom_container, false)
        setVisible(R.id.route_top_container, false);
        val selectionFragment = addFragment(R.id.route_mid_container, WaypointSelectionFragment::class.java, true)
        val penalty = plannerFragment?.trafficMode
        selectionFragment.traffic = penalty == Route.TrafficPenaltyMode.OPTIMAL
        selectionFragment.updatePosition(index, current)
    }

    override fun onWaypointSelectionCancelled(index: Int?, current: WaypointEntry?) {
        plannerFragment?.waypointSelectionCancelled(index)
    }

    override fun onWaypointSelected(index: Int?, current: WaypointEntry) {
        if (index != null) {
            onBackPressed()
            val fragment = plannerFragment
            setVisible(R.id.route_top_container, true)
            setVisible(R.id.route_bottom_container, true)
            fragment?.updateWaypoint(index, current)
            fragment?.calculateRoute()
        }
    }

    override fun onRouteCalculated(routes: List<Route>) {
        removeFragment(R.id.route_mid_container)
        val fr = addFragment(R.id.route_bottom_container, RouteDescriptionListFragment::class.java, true)
        setVisible(R.id.route_bottom_container, true)
        val penalty = plannerFragment?.trafficMode
        fr.traffic = penalty == Route.TrafficPenaltyMode.OPTIMAL
        fr.updateRoutes(routes)
    }

    override fun onRoutingFailed(reason: String) {
        //reset the old route
        (getFragment(R.id.route_bottom_container) as? RouteDescriptionListFragment)?.updateRoutes(arrayListOf())
        setVisible(R.id.route_bottom_container, false)
        (context as Activity).findViewById<TextView>(R.id.error_message_holder).text = reason
    }

    override fun onTitleChange(isRouteTitle: Boolean) {
        (getFragment(R.id.route_bottom_container) as? RouteDescriptionListFragment)?.updateTitle(isRouteTitle)
    }

    override fun onItemSelected(index: Int?, item: RouteDescriptionItem) {
        startManeuver(item.route, item.isTrafficEnabled)
    }

    private fun startManeuver(route: Route, isTraffic: Boolean) {
        setVisible(R.id.route_top_container, false)
        setVisible(R.id.route_bottom_container, false)
        val maneuverListFragment = addFragment(R.id.route_mid_container, RouteManeuverListFragment::class.java, true)
        maneuverListFragment.setRoute(route, isTraffic)
    }

    override fun onOptionPanelClicked(options: RouteOptions, dynamicPenalty: DynamicPenalty) {
        setVisible(R.id.route_top_container, false)
        setVisible(R.id.route_bottom_container, false)
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
            plannerFragment?.trafficMode = it
        }
    }

    override fun renderRoute(route: Route) {
        (getFragment(R.id.route_mid_container) as? RouteManeuverListFragment)?.renderRoute(route)
    }

    override fun onBackPressed(): Boolean {
        var backToHome = false
        var reCalculate = false
        val bottomFragment = getFragment(R.id.route_bottom_container)
        val midFragment = getFragment(R.id.route_mid_container)

        when (midFragment) {
            is OptionPanelFragment -> {
                midFragment.updateOptions()
                backToHome = true
                reCalculate = true
            }
            is WaypointSelectionFragment -> backToHome = true
            is RouteManeuverListFragment -> backToHome = true
        }
        var backBlocked = false
        when (bottomFragment) {
            is RouteDescriptionListFragment -> {
                if (bottomFragment.isVisible) {
                    plannerFragment?.reset()
                    removeFragment(R.id.route_bottom_container)
                    backBlocked = true
                }
            }
        }

        if (backToHome) {
            setVisible(R.id.route_top_container, true)
            setVisible(R.id.route_bottom_container, true)
            plannerFragment?.updateActionBar()
        }
        if (reCalculate) {
            plannerFragment?.calculateRoute()
        }
        val backProcessed = super.onBackPressed()
        return backBlocked || backProcessed
    }
}