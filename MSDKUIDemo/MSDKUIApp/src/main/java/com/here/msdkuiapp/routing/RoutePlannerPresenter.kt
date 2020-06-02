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

import android.util.Log
import android.util.SparseArray
import com.here.android.mpa.routing.CoreRouter
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteOptions
import com.here.android.mpa.routing.RouteResult
import com.here.android.mpa.routing.RouteWaypoint
import com.here.android.mpa.routing.RoutingError
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkui.routing.WaypointList
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BasePresenter
import com.here.msdkuiapp.common.AppActionBar
import com.here.msdkuiapp.common.Provider
import java.util.*

/**
 * Handles all logic of [RoutePlannerFragment].
 */
class RoutePlannerPresenter : BasePresenter<RoutingContracts.RoutePlanner>() {

    internal var appActionBar: AppActionBar? = null
    internal val state = State()
    internal var provider = Provider()
    var coordinatorListener: RoutePlannerFragment.Listener? = null

    /**
     * Sets Gets traffic mode.
     */
    var trafficMode: Route.TrafficPenaltyMode
        set(value) {
            state.dynamicPenalty.trafficPenaltyMode = value
        }
        get() = state.dynamicPenalty.trafficPenaltyMode

    /**
     * Waypoint List listener.
     */
    val waypointListener = object : WaypointList.Listener {
        override fun onEntryClicked(index: Int, current: WaypointEntry) {
            coordinatorListener?.onEntryClicked(index, current)
        }

        override fun onEntryAdded(index: Int, entry: WaypointEntry) {
            state.entryList.add(index, entry)
        }

        override fun onEntryUpdated(index: Int, entry: WaypointEntry) {}
        override fun onEntryRemoved(index: Int, entry: WaypointEntry) {
            state.entryList.removeAt(index)
            calculateRoute(state.entryList)
        }

        override fun onEntryDragged(fromIdx: Int, toIdx: Int) {
            val entry = state.entryList.removeAt(fromIdx)
            state.entryList.add(toIdx, entry)
            calculateRoute(state.entryList)
        }
    }

    /**
     * Updates UI or visibility of the action bar title, back & right icon.
     *
     * @param updateBack if back button needs to be updated.
     * @param updateTitle if title needs to be updated.
     * @param updateRightIcon if right icon needs to be update.
     */
    fun updateActionBar(updateBack: Boolean = false, updateTitle: Boolean = false, updateRightIcon: Boolean = false) {

        appActionBar?.run {
            if (updateBack)
                setBack(true)  // with default values

            if (updateTitle) {
                //title
                val isRouteTitle = !state.isExpanded && !state.entryList.isEmpty() && state.entryList.all { it.isValid }
                val text = if (isRouteTitle) getString(R.string.msdkui_app_route_results_title) else
                    getString(R.string.msdkui_app_rp_teaser_title)
                setTitle(value = text)
                coordinatorListener?.onTitleChange(isRouteTitle)
            }

            if (updateRightIcon) {
                val infoIconEnable = state.entryList.isEmpty() || !state.entryList.all { it.isValid }
                if (infoIconEnable) {
                    setRightIcon(false)
                    return
                }
                if (state.isExpanded) {
                    setRightIcon(accessibleValue = getString(R.string.msdkui_app_hint_collapse),
                            id = R.drawable.ic_collapse,
                            clickListener = { notifyListTitleChanges(listVisible = false, isTitleChange = true) })
                } else {
                    setRightIcon(accessibleValue = getString(R.string.msdkui_app_hint_expand),
                            id = R.drawable.ic_expande,
                            clickListener = { notifyListTitleChanges(listVisible = true, isTitleChange = true) })
                }
            }
        }
    }

    private fun notifyListTitleChanges(listVisible: Boolean, isTitleChange: Boolean) {
        state.isExpanded = listVisible
        contract?.updateList(listVisible = listVisible)
        updateActionBar(updateTitle = isTitleChange, updateRightIcon = true)
    }

    /**
     * Calculates the route with selected options in route planner.
     *
     * @param entries List of [WaypointEntry] to calculate routes.
     */
    fun calculateRoute(entries: List<WaypointEntry>) {
        RoutingIdlingResourceWrapper.increment()
        if (entries.isEmpty() || !entries.all { it.isValid }) {
            RoutingIdlingResourceWrapper.decrement()
            return
        }
        val waypoints = entries.map { it.routeWaypoint }
        contract?.onProgress(true)
        val routePlan = provider.provideRoutePlan()
        for (i in 0 until waypoints.size) {
            val waypoint = waypoints[i]
            waypoint.waypointType = if (i == 0 || i == waypoints.size - 1)
                RouteWaypoint.Type.STOP_WAYPOINT else RouteWaypoint.Type.VIA_WAYPOINT
            routePlan.addWaypoint(waypoint)
        }
        val router = provider.providesCoreRouter()
        with(state.routeOptions) {
            setTime(state.travelDate, state.travelType)
            routeCount = 5
            if (transportMode == RouteOptions.TransportMode.TRUCK || transportMode == RouteOptions.TransportMode.SCOOTER) {
                // for truck and scooter route type, only fastest route are supported.
                routeType = RouteOptions.Type.FASTEST
            }
            routePlan.routeOptions = this
        }
        val penalty = provider.providesDynamicPenalty()  // passing direct object get modified by sdk.
        penalty.trafficPenaltyMode = state.dynamicPenalty.trafficPenaltyMode
        router.setDynamicPenalty(penalty)
        router.calculateRoute(routePlan, object : CoreRouter.Listener {
            override fun onCalculateRouteFinished(inputList: List<RouteResult>, routingError: RoutingError) {
                contract?.onProgress(false)
                if (state.entryList.isEmpty() || !state.entryList.all { it.isValid }) {
                    // If entryList is empty or invalid when route calculation ends then it means
                    // that source waypoints has been cleared so result of this calculation
                    // should be abandoned.
                    return
                }
                if (inputList.isEmpty()) {
                    Log.e(RoutePlannerPresenter::class.java.name, "Routing failed  ${routingError.name}")
                    coordinatorListener?.onRoutingFailed(getString(R.string.msdkui_app_routeresults_error))
                    RoutingIdlingResourceWrapper.decrement()
                    return
                }
                notifyListTitleChanges(listVisible = false, isTitleChange = true)
                coordinatorListener?.onRouteCalculated(inputList.map { it.route })
                RoutingIdlingResourceWrapper.decrement()
            }

            override fun onProgress(i: Int) {}
        })
    }

    /**
     * Populates waypoint list.
     *
     * @param entries List of [WaypointEntry].
     * @param isFirstTime true if populating first time, false otherwise
     */
    fun makeWaypointListData(entries: List<WaypointEntry>?, isFirstTime: Boolean = false) {
        val entriesMap = SparseArray<WaypointEntry>()
        val entrySize = entries?.size ?: 0
        // add initial waypoints
        val stateCount = state.entryList.size
        if (entrySize > stateCount) {
            for (i in 0 until stateCount) {
                entriesMap.put(i, state.entryList[i])
            }
            for (i in stateCount until entrySize) {
                state.entryList.add(entries!![i])
            }
        } else {
            val list = state.entryList
            for (i in 0 until entrySize) {
                entriesMap.put(i,list[i])
            }
            for (i in entrySize until state.entryList.size) {
                entriesMap.put(i, state.entryList[i])
            }
        }
        contract?.onWaypointListDataReady(entriesMap)
        if (isFirstTime) {
            contract?.updateList(listVisible = state.isExpanded)
        }
    }

    /**
     * Populates swap button.
     */
    fun makeSwapReady() {
        val count = state.entryList.count { it.isValid }
        contract?.onSwapReady(count >= 2)
    }

    /**
     * Populates travel time panel.
     *
     * @param time time to populate the panel with.
     * @param timeType [RouteOptions.TimeType] to populate the panel with.
     *
     */
    fun makeTravelTimeData(time: Date?, timeType: RouteOptions.TimeType?) {

        if (state.travelDate == null) {
            state.travelDate = time
        }

        if (state.travelType == null) {
            state.travelType = timeType
        }

        contract?.onTravelTimeDataReady(state.travelDate, state.travelType)
    }

    /**
     * Calculates route when transport mode is selected.
     */
    fun transportModeSelected(selectedTransportMode: RouteOptions.TransportMode?) {
        if (state.routeOptions.transportMode != selectedTransportMode) {
            state.routeOptions.transportMode = selectedTransportMode
            calculateRoute(state.entryList)
        }
    }

    /**
     * Calculates route when swap is clicked.
     */
    fun swapClicked() {
        state.entryList.reverse()
        calculateRoute(state.entryList)
    }

    /**
     * Calculates route when time is changed.
     *
     * @param date travel date.
     * @param type travel time type.
     */
    fun timeChanged(date: Date?, type: RouteOptions.TimeType?) {
        state.travelDate = date
        state.travelType = type
        calculateRoute(state.entryList)
    }

    /**
     * Opens options panel.
     */
    fun openOptionPanel() {
        coordinatorListener?.onOptionPanelClicked(state.routeOptions, state.dynamicPenalty)
    }

    /**
     * Updates waypoints.
     *
     * @param index index of waypoint.
     * @param entry entry need to be updated.
     * @param entries list of entries.
     */
    fun updateWaypoint(index: Int, entry: WaypointEntry, entries: List<WaypointEntry>?) {
        if (state.entryList.size > index) {
            state.entryList[index] = entry
        } else {
            state.entryList.add(entry)
        }
        makeWaypointListData(entries)
        makeSwapReady()
    }

    /**
     * Reset the panel.
     *
     * @param entries List of [WaypointEntry] that will be added as starting one.
     */
    fun reset(entries: List<WaypointEntry>) {
        state.entryList.clear()
        entries.forEach { entry -> state.entryList.add(entry) }
        notifyListTitleChanges(listVisible = true, isTitleChange = true)
    }

    internal class State {
        var provider = Provider()

        val entryList = ArrayList<WaypointEntry>()
        var travelDate: Date? = null
        var travelType: RouteOptions.TimeType? = null
        val routeOptions: RouteOptions by lazy {
            provider.providesRouteOptions()
        }
        val dynamicPenalty: DynamicPenalty by lazy {
            provider.providesDynamicPenalty().apply {
                trafficPenaltyMode = Route.TrafficPenaltyMode.OPTIMAL
            }
        }
        var isExpanded = true
    }
}