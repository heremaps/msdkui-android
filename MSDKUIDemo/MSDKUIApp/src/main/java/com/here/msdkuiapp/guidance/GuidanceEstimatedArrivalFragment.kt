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

import android.content.res.Configuration
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.view.View.TEXT_ALIGNMENT_VIEW_START
import android.view.ViewGroup
import android.widget.TextView
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewData
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewPresenter
import com.here.msdkui.guidance.GuidanceEstimatedArrivalView
import com.here.msdkui.guidance.GuidanceEstimatedArrivalViewListener
import com.here.msdkuiapp.R
import com.here.msdkuiapp.common.Util
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions

/**
 * Fragment class for GuidanceEstimatedArrivalView.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class GuidanceEstimatedArrivalFragment : Fragment(), GuidanceEstimatedArrivalViewListener {

    internal var viewPresenter : GuidanceEstimatedArrivalViewPresenter? = null

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = GuidanceEstimatedArrivalFragment()
    }

    /**
     * Creates view.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return GuidanceEstimatedArrivalView(activity).apply {
            id = R.id.guidanceEstimatedArrivalViewId
            unitSystem = Util.getLocaleUnit()
        }
    }

    /**
     * Creates Presenter for this [GuidanceEstimatedArrivalFragment].
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<TextView>(R.id.eta).textAlignment =
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) TEXT_ALIGNMENT_CENTER
                else TEXT_ALIGNMENT_VIEW_START
        if (viewPresenter == null) {
            viewPresenter = GuidanceEstimatedArrivalViewPresenter(SingletonHelper.navigationManager).apply {
                addListener(this@GuidanceEstimatedArrivalFragment)
                resume()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewPresenter?.pause()
    }

    override fun onResume() {
        super.onResume()
        viewPresenter?.resume()
    }

    override fun onDataChanged(viewData: GuidanceEstimatedArrivalViewData?) {
        (view as? GuidanceEstimatedArrivalView)?.estimatedArrivalData = viewData
    }
}