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

import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.routing.Route
import com.here.msdkui.guidance.GuidanceNextManeuverData
import com.here.msdkui.guidance.GuidanceNextManeuverPanel
import com.here.msdkui.guidance.GuidanceNextManeuverPanelListener
import com.here.msdkui.guidance.GuidanceNextManeuverPanelPresenter
import com.here.msdkuiapp.R
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Fragment class for NextManeuverPanel View.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceNextManeuverPanelFragment : Fragment(), GuidanceNextManeuverPanelListener {

    internal var route: Route? = null
    internal var panelPresenter: GuidanceNextManeuverPanelPresenter? = null

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceNextManeuverPanelFragment()
    }

    /**
     * Creates Panel View.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val panelFragment = GuidanceNextManeuverPanel(activity)
        panelFragment.id = R.id.guidanceNextManeuverPanelId;
        return panelFragment
    }

    /**
     * Creates Presenter for this [GuidanceNextManeuverPanelFragment].
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (panelPresenter == null) {
            panelPresenter = GuidanceNextManeuverPanelPresenter(view.context, SingletonHelper.navigationManager, route).apply {
                addListener(this@GuidanceNextManeuverPanelFragment)
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

    override fun onDataChanged(data: GuidanceNextManeuverData?) {
        (view as? GuidanceNextManeuverPanel)?.nextManeuverData = data
    }
}