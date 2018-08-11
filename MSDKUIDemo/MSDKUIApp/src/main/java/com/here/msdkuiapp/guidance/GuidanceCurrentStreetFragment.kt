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
import com.here.msdkui.guidance.GuidanceCurrentStreet
import com.here.msdkui.guidance.GuidanceCurrentStreetData
import com.here.msdkui.guidance.GuidanceCurrentStreetListener
import com.here.msdkui.guidance.GuidanceCurrentStreetPresenter
import com.here.msdkuiapp.R
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Fragment class for [GuidanceCurrentStreet] view.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceCurrentStreetFragment : Fragment(), GuidanceCurrentStreetListener {

    private var mRoute: Route? = null
    internal var mPanelPresenter: GuidanceCurrentStreetPresenter? = null

    /**
     * Setter getter for [Route].
     */
    internal var route: Route?
        get() = mRoute
        set(value) {
            mRoute = value
        }

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceCurrentStreetFragment()
    }

    /**
     * Creates Panel View.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val panelFragment = GuidanceCurrentStreet(activity)
        panelFragment.id = R.id.guidanceCurrentStreetId
        return panelFragment
    }

    /**
     * Creates Presenter for this GuidanceCurrentStreetFragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (mPanelPresenter == null) {
            mPanelPresenter = GuidanceCurrentStreetPresenter(view.context, SingletonHelper.navigationManager, route).apply {
                addListener(this@GuidanceCurrentStreetFragment)
                resume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mPanelPresenter?.pause()
    }

    override fun onResume() {
        super.onResume()
        mPanelPresenter?.resume()
    }

    override fun onDataChanged(data: GuidanceCurrentStreetData) {
        (view as GuidanceCurrentStreet).setCurrentStreetData(data)
    }
}