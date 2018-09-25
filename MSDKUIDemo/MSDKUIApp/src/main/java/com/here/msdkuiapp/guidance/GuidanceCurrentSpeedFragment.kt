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
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.msdkui.common.ThemeUtil
import com.here.msdkui.guidance.GuidanceCurrentSpeedPanel
import com.here.msdkui.guidance.GuidanceSpeedData
import com.here.msdkui.guidance.GuidanceSpeedListener
import com.here.msdkui.guidance.GuidanceSpeedPresenter
import com.here.msdkuiapp.R
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.guidance_current_speed.*

/**
 * Fragment class for [GuidanceCurrentSpeedPanel] view.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceCurrentSpeedFragment : Fragment(), GuidanceSpeedListener {

    internal var panelPresenter: GuidanceSpeedPresenter? = null

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceCurrentSpeedFragment()
    }

    /**
     * Creates Panel View.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.guidance_current_speed, container, false)
    }

    /**
     * Creates Presenter for this GuidanceCurrentStreetFragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (panelPresenter == null) {
            panelPresenter = GuidanceSpeedPresenter(SingletonHelper.navigationManager ?: return,
                    SingletonHelper.positioningManager ?: return).apply {
                addListener(this@GuidanceCurrentSpeedFragment)
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

    override fun onDataChanged(data: GuidanceSpeedData?) {
        guidance_current_speed?.run {
            setCurrentSpeedData(data)
            data?.run {
                if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    if (isSpeeding) {
                        setValueTextColor(ThemeUtil.getColor(context, R.attr.colorNegative))
                        setUnitTextColor(ThemeUtil.getColor(context, R.attr.colorNegative))
                    } else {
                        setValueTextColor(ThemeUtil.getColor(context, R.attr.colorForeground))
                        setUnitTextColor(ThemeUtil.getColor(context, R.attr.colorForegroundSecondary))
                    }
                }
            }
        }
    }
}