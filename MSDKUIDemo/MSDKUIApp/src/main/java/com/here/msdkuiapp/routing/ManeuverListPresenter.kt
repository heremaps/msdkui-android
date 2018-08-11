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

import android.content.res.Configuration
import android.view.View
import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.CustomRecyclerView
import com.here.msdkui.routing.ManeuverDescriptionItem
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BasePresenter
import com.here.msdkuiapp.common.AppActionBar
import com.here.msdkuiapp.guidance.GuidanceRoutePreviewFragment

/**
 * Handles all logic of [ManeuverListFragment].
 */
class ManeuverListPresenter : BasePresenter<RoutingContracts.ManeuverList>() {

    private val state = State()
    var coordinatorListener: ManeuverListFragment.Listener? = null

    /**
     * Click listener for list.
     */
    val itemClickedListener = object : CustomRecyclerView.OnItemClickedListener {
        override fun onItemClicked(index: Int, item: View?) {
            coordinatorListener?.onManeuverClicked(index, item as ManeuverDescriptionItem)
        }

        override fun onItemLongClicked(index: Int, item: View?) {}
    }

    /**
     * Updates the action bar to show options corresponding to the [GuidanceRoutePreviewFragment].
     */
    fun updateActionBar(appActionBar: AppActionBar?) {
        appActionBar?.run {
            setBack(visible = true, id = R.drawable.ic_arrow_back_black_24dp)
            setTitle(value = context!!.getString(R.string.msdkui_app_route_preview_title))
            setRightIcon(visible = false)
        }
    }

    /**
     * Populates config changes.
     *
     * @param orientation orientation to populate changes.
     */
    fun populateConfigChanges(orientation: Int) {
        val visibility = orientation != Configuration.ORIENTATION_LANDSCAPE
        contract?.updateConfigChanges(visibility)
        coordinatorListener?.zoomToRoute(visibility)
    }

    /**
     * Populates UI.
     */
    fun makeUiDataReady() {
        state.route?.run {
            contract?.onUiDataReady(state.isTraffic, this)
        }
    }

    /**
     * Updates route to populate [ManeuverListFragment].
     *
     * @param route given [Route]
     * @param withTraffic true if list should consider traffic data, false otherwise.
     */
    fun updateRoute(route: Route, withTraffic: Boolean) {
        state.route = route
        state.isTraffic = withTraffic
    }

    /**
     * Handles route item clicked based on orientation.
     *
     * @param orientation phone orientation
     */
    fun routeItemClicked(orientation: Int) {
        coordinatorListener?.zoomToRoute(orientation != Configuration.ORIENTATION_LANDSCAPE)
    }

    private class State {
        var route: Route? = null
        var isTraffic: Boolean = false
    }
}