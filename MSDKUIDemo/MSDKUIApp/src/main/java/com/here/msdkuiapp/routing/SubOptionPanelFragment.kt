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

package com.here.msdkuiapp.routing

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkuiapp.R
import com.here.msdkuiapp.appActionBar
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.option_panel.*

/**
 * Fragment to show [RouteOptions] on UI.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class SubOptionPanelFragment() : Fragment(), RoutingContracts.SubOptionPanel {

    private val presenter = SubOptionPanelPresenter()

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = SubOptionPanelFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // create container for panels
        return inflater.inflate(R.layout.option_panel, container, false).apply { isClickable = true }
    }

    /**
     * Sets & Gets [RouteOptions] to populate.
     */
    var routeOptions: RouteOptions?
        get() = presenter.routeOptions
        set(value) {
            presenter.routeOptions = value
        }

    /**
     * Sets the type of [OptionPanelFragment].
     */
    var type: Panels?
        get() = presenter.type
        set(value) {
            presenter.type = value
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.run {
            onAttach(activity, this@SubOptionPanelFragment)
            updateActionBar(activity?.appActionBar)
            makeUiDataReady(activity?.appActionBar)
        }
    }

    override fun onUiDataReady(view: View) {
        optionpanel_container.addView(view)
    }

}