/*
 * Copyright (C) 2017-2021 HERE Europe B.V.
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

import androidx.fragment.app.Fragment
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.common.GeoCoordinate
import com.here.msdkui.common.ThemeUtil
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.*
import com.here.msdkuiapp.common.mapselection.WaypointSelectionFragment
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.waypoint_selection.*

/**
 * Fragment to display waypoint selection from map for guidance.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceWaypointSelectionFragment() : Fragment(), GuidanceContracts.GuidanceWaypointSelection {

    internal var presenter = GuidanceWaypointSelectionPresenter()

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceWaypointSelectionFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.run {
            onAttach(activity!!, this@GuidanceWaypointSelectionFragment)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.guidance_waypoint_selection, container, false).apply { isClickable = true }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.run {
            coordinatorListener = coordinator as? GuidanceRouteSelectionCoordinator
            setUpActionBar(activity?.appActionBar)
            updateUI()
        }
        selected_waypoint_label!!.hideAccessibilityExtraInfo()
    }

    override fun onUiUpdate(textValue: String, withColor: Boolean, rightIconVisible: Boolean) {
        if (activity == null || view == null) {
            return
        }

        if (withColor) {
            ws_parent?.setBackgroundColor(ThemeUtil.getColor(activity, R.attr.colorAccent))
        } else {
            ws_parent?.setBackgroundColor(ThemeUtil.getColor(activity, R.attr.colorBackgroundViewDark))
        }

        val loading = getString(R.string.msdkui_app_userposition_search) == textValue
        ws_placeholder_loading?.visibility = if (loading) View.VISIBLE else View.GONE
        ws_placeholder?.visibility = if (loading) View.INVISIBLE else View.VISIBLE

        with(activity!!.acRightIcon) {
            this?.visibility = if (rightIconVisible) {
                val color = if (withColor) R.attr.colorAccent else R.attr.colorHintLight
                this?.setColorFilter(ThemeUtil.getColor(activity, color),
                        PorterDuff.Mode.SRC_ATOP)
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        with(selected_waypoint_label) {
            visibility = View.VISIBLE
            text = textValue
        }
    }

    override fun onProgress(visible: Boolean) {
        activity?.showProgressBar(visible)
        activity?.acRightIcon?.isEnabled = !visible
    }

    /**
     * Update fragment with given [GeoCoordinate]. This will do reverse geo-coding to get name for
     * the given [GeoCoordinate].
     *
     * @param cord  [GeoCoordinate]
     */
    fun updateCord(cord: GeoCoordinate) {
        presenter.updateCord(cord)
    }

    /**
     * Listener to communicate with [WaypointSelectionFragment].
     */
    interface Listener {

        /**
         * Callback to be called when user select a waypoint.
         *
         * @param entry selected [WaypointEntry].
         */
        fun onWaypointSelected(entry: WaypointEntry)

        /**
         * Indicates position is available
         */
        fun onPositionAvailable()
    }
}