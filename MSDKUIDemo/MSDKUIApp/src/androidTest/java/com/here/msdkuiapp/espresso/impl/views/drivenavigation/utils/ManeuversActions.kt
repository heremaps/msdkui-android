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

package com.here.msdkuiapp.espresso.impl.views.drivenavigation.utils

import android.support.test.espresso.Espresso
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.here.msdkui.routing.ManeuverItemView
import com.here.msdkui.routing.ManeuverListAdapter
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.views.route.matchers.RouteMatchers
import com.here.msdkuiapp.espresso.impl.views.route.screens.RouteView

object ManeuversActions {
    /**
     * Iterate through maneuver list view and gather maneuvers data.
     */
    fun getManeuversDataFromManeuversList(): ArrayList<ManeuverData> {
        val retList = ArrayList<ManeuverData>()
        val itemsCount = RouteMatchers.getItemsListCount(RouteView.onRouteManeuversList)
        for (index in 0 until itemsCount) {
            var maneuverAddress = String()
            var maneuverIconTag = 0
            Espresso.onView(ViewMatchers.withId(R.id.guidance_maneuver_list))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<ManeuverListAdapter.ViewHolder>(index,
                            object : ViewAction {
                                override fun getConstraints() = ViewMatchers.isAssignableFrom(ManeuverItemView::class.java)

                                override fun getDescription() = "Get maneuver address and icon: "

                                override fun perform(uiController: UiController, view: View) {
                                    maneuverAddress = view.findViewById<TextView>(
                                            R.id.maneuver_address_view).text.toString()
                                    val viewTag = view.findViewById<ImageView>(
                                            R.id.maneuver_icon_view).tag
                                    if (viewTag is Int) {
                                        maneuverIconTag = viewTag
                                    }
                                }
                            }))
            retList.add(ManeuverData(maneuverAddress, maneuverIconTag))
        }
        return retList
    }
}