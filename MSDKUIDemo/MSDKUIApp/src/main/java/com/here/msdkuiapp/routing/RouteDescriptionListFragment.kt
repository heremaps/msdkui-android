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
import com.here.android.mpa.routing.Route
import com.here.msdkui.routing.CustomRecyclerView
import com.here.msdkui.routing.RouteDescriptionItem
import com.here.msdkuiapp.R
import com.here.msdkuiapp.coordinator
import kotlinx.android.synthetic.main.route_description_list.*

/**
 * Displays Route description list.
 */
class RouteDescriptionListFragment() : Fragment(), RoutingContracts.RouteDescriptionList {

    private val presenter = RouteDescriptionListPresenter()

    /**
     * List item click listener.
     */
    private val mItemClickedListener = object : CustomRecyclerView.OnItemClickedListener {
        override fun onItemClicked(index: Int, item: View?) {
            listener?.onItemSelected(index, item as RouteDescriptionItem)
        }

        override fun onItemLongClicked(index: Int, item: View?) {}
    }

    init {
        retainInstance = true
    }

    companion object {
        fun newInstance() = RouteDescriptionListFragment()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isResumed) {
            presenter.makeUiDataReady()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.route_description_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.run {
            onAttach(activity, this@RouteDescriptionListFragment)
            makeUiDataReady()
        }
        listener = coordinator as? RoutingCoordinator
    }

    override fun onUiDataReady(isTraffic: Boolean, route: List<Route>) {
        route_description_list_heading?.visibility = View.GONE
        route_description_list?.run {
            isTrafficEnabled = isTraffic
            this.routes = route
            setOnItemClickedListener(mItemClickedListener)
        }
    }

    /**
     * Update routes to populate UI.
     */
    fun updateRoutes(routes: List<Route>) {
        presenter.updateRoutes(routes)
        presenter.makeUiDataReady()
    }

    /**
     * Update title visibility.
     * @param notVisible true if title should not be visible, false otherwise.
     */
    fun updateTitle(notVisible: Boolean) {
        route_description_list_heading?.visibility = if (notVisible) View.GONE else View.VISIBLE
    }

    /**
     * Sets Gets traffic mode.
     */
    var traffic: Boolean
        get() = presenter.trafficMode
        set(value) {
            presenter.trafficMode = value
        }

    public var listener: Listener? = null

    /**
     * Listener to communicate with [RouteDescriptionListFragment].
     */
    interface Listener {
        /**
         * Notifies when item of list is selected.
         *
         * @param index index of item clicked.
         * @param item associated [RouteDescriptionItem].
         */
        fun onItemSelected(index: Int?, item: RouteDescriptionItem)
    }
}