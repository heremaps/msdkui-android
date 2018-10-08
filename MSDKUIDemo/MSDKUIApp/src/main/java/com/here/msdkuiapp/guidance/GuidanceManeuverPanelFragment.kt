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

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.routing.Route
import com.here.msdkui.common.ThemeUtil
import com.here.msdkui.guidance.GuidanceManeuverData
import com.here.msdkui.guidance.GuidanceManeuverPanel
import com.here.msdkui.guidance.GuidanceManeuverPanelListener
import com.here.msdkui.guidance.GuidanceManeuverPanelPresenter
import com.here.msdkuiapp.R
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Fragment class for ManeuverPanel View.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceManeuverPanelFragment : Fragment(), GuidanceManeuverPanelListener {

    private var _route: Route? = null
    internal var panelPresenter: GuidanceManeuverPanelPresenter? = null

    /**
     * Setter getter for [Route].
     */
    internal var route: Route?
        get() = _route
        set(value) {
            _route = value
        }

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceManeuverPanelFragment()
    }

    /**
     * Creates Panel View.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val panelFragment = GuidanceManeuverPanel(activity)
        panelFragment.id = R.id.guidanceManeuverPanelId
        return panelFragment
    }

    /**
     * Creates Presenter for this GuidanceManeuverPanelFragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (panelPresenter == null) {
            panelPresenter = GuidanceManeuverPanelPresenter(view.context, SingletonHelper.navigationManager, route).apply {
                addListener(this@GuidanceManeuverPanelFragment)
                resume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        panelPresenter?.pause()
    }

    override fun onResume() {
        super.onResume()
        panelPresenter?.resume()
    }

    override fun onDataChanged(data: GuidanceManeuverData?) {
        (view as? GuidanceManeuverPanel)?.maneuverData = data
    }

    override fun onDestinationReached() {
        (view as? GuidanceManeuverPanel)?.highLightManeuver(ThemeUtil.getColor(activity,
                com.here.msdkui.R.attr.colorAccentLight))
    }
}