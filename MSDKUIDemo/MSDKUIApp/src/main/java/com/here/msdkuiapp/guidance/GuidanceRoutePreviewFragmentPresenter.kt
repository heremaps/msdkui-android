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

import android.content.Intent
import android.os.Bundle
import com.here.android.mpa.routing.*
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.GuidanceContracts
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BasePresenter
import com.here.msdkuiapp.common.AppActionBar
import com.here.msdkuiapp.common.Constant.GUIDANCE_IS_SIMULATION_KEY
import com.here.msdkuiapp.common.Constant.ROUTE_KEY
import com.here.msdkuiapp.common.Provider
import com.here.msdkuiapp.guidance.SingletonHelper.positioningManager

/**
 * Presenter class to handle logic of [GuidanceRoutePreviewFragment].
 */
class GuidanceRoutePreviewFragmentPresenter() : BasePresenter<GuidanceContracts.RoutePreview>() {

    private var state = State()
    var provider = Provider()

    /**
     * Updates the action bar to show options corresponding to the [GuidanceRoutePreviewFragment].
     */
    fun updateActionBar(appActionBar: AppActionBar?) {
        appActionBar?.run {
            setBack(visible = true)
            setRightIcon(false)
            setTitle(value = context!!.getString(R.string.msdkui_app_route_preview_title))
        }
    }

    /**
     * Sets destination for route calculation.
     *
     * @param entry [WaypointEntry].
     */
    fun setDestination(entry: WaypointEntry) {
        state.destination = entry
        positioningManager?.run {
            when {
                hasValidPosition() -> state.cordsList.add(provider.providesRouteWaypoint(position.coordinate))
                lastKnownPosition != null && lastKnownPosition.isValid -> state.cordsList.add(provider
                        .providesRouteWaypoint(lastKnownPosition.coordinate))
                else -> {
                }
            }
        }
        state.cordsList.add(entry.routeWaypoint)
    }

    /**
     * Calculates route.
     */
    fun calculateRoute() {
        contract?.onProgress(true)
        if (state.cordsList.size < 2 || !state.cordsList.all { it.originalPosition.isValid }) {
            contract?.onProgress(false)
            state.errorMessage = getString(R.string.msdkui_no_route_found)
            contract?.routingFailed(state.errorMessage)
            return
        }
        val router = provider.providesCoreRouter()
        router.dynamicPenalty.trafficPenaltyMode = Route.TrafficPenaltyMode.OPTIMAL
        val routePlan = provider.provideRoutePlan()
        routePlan.routeOptions.transportMode = RouteOptions.TransportMode.CAR
        state.cordsList.forEach { waypoint -> routePlan.addWaypoint(waypoint) }
        router.calculateRoute(routePlan, object : CoreRouter.Listener {
            override fun onCalculateRouteFinished(inputList: List<RouteResult>, routingError: RoutingError) {
                contract?.onProgress(false)
                if (inputList.isEmpty() || routingError != RoutingError.NONE) {
                    state.errorMessage = getString(R.string.msdkui_no_route_found)
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
        return state.destination != null
    }

    /**
     * Populate UI with route & destination information.
     */
    fun populateUI() {
        state.route?.run {
            contract?.populateUI(state.destination!!, this)
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
        contract?.onProgress(true)
        state.route?.run {
            provider.provideSerialize(this, Route.SerializationCallback { serializationResult ->
                contract?.onProgress(false)
                if (serializationResult.error == Route.SerializerError.NONE) {
                    val intent = Intent(context, GuidanceActivity::class.java)
                    val bundle = Bundle()
                    bundle.putByteArray(ROUTE_KEY, serializationResult.data)
                    bundle.putBoolean(GUIDANCE_IS_SIMULATION_KEY, isSimulation)
                    context?.startActivity(intent.putExtras(bundle))
                }
            })
        }
    }

    /**
     * Class to hold information required by [GuidanceRoutePreviewFragmentPresenter].
     */
    private class State {
        val cordsList = ArrayList<RouteWaypoint>()
        var destination: WaypointEntry? = null
        var route: Route? = null
        var errorMessage = ""
    }
}