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

package com.here.msdkuiapp.espresso.impl.views.routeplanner.screens

import android.support.test.espresso.DataInteraction
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.here.msdkuiapp.R
import com.here.msdkuiapp.espresso.impl.core.CoreMatchers.withIndex
import org.hamcrest.CoreMatchers.anything

/**
 * Route planner specific options panel related ViewInteractions selectors
 */
object RoutePlannerOptionsView {

    /**
     * @return The [ViewInteraction] Route Type setting on options panel
     */
    val onOptionsPanelRouteTypeLabelView: ViewInteraction
        get() = onView(withText(R.string.msdkui_route_type_title))

    /**
     * @return The [ViewInteraction] Route Options setting on options panel
     */
    val onOptionsPanelRouteOptionsSettingView: ViewInteraction
        get() = onView(withText(R.string.msdkui_routing_options_title))

    /**
     * @return The [ViewInteraction] Hazardous Materials setting on options panel
     */
    val onOptionsPanelHazardousMaterialsSettingView: ViewInteraction
        get() = onView(withText(R.string.msdkui_hazardous_materials_title))

    /**
     * @return The [ViewInteraction] Truck options setting on options panel
     */
    val onOptionsPanelTruckOptionsSettingView: ViewInteraction
        get() = onView(withText(R.string.msdkui_truck_options_title))

    /**
     * @return The [ViewInteraction] Avoid Traffic setting on options panel
     */
    val onOptionsPanelAvoidTrafficLabelView: ViewInteraction
        get() = onView(withText(R.string.msdkui_traffic))

    /**
     * @return The [ViewInteraction] Tunnels Allowed setting on options panel
     */
    val onOptionsPanelTunnelsAllowedLabelView: ViewInteraction
        get() = onView(withText(R.string.msdkui_tunnels_allowed_title))

    /**
     * return The [DataInteraction] Spinner item from list view on options panel
     */
    fun onOptionsPanelContextMenuListView(item: Int): DataInteraction = onData(anything()).atPosition(item)

    /**
     * @return The [ViewInteraction] Avoid traffic default spinner value on options panel
     */
    fun onOptionsPanelSpinnerView(item: Int): ViewInteraction = onView(withIndex(withId(R.id.spinner_text), item))
}