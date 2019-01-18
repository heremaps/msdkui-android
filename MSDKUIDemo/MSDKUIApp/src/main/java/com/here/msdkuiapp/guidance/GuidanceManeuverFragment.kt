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
import com.here.msdkui.common.ThemeUtil
import com.here.msdkui.guidance.GuidanceManeuverData
import com.here.msdkui.guidance.GuidanceManeuverView
import com.here.msdkui.guidance.GuidanceManeuverListener
import com.here.msdkui.guidance.GuidanceManeuverPresenter
import com.here.msdkuiapp.R
import com.here.msdkuiapp.common.Util
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Fragment class for ManeuverPanel View.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceManeuverFragment : Fragment(), GuidanceManeuverListener {

    private var _route: Route? = null
    internal var presenter: GuidanceManeuverPresenter? = null

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
        fun newInstance() = GuidanceManeuverFragment()
    }

    /**
     * Creates Panel View.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.guidance_maneuver_fragment, container, false) as GuidanceManeuverView
        view.unitSystem =  Util.getLocaleUnit()
        return view
    }

    /**
     * Creates Presenter for this GuidanceManeuverFragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (presenter == null) {
            presenter = GuidanceManeuverPresenter(view.context, SingletonHelper.navigationManager, route).apply {
                addListener(this@GuidanceManeuverFragment)
                resume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        presenter?.pause()
    }

    override fun onResume() {
        super.onResume()
        presenter?.resume()
    }

    override fun onDataChanged(data: GuidanceManeuverData?) {
        val notifiedView = view as? GuidanceManeuverView
        data?.run {
            notifiedView?.setViewState(GuidanceManeuverView.State(data))
        } ?: notifiedView?.setViewState(GuidanceManeuverView.State.UPDATING)

    }

    override fun onDestinationReached() {
        (view as? GuidanceManeuverView)?.highLightManeuver(ThemeUtil.getColor(activity,
                com.here.msdkui.R.attr.colorAccentLight))
    }
}