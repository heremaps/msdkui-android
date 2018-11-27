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

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.here.android.mpa.common.GeoCoordinate
import com.here.msdkui.common.ThemeUtil
import com.here.msdkui.routing.WaypointEntry
import com.here.msdkuiapp.*
import com.here.msdkuiapp.base.RetainFragment
import com.here.msdkuiapp.map.MapFragmentWrapper
import com.here.msdkuiapp.routing.RoutingCoordinator
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.waypoint_selection.*

/**
 * Fragment for waypoint selection from map.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class WaypointSelectionFragment() : RetainFragment(),
        CommonContracts.WaypointSelection, MapFragmentWrapper.Listener {

    internal var presenter = WaypointSelectionPresenter()
    internal var mapFragment: MapFragmentWrapper? = null
        get() = field ?: (childFragmentManager.findFragmentById(R.id.mapfragment_wrapper) as? MapFragmentWrapper)
    // In roboelectric, supported fragment activity get hang while inflating a fragment which consist another fragment
    // so created a instance to inject it instead of loading the fragment.
    internal var activityInstance: FragmentActivity? = null
        get() = field ?: activity

    /**
     * Sets Gets traffic mode.
     */
    var traffic: Boolean
        get() = presenter.trafficMode
        set(value) {
            presenter.trafficMode = value
        }

    companion object {
        fun newInstance() = WaypointSelectionFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.waypoint_selection, container, false).apply { isClickable = true }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.run {
            activityInstance?.run {
                onAttach(this, this@WaypointSelectionFragment)
                setUpActionBar(appActionBar)
            }
            updateUI()
        }
        selected_waypoint_label?.hideAccessibilityExtraInfo()
        mapFragment?.start(this::onMapEngineInit)
    }

    override fun onPointSelectedOnMap(cord: GeoCoordinate) {
        updateCord(cord)
    }

    private fun onMapEngineInit() {
        with(mapFragment!!) {
            traffic = this@WaypointSelectionFragment.traffic
            onTouch(true, this@WaypointSelectionFragment)
            view?.isFocusable = true
        }
    }

    /**
     * Updates position with given [WaypointEntry].
     *
     * @param index index of entry to keep track of entry.
     * @param entry [WaypointEntry]
     */
    fun updatePosition(index: Int = -1, entry: WaypointEntry) {
        presenter.updatePosition(index, entry)
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

    override fun onRightIconClicked(index: Int?, entry: WaypointEntry?) {
        if (entry != null && entry.isValid) {
            (coordinator as? RoutingCoordinator)?.onWaypointSelected(index, entry)
        } else {
            Toast.makeText(activityInstance, activityInstance!!.getString(R.string.msdkui_app_waypoint_not_valid),
                    Toast.LENGTH_LONG).show()
        }
    }

    override fun onUiUpdate(value: String, withColor: Boolean) {
        if (withColor) {
            ws_container?.setBackgroundColor(ThemeUtil.getColor(activityInstance, R.attr.colorAccent))
            activityInstance?.acRightIcon?.setColorFilter(ThemeUtil.getColor(activityInstance, R.attr.colorAccent),
                    PorterDuff.Mode.SRC_ATOP)
        } else {
            activityInstance?.acRightIcon?.setColorFilter(ThemeUtil.getColor(activityInstance, R.attr.colorHintLight),
                    PorterDuff.Mode.SRC_ATOP)
        }

        with(selected_waypoint_label) {
            visibility = View.VISIBLE
            text = value
        }
    }

    override fun onProgress(visible: Boolean) {
       with(activityInstance!!) {
           showProgressBar(visible)
           acRightIcon?.isEnabled = !visible
       }
    }

    /**
     * Listener to communicate with [WaypointSelectionFragment].
     */
    interface Listener {

        /**
         * Callback to be called when user select a waypoint.
         *
         * @param index a integer if you want to track waypoint entry, useful like using in list, null otherwise.
         */
        fun onWaypointSelected(index: Int?, current: WaypointEntry)
    }
}