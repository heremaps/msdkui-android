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

package com.here.msdkuiapp.common.mapselection

import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.search.ErrorCode
import com.here.android.mpa.search.Location
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.CommonContracts
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BasePresenter
import com.here.msdkuiapp.common.AppActionBar
import com.here.msdkuiapp.common.Provider
import java.util.*

/**
 * Presenter class to handle all logic of [WaypointSelectionFragment].
 */
class WaypointSelectionPresenter() : BasePresenter<CommonContracts.WaypointSelection>() {

    private val state = State()
    internal var provider = Provider()

    /**
     * Sets Gets traffic mode.
     */
    var trafficMode: Boolean
        get() = state.traffic
        set(value) {
            state.traffic = value
        }

    /**
     * Displays the action bar needed for [WaypointSelectionFragment].
     *
     * @param appActionBar [AppActionBar]
     */
    fun setUpActionBar(appActionBar: AppActionBar?) {
        appActionBar?.run {
            setBack(true, id = R.drawable.ic_clear_black_24dp)
            setTitle(value = context!!.getString(R.string.msdkui_waypoint_select_location))
            setRightIcon(id = R.drawable.ic_check_black_24dp,
                    accessibleValue = context!!.getString(R.string.msdkui_app_done),
                    clickListener = { contract?.onRightIconClicked(state.index, state.entry) })
        }
    }

    /**
     * Updates the top text view of [WaypointSelectionFragment] for new text.
     */
    fun updateUI() {
        state.entry?.run {
            // initial case
            if (resourceIdLabel == R.string.msdkui_waypoint_select_location) {
                handleDefaultCase()
                return
            }
            if (!stringLabel.isNullOrBlank()) {
                contract?.onUiUpdate(stringLabel!!, true)
                return
            } else if (resourceIdLabel != 0) {
                contract?.onUiUpdate(getString(resourceIdLabel), true)
                return
            }
            // fallback
            routeWaypoint?.originalPosition?.run {
                contract?.onUiUpdate(context!!.getString(R.string.msdkui_app_cord,
                        String.format(Locale.ENGLISH, "%.5f", latitude),
                        String.format(Locale.ENGLISH, "%.5f", longitude)), true)
            } ?: handleDefaultCase()
        } ?: handleDefaultCase()
    }

    private fun handleDefaultCase() {
        contract?.onUiUpdate(context!!.getString(R.string.msdkui_app_rp_waypoint_subtitle), false)
    }

    /**
     * Updates position with given [WaypointEntry].
     *
     * @param index index of entry to keep track of entry.
     * @param entry [WaypointEntry]
     */
    fun updatePosition(index: Int, entry: WaypointEntry) {
        state.index = index
        state.entry = entry
    }

    /**
     * Update fragment with given [GeoCoordinate]. This will do reverse geo-coding to get name for
     * the given [GeoCoordinate].
     *
     * @param cord [GeoCoordinate]
     */
    fun updateCord(cord: GeoCoordinate) {
        if (!cord.isValid) return
        state.entry = WaypointEntry(provider.providesRouteWaypoint(cord))
        contract?.onProgress(true)
        provider.providesReverseGeocodeRequest(cord).execute { location: Location?, errorCode: ErrorCode? ->
            this.onGeoRequestComplete(location, errorCode)
        }
    }

    private fun onGeoRequestComplete(location: Location?, errorCode: ErrorCode?) {
        contract?.onProgress(false)
        if (errorCode == ErrorCode.NONE) {
            state.entry?.run {
                stringLabel = location?.address?.text ?: ""
            }
        }
        updateUI()
    }

    private class State {
        var index: Int? = null
        var traffic: Boolean = false
        var entry: WaypointEntry? = null
    }
}