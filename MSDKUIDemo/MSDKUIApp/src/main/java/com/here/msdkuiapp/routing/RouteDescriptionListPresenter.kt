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

import com.here.android.mpa.routing.Route
import com.here.msdkuiapp.base.BasePresenter

/**
 * Handles logic from [RouteDescriptionListFragment].
 */
class RouteDescriptionListPresenter : BasePresenter<RoutingContracts.RouteDescriptionList>() {

    private val state = State()

    /**
     * Sets Gets traffic mode.
     */
    var trafficMode: Boolean
        get() = state.traffic
        set(value) {
            state.traffic = value
        }

    /**
     * Populate UI.
     */
    fun makeUiDataReady() {
        contract?.onUiDataReady(state.traffic, state.routeList)
    }

    /**
     * Updates UI with a given list of route.
     *
     * @param routes list of route for populating the UI.
     */
    fun updateRoutes(routes: List<Route>) {
        state.routeList.clear()
        state.routeList.addAll(routes)
    }

    private class State {
        var routeList: ArrayList<Route> = ArrayList()
        var traffic = false
    }
}