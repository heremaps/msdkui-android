/*
 * Copyright (C) 2017-2019 HERE Europe B.V.
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
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.msdkui.common.ThemeUtil
import com.here.msdkui.guidance.GuidanceSpeedView
import com.here.msdkui.guidance.GuidanceSpeedData
import com.here.msdkui.guidance.GuidanceSpeedListener
import com.here.msdkui.guidance.GuidanceSpeedPresenter
import com.here.msdkuiapp.R
import com.here.msdkuiapp.common.Util
import com.here.msdkuiapp.guidance.SingletonHelper.appPositioningManager
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.guidance_speed.*
import kotlinx.android.synthetic.main.guidance_speed.view.guidance_current_speed

/**
 * Fragment class for [GuidanceSpeedView] view.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceSpeedFragment : Fragment(), GuidanceSpeedListener {

    internal var presenter: GuidanceSpeedPresenter? = null

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceSpeedFragment()
    }

    /**
     * Creates Panel View.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.guidance_speed, container, false)
        if (isLandscapeOrientation) { // apply rounded background in landscape mode, which wil
            // change according to speed.
            view.guidance_current_speed.background = ContextCompat.getDrawable(inflater.context,
                    R.drawable.current_speed_bg)
        }
        return view
    }

    /**
     * Creates Presenter for this GuidanceStreetLabelFragment.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        guidance_current_speed.unitSystem = Util.getLocaleUnit()
        if (presenter == null) {
            presenter = GuidanceSpeedPresenter(SingletonHelper.navigationManager ?: return,
                    appPositioningManager?.sdkPositioningManager ?: return).apply {
                addListener(this@GuidanceSpeedFragment)
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

    private val isLandscapeOrientation
        get() = activity!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    private val colorNegative by lazy {
        val color = ThemeUtil.getColor(activity, R.attr.colorNegative)
        color
    }

    private val colorForeground by lazy {
        val color = ThemeUtil.getColor(activity, R.attr.colorForeground)
        color
    }

    private val colorForegroundLight by lazy {
        val color = ThemeUtil.getColor(activity, R.attr.colorForegroundLight)
        color
    }

    private val colorForegroundSecondary by lazy {
        val color = ThemeUtil.getColor(activity, R.attr.colorForeground)
        color
    }

    override fun onDataChanged(data: GuidanceSpeedData?) {
        guidance_current_speed?.run {
            setCurrentSpeedData(data)
            data?.run {
                if (isLandscapeOrientation) {
                    valueTextColor = colorForegroundLight
                    unitTextColor = colorForegroundLight
                    return
                }
                // portrait mode
                if (isSpeeding) {
                    valueTextColor = colorNegative
                    unitTextColor = colorNegative
                } else {
                    valueTextColor = colorForeground
                    unitTextColor = colorForegroundSecondary
                }
            }
        }
    }
}