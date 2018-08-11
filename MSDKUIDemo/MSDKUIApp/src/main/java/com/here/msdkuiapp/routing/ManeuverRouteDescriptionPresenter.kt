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

import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkuiapp.base.BasePresenter

/**
 * Handles logic of [ManeuverRouteDescriptionFragment] view.
 */
class ManeuverRouteDescriptionPresenter : BasePresenter<RoutingContracts.ManeuverRouteDescription>() {

    private val state = State()

    /**
     * Populates UI.
     */
    fun makeUiDataReady() {
        val item = RouteDescriptionItem(context)
        item.route = state.route
        contract?.onUiDataReady(item)
    }

    /**
     * Update route to populate the UI.
     */
    fun updateRoute(route: Route) {
        state.route = route
    }

    private class State {
        var route: Route? = null
    }
}