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
import android.content.Intent
import com.here.android.mpa.guidance.NavigationManager
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteResult
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseFragmentCoordinator
import com.here.msdkuiapp.msdkuiApplication
import com.here.msdkuiapp.common.Provider
import com.here.msdkuiapp.guidance.SingletonHelper.navigationManager
import com.here.msdkuiapp.isLocationOk
import com.here.msdkuiapp.landing.LandingActivity
import com.here.msdkuiapp.map.MapFragmentWrapper
import java.lang.ref.WeakReference

/**
 * Coordinator for managing (add, remove, show, hide etc) different views/panel for guidance activity.
 */
class GuidanceCoordinator(private val context: Context, fragmentManager: FragmentManager) :
        BaseFragmentCoordinator(fragmentManager) {

    internal val rerouteListener: NavigationManager.RerouteListener =
            object : NavigationManager.RerouteListener() {
                override fun onRerouteEnd(rerouteResult: RouteResult) {
                    rerouteResult.route?.run {
                        updateRoute(this)
                    }
                }
            }

    internal val trafficRerouteListener: NavigationManager.TrafficRerouteListener =
            object : NavigationManager.TrafficRerouteListener() {
                override fun onTrafficRerouted(rerouteResult: RouteResult) {
                    rerouteResult.route?.run {
                        updateRoute(this)
                    }
                }
            }

    internal val navigationManagerEventListener: NavigationManager.NavigationManagerEventListener =
            object : NavigationManager.NavigationManagerEventListener() {
                override fun onEnded(navigationMode : NavigationManager.NavigationMode?) {
                    didGuidanceFinished = true
                }
            }

    internal var route: Route? = null

    internal var mapFragment: MapFragmentWrapper? = null
        get() = field ?: fragmentManager.findFragmentById(R.id.mapfragment_wrapper) as? MapFragmentWrapper

    internal var isSimulation: Boolean = false
    internal var simulationSpeed: Long = DEFAULT_SIMULATION_SPEED
    internal var didGuidanceFinished: Boolean = false

    var provider: Provider? = null
        get() = field ?: Provider()

    companion object {
        /**
         * Speed of simulation - meters per second.
         */
        const val DEFAULT_SIMULATION_SPEED: Long = 12

        /**
         * Zoom level of map
         */
        const val MAP_ZOOM = 18.40

        /**
         * Tilt of map
         */
        const val MAP_TILT = 72.0f
    }

    /**
     * Init the map engine.
     */
    fun start() {
        mapFragment?.start(this::onEngineInit)
    }

    /**
     * Called when owning activity is destroyed.
     */
    fun destroy() {
        navigationManager?.run {
            removeRerouteListener(rerouteListener)
            removeTrafficRerouteListener(trafficRerouteListener)
            removeNavigationManagerEventListener(navigationManagerEventListener)
        }
    }

    /**
     * Called when map engine is initialize.
     */
    private fun onEngineInit() {
        doMapSettings(mapFragment!!.map!!)
        route = context.msdkuiApplication.route
        init()
    }

    private fun init() {
        if (route == null || context.isLocationOk.not()) {
            context.startActivity(Intent(context, LandingActivity::class.java))
            return
        }
        navigationManager?.run {
            addRerouteListener(WeakReference(rerouteListener))
            addTrafficRerouteListener(WeakReference(trafficRerouteListener))
            addNavigationManagerEventListener(WeakReference(navigationManagerEventListener))
        }

        renderRoute()
        addManeuverPanel()
        addNextManeuverPanel()
        addStreetNameView()
        addEstimatedArrivalTimeView()
        addSpeedPanel()
        addSpeedLimitPanel()
        doNavigationSettings(mapFragment!!.map!!)
    }

    private fun updateRoute(updatedRoute: Route) {
        context.msdkuiApplication.route = updatedRoute
        route = updatedRoute
        renderRoute()
    }

    private fun doNavigationSettings(map: Map) {
        navigationManager?.apply {
            setMap(map)
            if (runningState != NavigationManager.NavigationState.RUNNING && !didGuidanceFinished) {
                mapUpdateMode = NavigationManager.MapUpdateMode.POSITION
                if (isSimulation) {
                    simulate(route, simulationSpeed)
                } else {
                    startNavigation(route)
                }
            }
        }
    }

    private fun doMapSettings(map: Map) {
        with(mapFragment!!) {
            traffic = true
            showPositionIndicator()
        }
        with(map) {
            setLandmarksVisible(true)
            setExtrudedBuildingsVisible(true)
            zoomLevel = MAP_ZOOM
            tilt = MAP_TILT
        }
    }

    /**
     * Adds Maneuver panel to guidance view screen.
     */
    private fun addManeuverPanel() {
        val fragment = addFragment(R.id.maneuver_panel_container, GuidanceManeuverFragment::class.java, false)
        fragment.route = route
    }

    private fun addNextManeuverPanel() {
        val fragment = addFragment(R.id.next_maneuver_panel_container, GuidanceNextManeuverFragment::class.java, false)
        fragment.route = route
    }

    private fun addEstimatedArrivalTimeView() {
        addFragment(R.id.eta_view_container, GuidanceEstimatedArrivalFragment::class.java, false)
    }

    private fun addStreetNameView() {
        val fragment = addFragment(R.id.current_street_name_fragment, GuidanceStreetLabelFragment::class.java, false)
        fragment.route = route
    }

    private fun addSpeedPanel() {
        addFragment(R.id.current_speed_panel_container, GuidanceCurrentSpeedFragment::class.java, false)
    }

    private fun addSpeedLimitPanel() {
        addFragment(R.id.speed_limit_container, GuidanceSpeedLimitFragment::class.java, false)
    }

    /**
     * Renders route on map.
     */
    private fun renderRoute() {
        route?.run {
            mapFragment?.renderRoute(this@run, false)
            mapFragment?.map?.setCenter(start, Map.Animation.LINEAR)
        }
    }

    /**
     * Manages views on back button pressed.
     */
    override fun onBackPressed(): Boolean {
        // stop navigation
        navigationManager?.stop()
        return super.onBackPressed()
    }
}