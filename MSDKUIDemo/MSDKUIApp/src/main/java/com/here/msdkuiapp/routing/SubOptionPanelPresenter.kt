/*
 * Copyright (C) 2017-2019 HERE Europe B.V.
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

import android.widget.TextView
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkui.routing.HazardousMaterialsOptionsPanel
import com.here.msdkui.routing.RouteOptionsPanel
import com.here.msdkui.routing.TruckOptionsPanel
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BasePresenter
import com.here.msdkuiapp.common.AppActionBar
import com.here.msdkuiapp.common.routepreview.RoutePreviewFragment

/**
 * Handles logic of [SubOptionPanelFragment] view.
 */
class SubOptionPanelPresenter : BasePresenter<RoutingContracts.SubOptionPanel>() {

    private var state = State()
    /**
     * Sets & Gets [RouteOptions] to populate.
     */
    var routeOptions: RouteOptions?
        get() = state.routeOptions
        set(value) {
            state.routeOptions = value
        }

    /**
     * Sets the type of [OptionPanelFragment].
     */
    var type: Panels?
        get() = state.type
        set(value) {
            state.type = value
        }

    /**
     * Updates the action bar to show options corresponding to the [RoutePreviewFragment].
     */
    fun updateActionBar(appActionBar: AppActionBar?) {
        appActionBar?.run {
            setBack(visible = true, id = R.drawable.ic_arrow_back_black_24dp) //content description
            setTitle(value = context!!.getString(R.string.msdkui_app_options))
            setRightIcon(visible = false)
        }
    }

    fun makeUiDataReady(appActionBar: AppActionBar?) {
        val mPanelView = when (type) {
            Panels.DRIVE -> {
                appActionBar?.setTitle(value = context!!.getString(R.string.msdkui_routing_options_title))
                val panel = RouteOptionsPanel(context)
                panel.routeOptions = routeOptions
                panel
            }
            Panels.HAZARDOUS -> {
                appActionBar?.setTitle(value = context!!.getString(R.string.msdkui_hazardous_materials_title))
                val panel = HazardousMaterialsOptionsPanel(context)
                panel.routeOptions = routeOptions
                panel
            }
            Panels.TRUCK -> {
                appActionBar?.setTitle(value = context!!.getString(R.string.msdkui_truck_options_title))
                val panel = TruckOptionsPanel(context)
                panel.routeOptions = routeOptions
                panel
            }
            else -> {
                val panel = TextView(context) // empty view
                panel
            }
        }
        contract?.onUiDataReady(mPanelView)
    }

    private class State {
        var routeOptions: RouteOptions? = null
        var type: Panels? = null
    }
}