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
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.here.android.mpa.routing.Route
import com.here.msdkui.guidance.GuidanceManeuverView
import com.here.msdkui.guidance.GuidanceNextManeuverData
import com.here.msdkui.guidance.GuidanceNextManeuverView
import com.here.msdkui.guidance.GuidanceNextManeuverListener
import com.here.msdkui.guidance.GuidanceNextManeuverPresenter
import com.here.msdkuiapp.R
import com.here.msdkuiapp.common.Util
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Fragment class for NextManeuverPanel View.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceNextManeuverFragment : Fragment(), GuidanceNextManeuverListener {

    internal var route: Route? = null
    internal var presenter: GuidanceNextManeuverPresenter? = null

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceNextManeuverFragment()
    }

    /**
     * Creates Panel View.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =  inflater.inflate(R.layout.guidance_next_maneuver_fragment, container, false) as GuidanceNextManeuverView
        view.unitSystem =  Util.getLocaleUnit()
        return view
    }

    /**
     * Creates Presenter for this [GuidanceNextManeuverFragment].
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (presenter == null) {
            presenter = GuidanceNextManeuverPresenter(view.context, SingletonHelper.navigationManager, route).apply {
                addListener(this@GuidanceNextManeuverFragment)
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

    override fun onDataChanged(data: GuidanceNextManeuverData?) {
        val nextManeuverView = (view as? GuidanceNextManeuverView)
        data?.run {
            nextManeuverView?.visibility = VISIBLE
            nextManeuverView?.nextManeuverData = data
        } ?: run {
            nextManeuverView?.visibility = GONE
        }
    }
}