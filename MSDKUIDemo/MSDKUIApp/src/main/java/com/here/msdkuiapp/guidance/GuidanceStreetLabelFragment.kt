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
import com.here.msdkui.guidance.GuidanceStreetLabelView
import com.here.msdkui.guidance.GuidanceStreetLabelData
import com.here.msdkui.guidance.GuidanceStreetLabelListener
import com.here.msdkui.guidance.GuidanceStreetLabelPresenter
import com.here.msdkuiapp.R
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Fragment class for [GuidanceStreetLabelView] view.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceStreetLabelFragment : Fragment(), GuidanceStreetLabelListener {

    internal var route: Route? = null
    internal var presenter: GuidanceStreetLabelPresenter? = null

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceStreetLabelFragment()
    }

    /**
     * Creates Panel View.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val panelFragment = GuidanceStreetLabelView(activity)
        panelFragment.id = R.id.guidanceCurrentStreetId
        return panelFragment
    }

    /**
     * Creates Presenter for this GuidanceStreetLabelFragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (presenter == null) {
            presenter = GuidanceStreetLabelPresenter(view.context, SingletonHelper.navigationManager, route).apply {
                addListener(this@GuidanceStreetLabelFragment)
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

    override fun onDataChanged(labelData: GuidanceStreetLabelData) {
        (view as GuidanceStreetLabelView).setCurrentStreetData(labelData)
    }
}