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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkui.routing.WaypointList
import com.here.msdkuiapp.R
import com.here.msdkuiapp.appActionBar
import com.here.msdkuiapp.coordinator
import com.here.msdkuiapp.showProgressBar
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.route_planner.*
import java.util.*

/**
 * Fragment to deal with route planner UI.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class RoutePlannerFragment() : Fragment(), RoutingContracts.RoutePlanner {

    private var waypointList: WaypointList? = null
    val presenter = RoutePlannerPresenter()

    var trafficMode: Route.TrafficPenaltyMode
        set(value) {
            presenter.trafficMode = value
        }
        get() = presenter.trafficMode

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = RoutePlannerFragment()
    }

    override fun onResume() {
        super.onResume()
        waypointList?.entries?.run {
            waypointList?.scrollToPosition(size - 1)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        presenter.appActionBar = activity?.appActionBar
        presenter.coordinatorListener = coordinator as? RoutingCoordinator
        return inflater.inflate(R.layout.route_planner, container, false).apply { isClickable = true }
    }

    override fun updateList(listVisible: Boolean) {
        val visibility = if (listVisible) View.VISIBLE else View.GONE
        waypointList?.visibility = visibility
        waypoint_add?.visibility = visibility
        swap_list?.visibility = visibility
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        waypointList = waypoint_list
        presenter.run {
            onAttach(activity, this@RoutePlannerFragment)
            updateActionBar(updateBack = true, updateTitle = true, updateRightIcon = true)
            makeWaypointListData(waypointList!!.entries, true)
            waypointList?.setListener(waypointListener)
            makeTravelTimeData(travel_time_panel.time, travel_time_panel.timeType)
            option_panel.setOnClickListener({ openOptionPanel() })
            makeSwapReady()
        }
        makeWaypointAddReady()
        makeTransportModeReady()
    }


    override fun onWaypointListDataReady(entriesMap: Map<Int, WaypointEntry>) {
        val size = waypointList!!.entries.size
        entriesMap.forEach { (index, entry) ->
            if (index < size) {
                waypointList!!.updateEntry(index, entry)
            } else {
                waypointList!!.addEntry(entry)
            }
        }
    }

    override fun onTravelTimeDataReady(time: Date?, timeType: RouteOptions.TimeType?) {
        with(travel_time_panel) {
            // set min date today.
            picker.minDate = Date().time
            this.time = time
            this.timeType = timeType
            setOnTimeChangedListener { date, type ->
                presenter.timeChanged(date, type)
            }
        }
    }

    override fun onSwapReady(enable: Boolean) {
        swap_list?.run {
            setOnClickListener {
                waypointList?.reverse()
                presenter.swapClicked()
            }
            isEnabled = enable
        }
    }

    override fun onRoutingFailed(reason: String) {
        Toast.makeText(activity, reason, Toast.LENGTH_LONG).show()
    }

    override fun onProgress(visible: Boolean) {
        activity?.showProgressBar(visible)
    }

    private fun makeWaypointAddReady() {
        waypoint_add?.setOnClickListener({
            waypointList?.let {
                val entry = it.addEmptyEntry()
                presenter.waypointListener.onEntryClicked(it.entriesCount - 1, entry)
            }
        })
    }

    private fun makeTransportModeReady() {
        transport_panel.run {
            setOnSelectedListener { _, _ -> presenter.transportModeSelected(selectedTransportMode) }
        }
    }

    /**
     * Updates waypoint to [WaypointList].
     *
     * @param index index within the list where [WaypointEntry] needs to be updated.
     * @param current [WaypointEntry] that needs to be updated.
     */
    fun updateWaypoint(index: Int, current: WaypointEntry) {
        presenter.updateWaypoint(index, current, waypointList?.entries)
    }

    /**
     * Resets the route planner panel to default.
     */
    fun reset() {
        waypointList?.reset()
        presenter.reset(waypointList!!.entries)
        travel_time_panel?.time = Date()
    }

    /**
     * Calculates the route with selected options in route planner.
     */
    fun calculateRoute() {
        if (!this.userVisibleHint) {
            return
        }
        val list = waypointList ?: return
        presenter.calculateRoute(list.entries)
    }

    /**
     * Updates UI or visibility of the action bar title, back & right icon.
     *
     * @param updateBack if back button needs to be updated.
     * @param updateTitle if title needs to be updated.
     * @param updateRightIcon if right icon needs to be update.
     */
    fun updateActionBar(updateBack: Boolean = true, updateTitle: Boolean = true, updateRightIcon: Boolean = true) {
        presenter.updateActionBar(updateBack = updateBack, updateTitle = updateTitle, updateRightIcon = updateRightIcon)
    }

    /**
     * Listeners to be notified by route planner component.
     */
    interface Listener {

        /**
         * To be called when a entry is clicked in [WaypointList].
         *
         * @param index index of entry in list.
         * @param current clicked [WaypointEntry].
         */
        fun onEntryClicked(index: Int, current: WaypointEntry)

        /**
         * To be called when routes is calculated.
         *
         * @param routes the calculated routes.
         */
        fun onRouteCalculated(routes: List<Route>)

        /**
         * To be called when option panel is clicked.
         *
         * @param options updated [RouteOptions].
         * @param dynamicPenalty updated [DynamicPenalty].
         */
        fun onOptionPanelClicked(options: RouteOptions, dynamicPenalty: DynamicPenalty)

        /**
         * To be called when there is a title change.
         *
         * @param isRouteTitle true if route is already calculated, false otherwise.
         */
        fun onTitleChange(isRouteTitle: Boolean)
    }
}
