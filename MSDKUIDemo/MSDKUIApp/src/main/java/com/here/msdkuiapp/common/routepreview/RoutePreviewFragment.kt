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

package com.here.msdkuiapp.common.routepreview

import android.support.v4.app.Fragment
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.*
import com.here.msdkuiapp.common.Constant.GO_VISIBILITY
import com.here.msdkuiapp.common.Util
import com.here.msdkuiapp.landing.LandingActivity
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.guidance_route_preview.*

/**
 * Fragment class for Guidance Route preview.
 *
 * Please note that setting destination & no route will leads route calculation with current position.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class RoutePreviewFragment : Fragment(), GuidanceContracts.RoutePreview {

    var presenter: RoutePreviewFragmentPresenter = RoutePreviewFragmentPresenter()
    var listener: Listener? = null

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = RoutePreviewFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.guidance_route_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.run {
            if (presenter.context == null) {
                if (onAttach(activity!!, this@RoutePreviewFragment).not()) {
                    activity!!.startActivity(Intent(activity, LandingActivity::class.java))
                    return
                }
                doSetup()
            } else {
                populateUI()
            }
            updateActionBar(activity!!.appActionBar)
        }
        destination.visibility =
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) View.GONE
                else View.VISIBLE
        with(go) {
            visibility = arguments?.run {
                getInt(GO_VISIBILITY)
            } ?: View.VISIBLE
            setOnClickListener { presenter.startGuidance(false) }
            setOnLongClickListener {
                presenter.showStartSimulationAlertDialog()
                true
            }
        }
        see_steps.setOnClickListener { presenter.toggleSteps() }
        listener = coordinator as? Listener
    }

    /**
     * Toggles route's maneuver steps.
     */
    override fun toggleSteps(listVisible: Boolean) {
        if (listVisible.not()) {
            see_steps.text = activity!!.getText(com.here.msdkuiapp.R.string.msdkui_app_guidance_button_showmaneuvers)
            guidance_maneuver_list.visibility = View.GONE
            list_end_divider.visibility = View.GONE
            place_holder.visibility = View.VISIBLE
        } else {
            see_steps.text = activity!!.getText(com.here.msdkuiapp.R.string.msdkui_app_guidance_button_showmap)
            guidance_maneuver_list.visibility = View.VISIBLE
            list_end_divider.visibility = View.VISIBLE
            place_holder.visibility = View.GONE
            activity?.appActionBar?.titleView?.run {
                sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
            }
        }
    }

    override fun onProgress(visible: Boolean) {
        route_preview_progress_bar?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun populateUI(entry: WaypointEntry, route: Route, listVisible: Boolean, trafficEnabled: Boolean) {
        val text = activity!!.getString(R.string.msdkui_rp_to, entry.getLabel(activity!!, ""))

        destination.text = text
        with(description) {
            isTrafficEnabled = trafficEnabled
            unitSystem = Util.getLocaleUnit()
            this.route = route
        }
        with(guidance_maneuver_list) {
            unitSystem = Util.getLocaleUnit()
            this.route = route
        }

        listener?.renderRoute(route)
        toggleSteps(listVisible)
    }

    override fun routingFailed(reason: String) {
        listOf<View>(destination, description, divider, go, see_steps).setVisibility(View.INVISIBLE)
        error_message.text = reason
    }

    /**
     * Sets destination for route calculation. Route will be calculated between given destination & current position.
     *
     * @param destination [WaypointEntry].
     */
    fun setWaypoint(destination: WaypointEntry, withCurrentPosition: Boolean = true) {
        presenter.setWaypoint(destination, withCurrentPosition)
    }

    /**
     * Sets already calculated route to render all route information on this fragment.
     *
     * @param route [Route].
     */
    fun setRoute(route: Route, isTrafficEnabled: Boolean = true) {
        presenter.setRoute(route, isTrafficEnabled)
    }

    /**
     * Listener for interacting with [RoutePreviewFragment].
     */
    interface Listener {
        /**
         * Renders route on map.
         */
        fun renderRoute(route: Route)
    }
}