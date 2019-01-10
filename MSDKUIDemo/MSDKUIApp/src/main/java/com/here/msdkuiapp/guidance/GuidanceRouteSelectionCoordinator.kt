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

package com.here.msdkuiapp.guidance

import android.support.v4.app.FragmentManager
import android.content.Context
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.MapEngine
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseFragmentCoordinator
import com.here.msdkuiapp.common.routepreview.RoutePreviewFragment
import com.here.msdkuiapp.guidance.SingletonHelper.positioningManager
import com.here.msdkuiapp.isLocationOk
import com.here.msdkuiapp.map.MapFragmentWrapper

/**
 * Coordinator for managing (add, remove, show, hide etc) different views/panel for guidance route selection.
 */
class GuidanceRouteSelectionCoordinator(private val context: Context, fragmentManager: FragmentManager) :
        BaseFragmentCoordinator(fragmentManager), MapFragmentWrapper.Listener, GuidanceWaypointSelectionFragment.Listener,
        RoutePreviewFragment.Listener {

    internal var mapFragment: MapFragmentWrapper? = null
        get() = field ?: fragmentManager.findFragmentById(R.id.mapfragment_wrapper) as? MapFragmentWrapper

    internal val routePreviewFragment: RoutePreviewFragment? = null
        get() = field ?: getFragment(R.id.guidance_selection_bottom_container) as? RoutePreviewFragment

    private val guidanceWaypointSelectionFragment: GuidanceWaypointSelectionFragment? = null
        get() = field ?: getFragment(R.id.guidance_selection_top_container) as? GuidanceWaypointSelectionFragment

    /**
     * Init the map engine.
     */
    fun start() {
        mapFragment?.start(this::onEngineInit)
        // if bottom fragment is not there, add waypoint selection fragments
        getFragment(R.id.guidance_selection_bottom_container) ?: run {
            addFragment(R.id.guidance_selection_top_container, GuidanceWaypointSelectionFragment::class.java)
        }
    }

    private fun onEngineInit() {
        mapFragment?.run {
            traffic = true
            view?.isFocusable = true
        }
        if (context.isLocationOk) {
            onLocationReady()
        }
    }

    /**
     * Indicates location services and permission are ready to use.
     */
    fun onLocationReady() {
        if (MapEngine.isInitialized()) {
            guidanceWaypointSelectionFragment?.presenter?.onLocationReady()
        }
    }

    override fun onPositionAvailable() {
        positioningManager?.run {
            val geoCoordinate = if (hasValidPosition()) position?.coordinate else lastKnownPosition?.coordinate
            mapFragment?.showPositionIndicator()
            mapFragment?.map?.setCenter(geoCoordinate, Map.Animation.LINEAR)
        }
        mapFragment?.onTouch(true, this)
    }

    override fun onPointSelectedOnMap(cord: GeoCoordinate) {
        guidanceWaypointSelectionFragment?.updateCord(cord)
    }

    override fun onWaypointSelected(entry: WaypointEntry) {
        mapFragment?.onTouch(false)
        removeFragment(R.id.guidance_selection_top_container)
        val fragment = addFragment(R.id.guidance_selection_bottom_container, RoutePreviewFragment::class.java, true)
        fragment.setWaypoint(entry)
    }

    override fun renderRoute(route: Route) {
        with(mapFragment!!) {
            renderAndZoomTo(route, false)
        }
    }

    /**
     * Manages views on back button pressed.
     */
    override fun onBackPressed(): Boolean {
        getFragment(R.id.guidance_selection_bottom_container)?.run {
            addFragment(R.id.guidance_selection_top_container, GuidanceWaypointSelectionFragment::class.java)
            with(mapFragment!!) {
                clearMap()
                onPositionAvailable()
            }
        }
        return super.onBackPressed()
    }
}
