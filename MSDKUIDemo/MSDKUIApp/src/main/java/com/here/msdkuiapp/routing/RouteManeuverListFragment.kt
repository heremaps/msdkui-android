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

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.routing.Route
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BaseFragmentCoordinator
import com.here.msdkuiapp.base.RetainFragment
import com.here.msdkuiapp.common.Constant
import com.here.msdkuiapp.common.routepreview.RoutePreviewFragment
import com.here.msdkuiapp.map.MapFragmentWrapper

/**
 * Fragment class to show route maneuver list.
 */
class RouteManeuverListFragment : RetainFragment() {

    companion object {
        fun newInstance() = RouteManeuverListFragment()
    }

    private var route: Route? = null
    private var isTrafficEnabled: Boolean = false
    internal var baseFragmentCoordinator: BaseFragmentCoordinator? = null
        get() = field ?: BaseFragmentCoordinator(manager!!)
    internal var mapFragment: MapFragmentWrapper? = null
        get() = field ?: childFragmentManager.findFragmentById(R.id.mapfragment_wrapper) as? MapFragmentWrapper
    internal var manager: FragmentManager? = null
       get() = field ?: childFragmentManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.route_maneuver_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapFragment?.start {}
        with(baseFragmentCoordinator!!) {
                val bundle = Bundle().apply {
                    putInt(Constant.GO_VISIBILITY, View.GONE)
                }
                addFragment(R.id.route_preview_fragment, RoutePreviewFragment::class.java, false,
                        bundle).apply {
                    setRoute(route!!, isTrafficEnabled)
                }
        }
    }

    /**
     * Sets already calculated route to render all route information on this fragment.
     *
     * @param route [Route].
     */
    fun setRoute(route: Route, isTrafficEnabled: Boolean = true) {
        this.route = route
        this.isTrafficEnabled = isTrafficEnabled
    }

    /**
     * Delegates call to render the given route on map
     *
     * @param route Route to be rendered on map.
     */
    fun renderRoute(route: Route) {
        with(mapFragment!!) {
            renderAndZoomTo(route)
        }
    }
}