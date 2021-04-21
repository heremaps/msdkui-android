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

package com.here.msdkuiapp.routing

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.here.android.mpa.routing.DynamicPenalty
import com.here.android.mpa.routing.Route
import com.here.android.mpa.routing.RouteOptions
import com.here.msdkuiapp.R
import com.here.msdkuiapp.appActionBar
import com.here.msdkuiapp.coordinator
import kotlinx.android.extensions.CacheImplementation
import kotlinx.android.extensions.ContainerOptions
import kotlinx.android.synthetic.main.option_panel.optionpanel_container

/**
 * Fragment to show different [RouteOptions] on UI.
 */
@ContainerOptions(CacheImplementation.NO_CACHE)
class OptionPanelFragment : Fragment(), RoutingContracts.OptionPanel {

    internal var presenter = OptionPanelPresenter()

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = OptionPanelFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        listener = coordinator as? RoutingCoordinator
        // create container for panels
        return inflater.inflate(R.layout.option_panel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.run {
            onAttach(activity!!, this@OptionPanelFragment)
            updateActionBar(activity?.appActionBar)
            makeUiDataReady()
        }
    }

    override fun onSubPanelClicked(panelType: Panels, routeOptions: RouteOptions?) {
        listener?.openSubPanel(panelType, routeOptions)
    }

    override fun trafficChanged(trafficPenaltyMode: Route.TrafficPenaltyMode?) {
        listener?.trafficChanged(trafficPenaltyMode)
    }

    override fun onUiDataReady(views: Collection<View>) {
        views.forEach { view -> optionpanel_container.addView(view) }
    }

    override fun getRowView(id: Int): View {
        val view = LayoutInflater.from(activity).inflate(R.layout.panel_row, optionpanel_container, false)
        view.findViewById<TextView>(R.id.option_panel_row).text = getString(id)
        return view
    }

    /**
     * Updates route options to populate on UI.
     */
    fun updateOptions() {
        presenter.updateOptions()
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
     * Sets & Gets [DynamicPenalty] to populate.
     */
    var dynamicPenalty: DynamicPenalty?
        get() = presenter.dynamicPenalty
        set(value) {
            presenter.dynamicPenalty = value
        }


    var listener: Listener? = null

    /**
     * Listener to communicate with [OptionPanelFragment].
     */
    interface Listener {

        /**
         * Notifies to open sub option panelType.
         *
         * @param panelType type of panelType
         * @param options [RouteOptions]
         */
        fun openSubPanel(panelType: Panels, options: RouteOptions?)

        /**
         * Notify that traffic condition has been changed from settings.
         *
         * @param penalty [Route.TrafficPenaltyMode].
         */
        fun trafficChanged(penalty: Route.TrafficPenaltyMode?)
    }
}