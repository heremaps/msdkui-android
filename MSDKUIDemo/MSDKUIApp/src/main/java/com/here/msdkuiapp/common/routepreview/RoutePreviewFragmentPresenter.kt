/*
 * Copyright (C) 2017-2021 HERE Europe B.V.
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

package com.here.msdkuiapp.common.routepreview

import android.app.AlertDialog
import android.content.Intent
import android.util.Log
import com.here.android.mpa.routing.*
import com.here.android.mpa.search.ErrorCode
import com.here.android.mpa.search.Location
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.GuidanceContracts
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BasePresenter
import com.here.msdkuiapp.common.AppActionBar
import com.here.msdkuiapp.common.Constant.GUIDANCE_IS_SIMULATION_KEY
import com.here.msdkuiapp.common.Constant.GUIDANCE_SIMULATION_SPEED
import com.here.msdkuiapp.common.Provider
import com.here.msdkuiapp.guidance.GuidanceActivity
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import com.here.msdkuiapp.msdkuiApplication
import java.util.Date

/**
 * Presenter class to handle logic of [RoutePreviewFragment].
 */
class RoutePreviewFragmentPresenter() : BasePresenter<GuidanceContracts.RoutePreview>() {

    private var state = State()
    var provider = Provider()
    var simulationSpeed: Long = 0

    /**
     * Sets already calculated route to render all route information on this fragment.
     *
     * @param route [Route]
     */
    fun setRoute(route: Route, isTrafficEnabled: Boolean = true) {
        with(state) {
            this.route = route
            this.isTrafficEnabled = isTrafficEnabled
        }
    }

    /**
     * Sets destination for route calculation.
     *
     * @param destination [WaypointEntry].
     */
    fun setWaypoint(destination: WaypointEntry, withCurrentPosition: Boolean = true) {
        state.destination = destination
        if (withCurrentPosition.not()) return
        appPositioningManager?.customLocation?.run {
            state.cordsList.add(provider.providesRouteWaypoint(this))
        }
        state.cordsList.add(destination.routeWaypoint)
    }


    /**
     * Updates the action bar to show options corresponding to the [RoutePreviewFragment].
     */
    fun updateActionBar(appActionBar: AppActionBar?) {
        appActionBar?.run {
            setBack(visible = true)
            setRightIcon(false)
            setTitle(value = context!!.getString(R.string.msdkui_app_route_preview_title))
        }
    }

    /**
     * Gets destination name via geo-coding or calculates route based on states.
     */
    fun doSetup() {
        contract?.onProgress(true)
        state.route?.run {
            destination?.let {
                provider.providesReverseGeocodeRequest(it).execute { location: Location?, errorCode: ErrorCode? ->
                    if (errorCode == ErrorCode.NONE) {
                        state.destination = WaypointEntry(provider.providesRouteWaypoint(it), location?.address?.text
                                ?: "")
                    }
                    contract?.run {
                        // Interact with contracts ui only if this ui exist.
                        if (rootViewExist()) {
                            onProgress(false)
                            populateUI()
                        }
                    }
                }
            }
            return
        }

        if (state.cordsList.size < 2 || !state.cordsList.all { it.originalPosition.isValid }) {
            contract?.onProgress(false)
            state.errorMessage = getString(R.string.msdkui_app_routeresults_error)
            contract?.routingFailed(state.errorMessage)
            return
        }
        val router = provider.providesCoreRouter()
        router.dynamicPenalty.trafficPenaltyMode = Route.TrafficPenaltyMode.OPTIMAL
        val routePlan = provider.provideRoutePlan()
        routePlan.routeOptions = with(routePlan.routeOptions) {
            transportMode = RouteOptions.TransportMode.CAR
            setTime(Date(), RouteOptions.TimeType.DEPARTURE)
        }
        state.cordsList.forEach { waypoint -> routePlan.addWaypoint(waypoint) }
        router.calculateRoute(routePlan, object : CoreRouter.Listener {
            override fun onCalculateRouteFinished(inputList: List<RouteResult>, routingError: RoutingError) {
                contract?.onProgress(false)
                if (inputList.isEmpty() || routingError != RoutingError.NONE) {
                    Log.e(RoutePreviewFragmentPresenter::class.java.name, "Routing failed  ${routingError.name}")
                    state.errorMessage = getString(R.string.msdkui_app_routeresults_error)
                    contract?.routingFailed(state.errorMessage)
                    return
                }
                val routeList = ArrayList<Route>()
                inputList.forEach { routeList.add(it.route) }
                state.route = inputList[0].route
                populateUI()
            }

            override fun onProgress(i: Int) {}
        })
    }

    override fun isStateValid(): Boolean {
        return state.destination != null || state.route != null
    }

    /**
     * Populate UI with route & destination information.
     */
    fun populateUI() {
        state.route?.run {
            contract?.populateUI(state.destination!!, this, state.listVisible, state.isTrafficEnabled)
        } ?: run {
            if (state.errorMessage.isNotBlank()) {
                contract?.routingFailed(state.errorMessage)
            }
        }
    }

    /**
     * Starts guidance
     * @param isSimulation true value starts simulation, otherwise it starts normal guidance.
     */
    fun startGuidance(isSimulation: Boolean) {
        state.route?.run {
            val intent = Intent(context, GuidanceActivity::class.java)
            intent.putExtra(GUIDANCE_IS_SIMULATION_KEY, isSimulation)
            if (isSimulation && simulationSpeed > 0) {
                intent.putExtra(GUIDANCE_SIMULATION_SPEED, simulationSpeed)
            }
            context?.msdkuiApplication?.route = this
            context?.startActivity(intent)
            simulationSpeed = 0
        }
    }

    /**
     * Shows or hides route maneuvers list.
     */
    fun toggleSteps() {
        state.listVisible = !state.listVisible  // toggle
        contract?.toggleSteps(state.listVisible)
    }

    fun showStartSimulationAlertDialog() {
        AlertDialog.Builder(context)
                .setMessage(R.string.msdkui_app_guidance_start_simulation)
                .setCancelable(true)
                .setNegativeButton(R.string.msdkui_app_cancel, null)
                .setPositiveButton(R.string.msdkui_app_ok) { _, _ ->
                    startGuidance(true)
                }
                .create()
                .show()
    }

    /**
     * Class to hold information required by [RoutePreviewFragmentPresenter].
     */
    private class State {
        val cordsList = ArrayList<RouteWaypoint>()
        var destination: WaypointEntry? = null
        var route: Route? = null
        var errorMessage = ""
        var listVisible = false
        var isTrafficEnabled: Boolean = true
    }
}