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
import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkuiapp.R
import kotlinx.android.synthetic.main.maneuver.*

/**
 * Displays Maneuver of a route.
 */
class ManeuverRouteDescriptionFragment() : Fragment(), RoutingContracts.ManeuverRouteDescription {

    private var presenter = ManeuverRouteDescriptionPresenter()

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = ManeuverRouteDescriptionFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.maneuver, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.run {
            onAttach(activity, this@ManeuverRouteDescriptionFragment)
            makeUiDataReady()
        }
    }

    /**
     * Updates route to populate the view.
     *
     * @param route [Route].
     */
    fun updateRoute(route: Route) {
        presenter.updateRoute(route)
    }

    override fun onUiDataReady(item: RouteDescriptionItem) {
        route_item_container.addView(item)
    }

}