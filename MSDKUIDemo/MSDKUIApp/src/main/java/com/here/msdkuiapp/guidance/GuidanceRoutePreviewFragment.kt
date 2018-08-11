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

import android.app.AlertDialog
import android.app.Fragment
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.*
import com.here.msdkuiapp.landing.LandingActivity
import kotlinx.android.synthetic.main.guidance_route_preview.*

/**
 * Fragment class for Guidance Route preview.
 */
class GuidanceRoutePreviewFragment : Fragment(), GuidanceContracts.RoutePreview {

    var presenter: GuidanceRoutePreviewFragmentPresenter = GuidanceRoutePreviewFragmentPresenter()
    var listener: Listener? = null

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceRoutePreviewFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.guidance_route_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.run {
            if (context == null) {
                if (onAttach(activity, this@GuidanceRoutePreviewFragment).not()) {
                    activity.startActivity(Intent(activity, LandingActivity::class.java))
                    return
                }
                calculateRoute()
            } else {
                populateUI()
            }
            updateActionBar(activity.appActionBar)
        }
        destination.visibility =
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) View.GONE
                else View.VISIBLE
        go.setOnClickListener { presenter.startGuidance(false) }
        go.setOnLongClickListener {
            showStartSimulationAlertDialog()
            true
        }
        listener = coordinator as? Listener
    }

    override fun getCurrentViewContract(): GuidanceContracts.RoutePreview {
        return this
    }

    override fun onProgress(visible: Boolean) {
        route_preview_progress_bar?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun populateUI(entry: WaypointEntry, route: Route) {
        val text = activity.getString(R.string.msdkui_rp_to, entry.name)
        destination.text = text
        with(description) {
            isTrafficEnabled = true
            this.route = route
        }
        listener?.renderRoute(route)
    }

    override fun routingFailed(reason: String) {
        listOf<View>(destination, description, divider, go).setVisibility(View.INVISIBLE)
        error_message.text = reason
    }

    private fun showStartSimulationAlertDialog() {
        AlertDialog.Builder(activity)
                .setMessage(R.string.msdkui_app_guidance_start_simulation)
                .setCancelable(true)
                .setNegativeButton(R.string.msdkui_app_cancel, null)
                .setPositiveButton(R.string.msdkui_app_ok) { _, _ ->
                    presenter.startGuidance(true)
                }
                .create()
                .show()
    }

    /**
     * Listener for interacting with [GuidanceRoutePreviewFragment].
     */
    interface Listener {
        /**
         * Renders route on map.
         */
        fun renderRoute(route: Route)
    }
}