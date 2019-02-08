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

package com.here.msdkuiapp.guidance

import android.widget.Toast
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.common.MapEngine
import com.here.android.mpa.search.ErrorCode
import com.here.android.mpa.search.Location
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.GuidanceContracts
import com.here.msdkuiapp.R
import com.here.msdkuiapp.base.BasePresenter
import com.here.msdkuiapp.common.AppActionBar
import com.here.msdkuiapp.common.Provider
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import com.here.msdkuiapp.position.AppPositioningManager
import java.util.*

/**
 * Presenter class to handle logic for [GuidanceWaypointSelectionFragment].
 */
class GuidanceWaypointSelectionPresenter : BasePresenter<GuidanceContracts.GuidanceWaypointSelection>() {

    private val state = State()
    internal var provider = Provider()
    var coordinatorListener: GuidanceWaypointSelectionFragment.Listener? = null

    /**
     * Displays the action bar needed for [GuidanceWaypointSelectionFragment].
     *
     * @param appActionBar [AppActionBar]
     */
    fun setUpActionBar(appActionBar: AppActionBar?) {
        appActionBar?.run {
            setBack(true, R.drawable.ic_arrow_back_black_24dp)
            setTitle(value = context!!.getString(R.string.msdkui_app_guidance_teaser_title))
            setRightIcon(visible = true, id = R.drawable.ic_check_black_24dp,
                    accessibleValue = context!!.getString(R.string.msdkui_app_done),
                    clickListener = { onRightIconClicked(state.entry) })
        }
    }

    private fun onRightIconClicked(entry: WaypointEntry?) {
        if (entry != null && entry.isValid) {
            coordinatorListener?.onWaypointSelected(entry)
        } else {
            Toast.makeText(context!!, getString(R.string.msdkui_app_waypoint_not_valid), Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Updates the top text view of [GuidanceWaypointSelectionFragment] for new text.
     */
    fun updateUI() {
        state.entry?.run {
            if (!stringLabel.isNullOrBlank()) {
                contract?.onUiUpdate(textValue = stringLabel!!, withColor = true, rightIconVisible = true)
                return
            } else if (resourceIdLabel != 0) {
                contract?.onUiUpdate(textValue = getString(resourceIdLabel), withColor = true, rightIconVisible = true)
                return
            }
            routeWaypoint?.originalPosition?.run {
                contract?.onUiUpdate(textValue = context!!.getString(R.string.msdkui_app_cord,
                        String.format(Locale.ENGLISH, "%.5f", latitude),
                        String.format(Locale.ENGLISH, "%.5f", longitude)), withColor = true, rightIconVisible = true)
            } ?: handleDefaultCase()
        } ?: handleDefaultCase()
    }

    private fun handleDefaultCase() {
        if (MapEngine.isInitialized() && appPositioningManager!!.isValidPosition) {
            contract?.onUiUpdate(getString(R.string.msdkui_app_guidance_waypoint_subtitle),
                    withColor = false, rightIconVisible = true)
        } else {
            contract?.onUiUpdate(getString(R.string.msdkui_app_userposition_search), false)
        }
    }

    private val positionChangeListener = object : AppPositioningManager.Listener {
        override fun onPositionAvailable() {
            updateUI()
            state.entry ?: coordinatorListener?.onPositionAvailable()
        }
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

    /**
     * Indicates location services and permission are ready to use.
     */
    fun onLocationReady() {
        contract?.onUiUpdate(getString(R.string.msdkui_app_userposition_search), false)
        appPositioningManager?.initPositioning(positionChangeListener)
    }

    private class State {
        var entry: WaypointEntry? = null
    }
}