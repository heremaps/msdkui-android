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

import android.view.View
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkui.routing.*
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BasePresenter
import com.here.msdkuiapp.common.AppActionBar

/**
 * Handles logic of [OptionPanelFragment] view.
 */
class OptionPanelPresenter : BasePresenter<RoutingContracts.OptionPanel>() {

    private var state: State = State()
    private var panels = LinkedHashMap<Panels, View>()

    /**
     * Traffic change listener.
     */
    private val trafficChangeListener = object : OptionsPanel.Listener {
        override fun onOptionChanged(item: OptionItem?) {
            contract?.trafficChanged(state.dynamicPenalty?.trafficPenaltyMode)
        }

        override fun onOptionCreated(item: MutableList<OptionItem>?) {
        }
    }


    /**
     * Sets & Gets [RouteOptions] to populate.
     */
    var routeOptions: RouteOptions?
        get() = state.routeOptions
        set(value) {
            state.routeOptions = value
        }

    /**
     * Sets & Gets [DynamicPenalty] to populate.
     */
    var dynamicPenalty: DynamicPenalty?
        get() = state.dynamicPenalty
        set(value) {
            state.dynamicPenalty = value
        }

    /**
     * Updates action bar for view [OptionPanelFragment].
     */
    fun updateActionBar(appActionBar: AppActionBar?) {
        appActionBar?.run {
            setBack(visible = true)      // content desc
            setTitle(value = context!!.getString(R.string.msdkui_app_options))
            setRightIcon(visible = false)
        }
    }

    /**
     *  Populates UI.
     */
    fun makeUiDataReady() {
        val routeTypePanel = RouteTypeOptionsPanel(context)
        routeTypePanel.routeOptions = state.routeOptions
        panels[Panels.ROUTE_TYPE] = routeTypePanel

        val trafficPanel = TrafficOptionsPanel(context)
        trafficPanel.dynamicPenalty = state.dynamicPenalty
        panels[Panels.TRAFFIC] = trafficPanel
        trafficPanel.setListener(trafficChangeListener)

        val driveView = contract?.getRowView(R.string.msdkui_routing_options_title)
        driveView?.run {
            setOnClickListener({ contract?.onSubPanelClicked(Panels.DRIVE, state.routeOptions) })
            panels[Panels.DRIVE] = this
        }

        val tunnelPanel = TunnelOptionsPanel(context)
        tunnelPanel.routeOptions = state.routeOptions
        panels[Panels.TUNNEL] = tunnelPanel

        val hazardousView = contract?.getRowView(R.string.msdkui_hazardous_materials_title)
        hazardousView?.run {
            setOnClickListener({ contract?.onSubPanelClicked(Panels.HAZARDOUS, state.routeOptions) })
            panels[Panels.HAZARDOUS] = this
        }

        val truckView = contract?.getRowView(R.string.msdkui_truck_options_title)
        truckView?.run {
            setOnClickListener({ contract?.onSubPanelClicked(Panels.TRUCK, state.routeOptions) })
            panels[Panels.TRUCK] = this
        }

        val mode = routeOptions?.transportMode
        val views: Collection<View> = when (mode) {
            RouteOptions.TransportMode.TRUCK -> {
                panels.filterKeys { it != Panels.ROUTE_TYPE }.values
            }
            RouteOptions.TransportMode.BICYCLE, RouteOptions.TransportMode.PEDESTRIAN -> {
                panels.filterKeys { it == Panels.DRIVE }.values
            }
            RouteOptions.TransportMode.SCOOTER -> {
                panels.filterKeys {
                    it == Panels.DRIVE || it == Panels.TRAFFIC
                }.values
            }
            else -> {
                panels.filterKeys {
                    it == Panels.ROUTE_TYPE || it == Panels.DRIVE ||
                            it == Panels.TRAFFIC
                }.values
            }
        }
        contract?.onUiDataReady(views)
    }

    /**
     * Updates selected options in [RouteOptions] for route calculation.
     */
    fun updateOptions() {
        for ((panel, panelView) in panels) {
            when (panel) {
                Panels.ROUTE_TYPE -> state.routeOptions = (panelView as RouteTypeOptionsPanel).routeOptions
                Panels.TRAFFIC -> state.dynamicPenalty = (panelView as TrafficOptionsPanel).dynamicPenalty
                Panels.TUNNEL -> state.routeOptions = (panelView as TunnelOptionsPanel).routeOptions
                else -> print("${context!!.getString(R.string.msdkui_unknown)} ${panel.name}")
            }
        }
    }

    private class State {
        var routeOptions: RouteOptions? = null
        var dynamicPenalty: DynamicPenalty? = null
    }
}

/**
 * Class to define different type of panel.
 */
enum class Panels {
    DRIVE, ROUTE_TYPE, TRAFFIC, TUNNEL, HAZARDOUS, TRUCK
}