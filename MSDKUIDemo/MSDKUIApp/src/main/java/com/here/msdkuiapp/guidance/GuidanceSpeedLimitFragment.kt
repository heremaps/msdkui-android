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
import com.here.msdkui.guidance.GuidanceSpeedData
import com.here.msdkui.guidance.GuidanceSpeedLimitPanel
import com.here.msdkui.guidance.GuidanceSpeedListener
import com.here.msdkui.guidance.GuidanceSpeedPresenter
import com.here.msdkuiapp.R
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Fragment class for [GuidanceSpeedLimitPanel] view.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceSpeedLimitFragment : Fragment(), GuidanceSpeedListener  {

    internal var mPresenter: GuidanceSpeedPresenter? = null

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceSpeedLimitFragment()
    }

    /**
     * Creates Panel View.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val panelFragment = GuidanceSpeedLimitPanel(activity)
        panelFragment.id = R.id.guidanceSpeedLimitPanelId
        return panelFragment
    }

    /**
     * Creates Presenter for this GuidanceCurrentStreetFragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (mPresenter == null) {
            mPresenter = GuidanceSpeedPresenter(SingletonHelper.navigationManager ?: return,
                    SingletonHelper.positioningManager ?: return).apply {
                addListener(this@GuidanceSpeedLimitFragment)
                resume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mPresenter?.pause()
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.resume()
    }

    override fun onDataChanged(data: GuidanceSpeedData?) {
        (view as GuidanceSpeedLimitPanel).setCurrentSpeedData(data)
    }
}